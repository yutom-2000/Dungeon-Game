package Game.controllers.playerController;

import Game.controllers.EntityController;
import Game.controllers.Command.Command;
import Game.controllers.Command.CommandInitialize;
import Game.controllers.Command.CommandMove;
import Game.controllers.Command.CommandRegisterEntity;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.entities.EntityStatus;
import Game.gameState.interaction.MoveResult;
import Game.gameState.level.Level;
import Game.gameState.posn.Posn;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.IOException;
import java.util.Scanner;

/**
 * A Player's controller Creates commands to send to the GameManager
 * <p>
 * Holds 1. Name (unique identifier for the controller) 2. Update (the current status of the level
 * given by manager)
 */
public abstract class PlayerController implements EntityController {

  String name;
  public int numTimesKey = 0;
  public int numTimesExit = 0;
  public int numTimesDie = 0;

  /**
   * Constructor of the PlayerController.
   *
   * @param name the name of the player.
   */
  public PlayerController(String name) {
    this.name = name;
  }

  /**
   * Get the name of this player
   *
   * @return the name.
   */
  public String getName() {
    return this.name;
  }

  @Override
  public Command commandRegister() {
    return new CommandRegisterEntity(name, this, true);
  }

  @Override
  public abstract void receiveUpdate(Update update);

  @Override
  public Command commandMove(Posn dest) {
    return new CommandMove(name, dest, true);
  }

  @Override
  public abstract Command promptMove();

  @Override
  public abstract void notifyLoadLevel(JsonObject message);

  @Override
  public abstract void receiveResult(MoveResult result);


}
