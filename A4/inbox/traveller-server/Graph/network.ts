import { INode } from './node'
import { Character, ICharacter } from '../Character/character'

export interface INetwork {

    nodes: INode []
    create(nodes: INode[]): void

    planRoute(begin: INode, destination: INode): boolean 

    placePlayer(character: ICharacter, nodeName: string): void
}

export class Network implements INetwork {

    nodes: INode []

    constructor() {
        this.nodes = []
    }

    create(nodes: INode[]): void {
        this.nodes = nodes
    }

    planRoute(begin: INode, destination: INode): boolean {

        const unvisitedNodes: Map<string, boolean> = new Map()
        
        this.nodes.forEach(node => {
            unvisitedNodes.set(node.name, false)
        })

        const startingCharacter = begin.character
        if (!startingCharacter) {
            return true //assume the route is possible without a character
        } else {
            return this.planHelper(unvisitedNodes, begin, destination)
        }
    }

    planHelper(unvisitedNodes: Map<string, boolean>, begin: INode, destination: INode): boolean {
        unvisitedNodes.set(begin.name, true)

        if (begin.name === destination.name && !destination.character) { //assumes there can't be a chaacter in te desination
            return true
        }

        return begin.neighbors
            .filter(node => !node.character && !unvisitedNodes.get(node.name))
            .reduce((acc: boolean, cur: INode) => {
                return acc || this.planHelper(unvisitedNodes, cur, destination)
            }, false)  
    }

    placePlayer(character: ICharacter, nodeName: string): void {
        const maybeNode: INode | undefined = this.nodes.find(node => {
            return node.name = nodeName
        }) 

        if (maybeNode) {
            const maybeCharacterNode = this.nodes.find(node => {
                node.character ? node.character.name === character.name : false
            })
            if (maybeCharacterNode) {
                maybeCharacterNode.deleteCharacter()
            }
            maybeNode.placePlayer(character)
        } 
    }
}
