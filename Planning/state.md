**GameState** : a class that keeps track of the state of the game.

| Fields | levels | players | isPlayerTurn |
| ------ | ------ | ------ | ------ |
| Type | Level [] | Player [] | boolean |

- boolean | movePlayer (int id, Enum direction) : determine whether the player with given id can move to the new tile. 
- Visitor | interact (Entity character, Posn newPosn) : interact with the object on the new tile.
- Blueprint | generateBluePrint(int... roomSize, Array ExitDimention) : generate the blueprint of the room and used for generating the room in larger scale. 
- boolean | isOver() : determine whether the game is over (won/lost).
- Level | getVisibleTiles(Entity entity) : generates the map (the visibility of Player and Adversary are different).
- void | nextLevel() : move all player to the next level.

### Entity

> an interface of all characters in the game : Player or Adversary

**Player Class**    and **Adverrsary Class** implements Entity interface

| Fields | id | isAlive | position | moveCount |
| ------ | ------ | ------ | ------ | ------ |
| Type |int | boollean | Posn(row,col) | int |

- plusMoveCount() : move the current Entity to the new Posn.
- interact (Tile tile) : interacts with the objects at the tile. 


**Level** : a class represents the level of the game, it is a 2d arraylist composts by unit tiles

| Fields | roomNum | numCol | numRow | isexit | tiles | door |
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| Type | int | int | boolean | 2d-Arr | int | Tile |


- void | movePlayer (Entity id, Posn newPosn) : move the player to the new tile. 
- void | interact (Entity character, Posn newPosn) : interact with the object on the new tile. 
- void | place(Object object, Posn posn) : place the object on the tile. 
- void | removeEntity(Entity character, Posn posn) : remove the character at the given posn.

### Tile
> an interface represents the tile in the game.

| Type | Method | Description |
| ------ | ------ | ------ |
| boolean | isStepable () | determine whether the entity can move to the tile.| 
| void | place(Object object) | place the item/entity on the tile.|
| void | remove(Object object) | remove the item/entity at the given posn.|
| void | get() | return the item/Entity on the tile.|


**Floor** : a class represents the floor where the characters and item can be placed on to it.

| Fields | object | character | occupied |
| ------ | ------ | ------ | ------ | 
| Type | Placeable | Entity | boolean |

**ExitTile** (boolean isLocked) : a class represents the exit door in the game, implements Tile interface.
- boolean | isLocked () : determine whether the exit door is locked.
- Object | get () : get the exit door.

**Wall** : a class represents the wall in the game, implements Tile interface.

### Item
> an interface represents the objects can be placed on the tile in the game.

| Type | Method | Description |
| ------ | ------ | ------ |
| int | getID () | get the current item's id.| 
| Visitor | get () | return the visitor and do interacion.| 

**Key** (int id) : a class represents the key to open the exit door in the game, implements Item interface.

**Blueprint** : a class represents the blueprint of level for map generating system .

| Fields | rooms | design |
| ------ | ------ | ------ |
| Type | Room[] | Room |

**Room** : a class represents the room in the game.

| Fields | tiles | size | exitDoor |
| ------ | ------ | ------ | ------ | 
| Type | Tile[] | Size(x,y) | Enum |

- boolean | isStepable (GameState gs) : determine whether the entity can move to the tile.
- Size | getSize() : return the size of the current room.

### Visiter
> an interface represents the functional object in the game.
Using visitor pattern ensures that by broadcast every action, we can effect change upon all levels of the heirarchy.Visitor pattern also ensures we can freely add more operations in the future as well

Some methods include visitGameState (GameState gs), visitLevel (Level level), and visitEntity (Entity entity). And the corresponding method accept() in each class. 
