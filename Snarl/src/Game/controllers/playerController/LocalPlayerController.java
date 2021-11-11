package Game.controllers.playerController;

import Game.controllers.Command.Command;
import Game.controllers.Command.CommandMove;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.entities.EntityStatus;
import Game.gameState.interaction.MoveResult;
import Game.gameState.posn.Posn;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.IOException;
import java.util.Scanner;

/**
 * A local player controller
 */
public class LocalPlayerController extends PlayerController {

  Update update;
  MoveResult result;
  boolean isRender;

  /**
   * Constructor of the PlayerController.
   * @param name the name of the player.
   */
  public LocalPlayerController(String name) {
    super(name);
  }

  /**
   * set the render status on LocalPlayerController
   * @param isRender whether the controller is render
   * @return the updated LocalPlayerController
   */
  public LocalPlayerController setRender(boolean isRender) {
    this.isRender = isRender;
    return this;
  }

  @Override
  public void receiveUpdate(Update update) {
    this.update = update;
    render();
  }

  /**
   * Print out the LocalPlayerController
   */
  private void render() {
    if (isRender) {
      System.out.println(name);
      System.out.println(update.getStatus());
      System.out.println(update.getPosition());
      System.out.println(update.getLevel());
      System.out.println(update.toString());
    }
  }

  @Override
  public Command promptMove() {
    System.out.println(name + " move:");
    Scanner scan = new Scanner(System.in);

    String s = scan.nextLine();
    try {
      Posn dest = TestParser.parsePosn(JsonParser.parseString(s).getAsJsonArray());
      return new CommandMove(name, dest, true);
    } catch (IOException e) {
      System.out.println(e);
    } catch (Exception e) { //catch misc json parsing exceptions
      System.out.println(e);
      return promptMove();
    }
    return null;
  }

  @Override
  public void notifyLoadLevel(JsonObject message) {

  }

  @Override
  public void notifyEndLevel(NotifyEndLevel notice) {

  }

  @Override
  public void notifyGameOver(PlayerController[] playerControllers) {
      if (update.getStatus() == EntityStatus.EXITED) {
          System.out.println(name + " has won!");
      } else {
          System.out.println(name + " has lost.");
      }
  }

  @Override
  public void receiveResult(MoveResult result) {
    this.result = result;
    System.out.println(result);
  }


}
