import { ICharacter } from '../Character/character'

interface INode {

    key: string
    name: string
    neighbors: INode []
    character?: ICharacter

    addNeighbors(node: INode): void

    findNode(node: INode): boolean

    deleteCharacter(): void

    placePlayer(character: ICharacter): void
}

class Node implements INode {

    key: string
    name: string
    neighbors: INode []
    character?: ICharacter

    constructor(key: string, name: string) {
        this.key = key
        this.name = name
        this.neighbors = []
    }
    addNeighbors(node: INode): void {
        const maybeNode: boolean = this.findNode(node)
        if(!maybeNode) {
            this.neighbors.push(node)
            node.addNeighbors(this)
        }
    }
    deleteCharacter(): void {
        this.character = undefined
    }

    placePlayer(character: ICharacter) {
        this.character = character
    }

    findNode(node: INode): boolean {
        return this.neighbors.reduce((acc: boolean, cur: INode) => {
            return acc || cur.name === node.name
        }, false)
    }

}

export {
    Node,
    INode
}