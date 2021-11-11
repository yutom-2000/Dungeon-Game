## Traveller Integration Report


The other team did a good job of implementing our specification. They followed our data definitions very well, and implemented the methods we had described. They also correctly placed their definitions within the packages we specified. Most of the deviances from our exact specifications were not their fault. For instance, we neglected to specify a language they should implement in, and as a result they implemented our specification in javascript, whereas we envisioned the implementation being done in java.

There were some slight deviations between their implementations and our specification, but they were justified. For instance, we had some conflicting type declarations in our specification, so they chose one and stuck with it. We also neglected to include type signatures for our functions, so they added some type signatures that made sense. The most significant change they made was changing the function planRoute to take an additional character argument so that the application could query whether or not a specific character was able to find a free route between two nodes. They also implemented a different algorithm to find a path than was outlined in our specification, however their implementation made more sense since our specification of using Dijkstra's algorithm was nonsensical in this situation where the edges are unweighted. They documented all of these changes in their implementation.md that they included with their code, citing the reasoning behind all of the changes they decided to make. 

We would not be able to integrate the received implementation with the code we wrote in task 3 of warm up 3 because we failed to specify the language. As a result, their implementation is in javascript which would not be easily compatible with our java code. To do so would require us to use a foreign function interface or some similar mechanism, which neither of us have experience using. Either our code from warm up 3 would have to be re-written, or we would have to ask the other group to re-implement our specification in our language. Most of the reason why our code was not able to be used in conjunction was due to things we neglected to specify in our specification. With a clearer understanding of the necessary components to writing a quality specification, we believe we would be able to write a new specification that if implemented would likely make merging both code bases fairly straightforward.

There are many things we could do to improve our specification. We should first and foremost specify the language the artifact should be implemented in. This would eliminate the issue for us to integrate our code with theirs, and was a critical flaw in our specification. Secondly, we should consider the function signatures more carefully. By not specifying the return types of the functions we outlined in our specification, we run the risk of the implementation not matching up with the code we write. By specifying return type we would ensure that our code would be able to sync up as well. Lastly, we should pay more attention to the needs of the design. For instance, not specifying that planRoute should include a character argument basically makes finding a path for a specific character impossible. Furthermore, upon reflection, a method we could use to ensure any future specifications we write would be more complete and logical is to draw some sort of class diagram and interaction diagram so we could organize our thoughts as we decided how to organize our program before writing the specification. Thinking purely abstractly and communicating only verbally, without laying down our ideas on paper to keep track is an easy way to get confused and leads to an unclear, contradictory, and potentially confusing specification.


























