# Snarl Game

**GameManager**
> a class that take in the game state, connects the game with all AIs and Players and requests any change occur in the game and player.
> Accepting players to the game and start a game with a single level, which will be provided. Players should provide a unique name when registering.

**fields**
| Type| Fields  | Description | 
| ------ | ------ | ------ |
| Level | level | a single level for the game state | 
| IGamestate | gametstate | the game state field to track the game| 


**Constructors**
 1) Primary constructor for GameSate for the first time initialization of the Game.
  GameState(Level level, IGamestate gamestate);

**Local Variables**
| Type| Fields  | Description | 
| ------ | ------ | ------ |
|Player[]| players| represents all the players in the game|
|Adversary[]|adversaries|represents all the adversaires in the game|
|RuleChecker | ruleChecker| reference to the checker component|

**Methods**


| Type | Method | Description |
| ------ | ------ | ------ |
|void|registerPlayer(String name)|Register between 1 and 4 players. The order of registration determines the order in which they take turns.|
| void | registerAdversary(int num)| Register an arbitrary number of adversaries.| 
| IGameState | startGame()| Start the game according to the registering information of player and adversaries.| 
| void | updatePlayerPosn() | update the player position if they moved in the game. |
| void | movePlayer(int id, Direction direction)| suppose we know who's trun is, we request the player's move|
|void | interact(Entity entity,  Tile tile)|   * Method to interact player character with the object at the given tile in the Game.level possible outcomes: Player: 1) get key --> open the exit --> move to next level. b) be killed by adversary. c) empty, nothing happened Adversary: 1) get key --> nothing happened 2) met player --> kill the player --> change player status.|
|IGameState | superviseGame()| Supervise a SNARL game. (assume the game only have one level so far) todo given the level, then supervised the given level game state|
| ArrayList<Player> | getPlayers() | Get the Players in the game. |
|ArrayList<Adversary> | getAdversaries()| Get the Adversaries in the game.|


