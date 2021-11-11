package Game;

import Game.controllers.Command.CommandInitialize;
import Game.controllers.Command.CommandRegisterObserver;
import Game.controllers.Observer;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.adversaryController.LocalAdversaryController;
import Game.controllers.playerController.LocalPlayerController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.GameManager;
import Game.gameManager.LocalGameManager;
import Game.gameState.entities.EntityType;
import Game.gameState.level.Level;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.sourceforge.argparse4j.impl.Arguments;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Class to run the game
 */
public class LocalGame {

  public static void main(String[] args) throws IOException {
    String levelFile = "Snarl.levels";
    int numPlayers = 1;
    boolean observe = false;

    ArgumentParser parser = ArgumentParsers.newFor("").build().defaultHelp(true);
    parser.addArgument("--levels");
    parser.addArgument("--players");
    parser.addArgument("--observe").action(Arguments.storeTrue());

    try {
      Namespace ns = parser.parseArgs(args);
      observe = ns.getBoolean("observe");
      if (ns.get("levels") != null) {
        levelFile = ns.get("levels");
      }
      if (ns.get("players") != null) {
        numPlayers = Integer.valueOf(ns.get("players"));
      }
    } catch (ArgumentParserException e) {
      System.out.println(e);
      System.exit(-1);
    } catch (NumberFormatException e) {
      System.out.println(e);
      System.exit(-1);
    }

    //System.out.println(Paths.get(levelFile).toAbsolutePath());
    String levelsJsonString = String.join("\n", (Files.readAllLines(Paths.get(levelFile))));

    String[] levelJsonArray = levelsJsonString.split("\n\n");
    int currentLevel = 0;
    Level[] levels = new Level[levelJsonArray.length];
    for (int i = 0; i < levelJsonArray.length; i++) {
      String json = levelJsonArray[i];
      levels[i] = TestParser.parseLevel(JsonParser.parseString(json).getAsJsonObject());
    }

    GameManager gameManager = new LocalGameManager();
    for (int i = 0; i < numPlayers; i++) {
      gameManager.promptRegister();
    }

    int numZombies = (currentLevel / 2) + 1;
    int numGhosts = (currentLevel - 1) / 2;

    for (int i = 0; i < numZombies; i++) {
      gameManager.acceptCommand(
          new LocalAdversaryController("zombie" + i, EntityType.ZOMBIE).commandRegister());
    }
    for (int i = 0; i < numGhosts; i++) {
      gameManager.acceptCommand(
          new LocalAdversaryController("ghost" + i, EntityType.GHOST).commandRegister());
    }
    gameManager.setLevelNum(currentLevel);
    if (observe) {
      gameManager.acceptCommand(new CommandRegisterObserver("observer",
          new Observer("observer").makeRender(observe)));
    }
    gameManager.acceptCommand(new CommandInitialize("admin", levels));

    gameManager.notifyAllLoadLevel();
    while (!gameManager.gameOver()) {
      while (!gameManager.levelOver()) {
        gameManager.doMove();
      }
        if (gameManager.getGameState().isExit()) {
            gameManager.setLevelNum(gameManager.getLevelNum() + 1);
            if (gameManager.notLastLevel()) { //load gamestate for this level
                gameManager.notifyAllEndLevel();
                gameManager.loadNextLevel();
                gameManager.updatePlayers();
                gameManager.updateObservers();
                gameManager.updateAdversaries();
            } else {
                break; //game over, no more levels
            }
        } else {
            break; //you have lost
        }

    }
    gameManager.notifyAllGameOver(gameManager.getPlayerControllers());
  }
}
