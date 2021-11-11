package Game.controllers;

import Game.controllers.Command.Command;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameState.interaction.MoveResult;
import Game.gameState.posn.Posn;
import com.google.gson.JsonObject;

/**
 * Indicates a controller for an entity in game Basically means must have ability to create move
 * commands
 */
public interface EntityController extends UpdateReceiver {

  /**
   * Create a command to move in a direction
   *
   * @param dest direction to move in
   * @return a command object to describe the movement
   */
  Command commandMove(Posn dest);

  /**
   * Prompt for a move command from the given controller For players this is determined by some
   * player input For adversaries this is determined by some computer algorithm.
   *
   * @return movement command from this controller.
   */
  Command promptMove();

  /**
   * Let this controller know that the level has ended
   *
   * @param notice
   */
  void notifyEndLevel(NotifyEndLevel notice);

  /**
   * Let this controller know the game is over If the player controlled is exited, they have won.
   * Otherwise they are dead and lost.
   * @param playerControllers
   */
  void notifyGameOver(PlayerController[] playerControllers);

  /**
   * Receive a result object (OK, EJECTED, EXITED, INVALID, etc)
   */
  void receiveResult(MoveResult reuslt);

  /**
   * Notify the controller that the level has changed
   *
   * @param message Json message parsing in through local client
   */
  void notifyLoadLevel(JsonObject message);
}
