package Game.gameManager;

import Game.controllers.Command.Command;
import Game.gameManager.Updates.Update;


/**
 * Interface for accepting command objects
 */
public interface CommandAcceptor {
    Update[] acceptCommand(Command command);
}
