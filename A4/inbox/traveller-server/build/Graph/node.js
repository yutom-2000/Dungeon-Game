"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Node = void 0;
class Node {
    constructor(key, name) {
        this.key = key;
        this.name = name;
        this.neighbors = [];
    }
    addNeighbors(node) {
        const maybeNode = this.findNode(node);
        if (!maybeNode) {
            this.neighbors.push(node);
            node.addNeighbors(this);
        }
    }
    deleteCharacter() {
        this.character = undefined;
    }
    placePlayer(character) {
        this.character = character;
    }
    findNode(node) {
        return this.neighbors.reduce((acc, cur) => {
            return acc || cur.name === node.name;
        }, false);
    }
}
exports.Node = Node;
