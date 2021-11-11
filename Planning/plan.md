# Data Design

We decided to use a Model-View-Controller design pattern to build out game. 
This conveniently splits the server and clients, so that clients will only send commands and receive information on how to display the model stored and handled by the server. 

#### Model

For our model we design a class called GameState. 
This class will hold all the information necessary to run a game, such as the Levels, a list of Players and Adversaries, and check for win/lose conditions.
This class will also generate all the information necessary to begin a game, such as the randomly generated levels.
Lastly, this class will also handle all the requests sent to it by the client, and then if the request is valid, call the appropriate method with appropriate arguments to change model.
The view will take in this class and display the appropriate data for the client that requests a view from the model.

#### Controller/View

For our controller, we have a lightweight client that simply sends requests and receives information from the server to display a view.
Therefore, the only information the controller needs to keep track of is an identifier for the character the player controls.

The view will simply of the tiles the player-controlled character can see.

#### Communication

Communication between client and server will occur over a TCP connection.

To ensure communication is successful, the client can only send requests. 
The server will determine if these requests are valid or not, and make changes as necessary.

In return, the client will receive information from the server representing some portion of the data from the model.
This will let the user see his player's position, the portions of the level determined to be visible to the player, and some information about their characters' status.

# Implementation Timeline

#### Model

The first piece we will begin to implement is the model. 
At this stage, levels are defined explicitly, and not yet randomly generated.
Here, we will ensure that player/adversary movement (manual, not computer controlled), and player/adversary interactions (such as with other players/adversaries, keys, and level exits) work as intended.
These interactions should propogate changes throughout the system, and ensure that the results of these actions act as intended.

#### Generation pt1

Here we will begin to implement random level generation. For our first part of generation, we will ensure that rooms are generated properly. 
To generate a room, we will ensure that we only generate rooms where all empty tiles are reachable from any other empty tile in the same room.
This means there will be no "islands", and that player's don't get trapped or have situations where keys/exits are unreachable.

Another feature available at room-generation is to create a room with guaranteed exits in certain directions.
For instance, generating a room with the paramter indicating there must be an exit on the left means that at least one tile along the column x=0 is a empty floor tile.

This stage can also include random placement of key and exit.

#### Generation pt2

At this stage we begin to implement hallways to connect two randomly sized rooms together. This will be a good check to make sure our maths our correct, and is a small scale test of our "Blueprint" generation idea. 

Here we can also begin to write some server code. Design the request format on server, and the parser for requests on the server.
Also write the server's response format for the client to interpret and display.

#### Client View

Now we will start to write more client code such as a graphical display for the client to display the data they receive from the server.

Here we can write some more development to the client, such as binding some commands to certain keys so that the user doesn't need to type out command strings.

#### Generation pt3

Write the final level of generation, supporting a 2d array of rooms in the blueprint.


#### Notes:

These levels of development are subject to change, as some things may take longer than expected, or to take extra time to ensure that each feature is thoroughly tested.



