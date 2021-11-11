# Traveller

##Global Variable:
Network - acts as the representation of the graph

##Data Definitions:

###Node in Graph package
- Int key - a unique identifier for the Node 
- String name - the name of the Node
- Array[Node] neighbors - list of connected nodes 
- Character character - The character that occupies the node
- METHODS :
    - addNeighbors(Node node) - add other node to this node’s neighbors and vice versa
    - Boolean findNode(Node node) - find whether given node is the neighbor of the current node
    - placePlayer(Character c) - place character in this node


###Networks in Graph package
- Array List of Nodes - represents the network of the town
- METHODS :
    - create(Array[Nodes]) - create the town network with given nodes
    - planRoute(Node begin, Node destination) - find possible route between the beginning and designated town without any character replacement.
    - placePlayer(Character c, String nodeName) - Put a character in named node

###Character 

- String name - the name of the player



##Process:
- <1> Initialize the game by creating the Nodes first, then passing the list of nodes to the Network class. The Nodes hold adjacency and name information for pathfinding.
- <2> To place player in named town:
    - First create the network
    - Call placePlayer() with given character and destination node
- <3> To query whether a specified character can reach a designated town without running into any other characters: 
    - Basic approach of using modified Dijkstra’s algorithm
    - 1) Check to see if the discovered node is occupied already. If occupied, do not continue to search from that node.
    - 2) Otherwise continue with Dijkstra’s to find a shortest path

