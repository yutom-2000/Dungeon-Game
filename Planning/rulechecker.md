# Rule Checker
We'll have IRuleChecker interface to validate the movements and interactions from players and adversaries, determine the end of game, and also reject invalid game states. It can be extended to handle different kinds of adversaries by adding methods and handle different game rules by adding classes.
The rule checker class will be a delegate in GameState to provide methods for validation. When a new GameState is created, a specific rule checker class will be provided based on the type of GameState. This feature allow user to select different modes for game.

#### The movement rules for players:
- A player can move to any traversable tile up to 2 cardinal moves away from themselves.

#### The interaction rules for players:
- A player can interact with keys and objects and adversaries
- A player cannot interact wiht other players (A tile can hold up to one player at a time).
- When the player moves, they interact with the object on the tile they choose and no other tile.

#### The rules for game state:
- All level are valid.
- Player Array and Adversary Array are not empty
- All players position must not in WallTile (Adversaries are determined by their types)
- A game is win if all levels are ended and is loss if all players are expelled.

#### The rules for Levels:
- Rooms and Hallways cannot overlap.
- Every Level has an Exit and a Key.
- Key's position is reachable.
- A Level is ended when a player reach the exit.

### Method Wishlist
- Boolean isValidGame (Level level, ArrayList<Player> players, ArrayList<Adversary> adversary) : determine if the GameState is valid.

- Boolean isPlayerValidMove (Level level, Player player, Posn dest) : determine if an player can move to the destination.

- Boolean isAdversaryValidMove (Level level, Aversary adversary, Posn dest) : determine if an adversary can move to the destination base on its type.

- Boolean isValidInteraction (Entity entity1, Entity entity2) : check if the interaction between two entities are allowed.

- Boolean isValidInteraction (Entity entity, Item item) : check if the entity can interact with an item.

- Boolean isWin (int exitedLevels, int totalLevels) : determine if players have exited all levels and win the game.

- Boolean isLoss (ArrayList<Player> players) : determine if game is loss by checking if all Players are expelled.
