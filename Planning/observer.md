# Observer component
An Observer component allows a user to view the game in progress without changing game state. The whole map is visible to observers, and the observers can see all information of players, adversaries, and items.

### Fields
- String | name: a unique name provided by Observer when registering.
- int | id: a unique id for Observer.
- GameState | gameState: a gameState of current game.

### Methods
- void | update(GameState gameState): receive game state from Game Manager and update observer field.
- void | main(): display all information about game, which allows the observer to view the game in progress.


### UI mock up
Player 1: Alice
Position: (4 , 2)

Player 2: Bob
Position: (3, 5)

Adversary1 type: Zombie
Position: (4 , 3)

Adversary2 type: Ghost
Position: (2 , 5)

Key ID: 3
Position: (2 , 1)

Exit position: (2 , 7)
Status: Locked

■■■■■■■■  
■K▢▢G▢E■  
■▢▢▢▢P■■  
■PZ▢■▢▢■  
■d■■■■■■  
