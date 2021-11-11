For our design of Adversray.md we plan to have it implement the same interface as our player controller.

Adversaries can move by issuing move commands and send to manager to validate their validity.
public Command commandMove(Direction d);

Adversaries can receive updates from the manager. These updates will contain information from the entire level, no subsetting.
public void receiveUpdate(Update update);
In this way, the adversaries will receive updates from the manager to be able to decide how they will move next.

Since the adversary is computer controlled, it will also have a command that will ask it to produce a move when prompted.
public Command promptMove(); 
I envision this method as keeping some decision making logic and end result is calling the commandMove.

With these three basic commands, we can implement all the logic necessary for computer controlled adversaries. They can hold their own logics as to how to determine how to make a next move.
