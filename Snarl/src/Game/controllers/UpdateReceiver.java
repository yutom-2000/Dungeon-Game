package Game.controllers;

import Game.controllers.Command.Command;
import Game.gameManager.Updates.Update;

/**
 * Interface for all classes that will receive updates from GameMaanger
 */
public interface UpdateReceiver {

    /**
     * Register a controller with the manager, so that it will receive updates from the manager in the future
     * @return a CommandRegister for register with manager
     */
    Command commandRegister();

    /**
     * Receive an update representing information about the level (if entity, also information about the entity status, position)
     * @param update
     */
    void receiveUpdate(Update update);
}
