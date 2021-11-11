# Snarl Game

**GameState**
> a class that keeps track of the state of the game, connects with all AIs and Players and updates the game.

**fields**
| Type| Fields  | Description | 
| ------ | ------ | ------ |
| Level | level | a single level for the game state | 
|Player[]| players| represents all the players in the game|
|Adversary[]|adversaries|represents all the adversaires in the game|
| Posn[] | playerPosn | the array of players' position|
| Posn[]|adversaryPosn| the array of adversaries' position|
| boolean|isPlayerTurn| determine whether this is player turn|
|boolean| isExit| determine whether the current level is exit|

**Constructors**
 1) Primary constructor for GameSate for the first time initialization of the Game.
  GameState(Level level, ArrayList<Player> players, ArrayList<Adversary> adversaries)
  
2) Secondary constructor for intermediate GameSate for updating.
  GameState(ArrayList<Posn> playerPosn, ArrayList<Posn> adversaryPosn, boolean isExit) 


**Methods**


| Type | Method | Description |
| ------ | ------ | ------ |
|IGameState|initialize(Level levels, int players, int adversaries|initialize a game Returns a new Game.IGameState based on input parameters|
| boolean | movePlayer (int id, Enum direction) | determine whether the player with given id can move to the new tile.| 
| Visitor | interact (Entity character, Tile tile) | interact with the object on the new tile.| 
| boolean | isExit() | determine whether the current level is exit|
|IGameState | getGameState()|return the current game state.|
| Posn | getPosn(int characterId) | get the position of the character according to its entity's id. |
|ArrayList<Player>| getPlayers()|Get the Players in the game.|
|ArrayList<Adversary> |getAdversaries()|Get the Adversaries in the game.|
| Level | getVisibleTiles(Entity entity) | generates the certain range of visible level to certain entity (the visibility of Player and Adversary are different) |


