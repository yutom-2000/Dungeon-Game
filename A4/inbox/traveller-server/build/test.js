"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const traveller_server_1 = require("./traveller-server");
const tom = new traveller_server_1.Character('Tom');
const pat = new traveller_server_1.Character('Pat');
const a = new traveller_server_1.Node('a', 'a');
const b = new traveller_server_1.Node('b', 'b');
const c = new traveller_server_1.Node('c', 'c');
const d = new traveller_server_1.Node('d', 'd');
const e = new traveller_server_1.Node('e', 'e');
a.addNeighbors(b);
a.addNeighbors(c);
b.addNeighbors(c);
const nodes = [a, b, c, d, e];
const net = new traveller_server_1.Network();
net.create(nodes);
a.placePlayer(tom);
console.log("---");
console.log(net.nodes.map(node => node.name));
const routable = net.planRoute(a, e);
console.log(routable);