# Traveller


The goal is to create the interface for a module dubbed Traveller.  For this assignment, this is to be completed in Python 3.9.1.

The module must support the following operations:
  - the creation of a town network with named nodes;
  - the placement of a named character in a town; and
  - a query whether a specified character can reach a designated town without running into any other characters.

### Classes


### Methods

Below are the methods that will need to be implemented to create the Traveller module.

| Modifier and Type | Method | Description |
| ------ | ------ | ------ |
| boolean | addTown(Town t) | Adds the specified town if not already present. |
| boolean | removeTown(Town t) | Removes the specified town if already exists. |
| boolean | isTownEmpty(Town t) | Returns True if the town is currently inhabitated. |
| boolean | doesTownExist(Town t) | Returns True if the town exists. |
| boolean | addRoad(Town t1, Town t2) | Adds the specified road if not already present. |
| boolean | removeRoad(Town t1, Town t2) | Removes the specified road if already present. |
| boolean | doesRoadExist(Towm t1, Town t2) | Returns True if the road currently exists. |
| boolean | addCharacter(Character c, Town t) | Adds the specified Character if town is currently empty. |
| boolean | removeCharacter(Character c) | Removes the specified Character if it already exists. |
| boolean | doesCharacterExist(Towm t1, Town t2) | Returns True if the character currently exists. |
| boolean | canReachTown(Character c, Town t) | Returns True if the character can reach the specified town without running into other characters. |


