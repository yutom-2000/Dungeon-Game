# Player component
A Player component represents the interests of the human behind the keyboard in the game. It receives updates from the Game Manager at appropriate moments. When it's the Player's turn, it needs to communicate the chosen action to the Game Manager.
A Player can only see 5x5 tiles given by Game Manager, and the Player can choose to move 0, 1, or 2 steps to a new reachable position. The Player component will send the information about movement to Game Manager and receive map with new visible tiles.

### Fields
- String | serverAddress: a server ip address that the player can use to connect to game server.
- String | name: a unique name provided by Player when registering.
- String | map: a map with visible tiles.
- Posn | position: the current position of the Player.
- int | id: a unique id for Player.
- Boolean | isPlayerTurn: check if it is player's turn.

### Constructor
- Player(String name, String address): create a Player with registered name and the ip address of server. It then connect to server and update fields by receiving information from server.

### Methods
- void | send(int id, Posn dest): send the Player id and the position of destination to Game Manager.
- void | update(): receive new visible tiles from Game Manager and update fields.
- void | render(): display the game.
- void | main(): create a text-based interface for a Player, it renders the game and allow the Player to input a number to move.