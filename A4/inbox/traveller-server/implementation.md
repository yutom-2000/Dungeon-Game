## Interpretations of Spec

The purpose of this document is to list the problems we encountered while providing an implementation.
And to propose possible solutions.

One of the first problems we encountered when implementing the spec. was with character placement.
Certain scenarios were unclarified in the specification and required judgement calls.
Since the specification implies a single character per node (a single character property on the node object)
a question was raised of what to do when placing one character in an already occupied node.
Possible resolutions were erroring out, or overriding the older character. We chose the latter.
Another scenario to consider is if a character already was placed in another node in the network.
Should the character move? Or be duplicated? We chose to move the character.
Redundant functionality to place characters was found in both the Node and Network interfaces.
It was unclear if the behavior was supposed to be different (would the INetwork be able to recognize
a character had to be move, and an INode would be dumb?). We chose the proposed smart / dumb split. 

Another issue we encountered was the inconsistency of type signatures.
For example, the Networks in Graph package interface can place a character with a Character object and String index for a node.
Whereas all the other methods use the Node interface to reference a node in the network e.g planning a route.

Another issue we encountered was the lack of completed type signatures for functions.
For example, the only method to provide a return type (Boolean) is findNode.
All other methods we had to interpret the return type from the description and assignment text.
One example of such a choice was returning a boolean from planRoute (though the description could perhaps lend itself to returning a path).

In a similar way, we found the specification of functions lacking for error checking.
For example, what happens when you try to add a node as a neighbor to another several times?

Another issue we encountered was the mixing of specification and implementation details.
The most prominent example was the supplying of class members (for example, providing an array of neighbors in a node, and it's unique key).
Additionally, the exact implementation of planRoute was provided in the spec.
It called for Dijkstra's algorithm, although this is an unweighted graph.
We ignored this suggestion. 

Finally, we found the specification failed to deliver on details from the assignment.
Specifically, planRoute clearly is intended to provide the wanted functionality of 
 querying "whether a *specified* character can reach a designated [...]" but the function
lacks a parameter for a specified character. We made a stretched interpretation that the
specified character was intended to occupy the begin node.