import { Node, INode, Network, INetwork, ICharacter,Character } from './traveller-server'
const tom: ICharacter = new Character('Tom')
const pat: ICharacter = new Character('Pat')


const a: INode = new Node('a', 'a')
const b: INode = new Node('b', 'b')
const c: INode = new Node('c', 'c')
const d: INode = new Node('d', 'd')
const e: INode = new Node('e', 'e')

a.addNeighbors(b)
a.addNeighbors(c)
b.addNeighbors(c)

const nodes = [a, b, c, d, e]

const net: INetwork = new Network()

net.create(nodes)

a.placePlayer(tom)

console.log("---")
console.log(net.nodes.map(node => node.name))
const routable: boolean = net.planRoute(a, e)

console.log(routable)