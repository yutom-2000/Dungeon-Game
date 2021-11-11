"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Network = void 0;
class Network {
    constructor() {
        this.nodes = [];
    }
    create(nodes) {
        this.nodes = nodes;
    }
    planRoute(begin, destination) {
        const unvisitedNodes = new Map();
        this.nodes.forEach(node => {
            unvisitedNodes.set(node.name, false);
        });
        const startingCharacter = begin.character;
        if (!startingCharacter) {
            console.log("--not starting character--");
            return true; //assume the route is possible without a character
        }
        else {
            return this.planHelper(unvisitedNodes, begin, destination);
        }
    }
    planHelper(unvisitedNodes, begin, destination) {
        console.log();
        console.log("in plan helper=============================================");
        console.log("cur node = ");
        console.log(begin.name);
        console.log("edges are");
        console.log(begin.neighbors.map(node => node.name));
        unvisitedNodes.set(begin.name, true);
        if (begin.name === destination.name && !destination.character) { //assumes there can't be a chaacter in te desination
            console.log("town name match");
            console.log(begin.name);
            return true;
        }
        // const maybeDestination: INode[]= this.nodes.filter(node => {
        //     // console.log("-=-")
        //     // console.log(node)
        //     return !node.character && node.name === destination.name
        // })
        // console.log(maybeDestination)
        // if(maybeDestination.length) {
        //     console.log("is true")
        //     return true
        // }
        //adjacent towns without any characters
        return begin.neighbors
            .filter(node => !node.character && !unvisitedNodes.get(node.name))
            .reduce((acc, cur) => {
            return acc || this.planHelper(unvisitedNodes, cur, destination);
        }, false);
    }
    placePlayer(character, nodeName) {
        const maybeNode = this.nodes.find(node => {
            return node.name = nodeName;
        });
        if (maybeNode) {
            const maybeCharacterNode = this.nodes.find(node => {
                node.character ? node.character.name === character.name : false;
            });
            if (maybeCharacterNode) {
                maybeCharacterNode.deleteCharacter();
            }
            maybeNode.placePlayer(character);
        }
    }
}
exports.Network = Network;
