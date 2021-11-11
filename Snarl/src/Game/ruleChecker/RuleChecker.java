package Game.ruleChecker;

import java.util.ArrayList;

import Game.gameManager.CommandAcceptor;
import Game.gameManager.Updates.Update;
import Game.gameState.level.Level;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.Player;
import Game.gameState.level.tiles.*;
import Game.gameState.posn.Posn;

import Game.controllers.Command.Command;


/**
 * Checks to determine if a move supplied by a player is valid 1. Check if it is players' turn 2.
 * Check if the player still has moves left 3. return isSteppable method from Level
 * <p>
 * Checks to determine if a game has been won
 */
public class RuleChecker implements CommandAcceptor {

  int numLevels;
  int numPlayers;
  int numAdversaries;

  Level[] levels;
  String[] playerNames = new String[0];
  String[] adversaryNames = new String[0];

  /**
   * Primary constructors of the RuleChecker
   */
  public RuleChecker() {
  }

  /**
   * Constructors of the RuleChecker
   *
   * @param numPlayers the number of the players in the game.
   */
  public RuleChecker(int numPlayers) {
    this.numPlayers = numPlayers;
  }

  /**
   * Get the number of levels in GameState
   *
   * @return the counts of levels.
   */
  public int getNumLevels() {
    return numLevels;
  }

  /**
   * Assign the number of levels in RuleChecker
   */
  public void setNumLevels(int numLevels) {
    this.numLevels = numLevels;
  }

  /**
   * Get the number of players in GameState
   *
   * @return the counts of players.
   */
  public int getNumPlayers() {
    return numPlayers;
  }

  /**
   * Get the number of adversaries in GameState
   *
   * @return the counts of adversaries.
   */
  public int getNumAdversaries() {
    return numAdversaries;
  }

  /**
   * Assign the level information in RuleChecker
   */
  public void setLevels(Level[] levels) {
    this.levels = levels;
  }

  /**
   * Get all players' names in the game.
   *
   * @return the arrays of players' names.
   */
  public String[] getPlayerNames() {
    return playerNames;
  }

  /**
   * Assign the player's information in RuleChecker
   */
  public void setPlayerNames(String[] playerNames) {
    this.playerNames = playerNames;
  }

  /**
   * Get all adversaries' names in the game.
   *
   * @return the arrays of adversaries' names.
   */
  public String[] getAdversaryNames() {
    return adversaryNames;
  }

  /**
   * Assign the adversary's information in RuleChecker
   */
  public void setAdversaryNames(String[] adversaryNames) {
    this.adversaryNames = adversaryNames;
  }


  public RuleChecker setNumAdversaries(int numAdversaries) {
    //use to verify that num registered
    this.numAdversaries = numAdversaries;
    return this;
  }

  public RuleChecker setNumPlayers(int numPlayers) {
    this.numPlayers = numPlayers;
    return this;
  }

  /**
   * Initialize a ruleChecker that has been created using noargs constructor
   *
   * @param levels levels of the game.
   */
  public void initialize(Level[] levels) {
    this.numLevels = levels.length;
    this.levels = levels;
  }


  /**
   * Get all levels
   *
   * @return arrays of levels in the game.
   */
  public Level[] getLevels() {
    return this.levels;
  }

  @Override
  public Update[] acceptCommand(Command command) {
    command.visitRuleChecker(this);
    return new Update[0];
  }

  /**
   * Determine if a name is in a name list
   *
   * @param name     name of the entity
   * @param isPlayer whether it is player
   * @return boolean represent whether the given name is unique
   */
  public boolean isUniqueName(String name, boolean isPlayer) {
    if (isPlayer) {
      for (String s : playerNames) {
        if (s.equals(name)) {
          return false;
        }
      }
    } else {
      for (String s : adversaryNames) {
        if (s.equals(name)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Expand each name list
   *
   * @param name     name given by command
   * @param isPlayer whether it is player
   */
  public void expandNameList(String name, boolean isPlayer) {
    if (isPlayer) {
      String[] update = new String[playerNames.length + 1];
      for (int i = 0; i < playerNames.length; i++) {
        update[i] = playerNames[i];
      }
      update[playerNames.length] = name;
      playerNames = update;
    } else {
      String[] update = new String[adversaryNames.length + 1];
      for (int i = 0; i < adversaryNames.length; i++) {
        update[i] = adversaryNames[i];
      }
      update[adversaryNames.length] = name;
      adversaryNames = update;
    }
  }

  /**
   * Flush the list of adversary names to allow for re-register upon new level
   */
  public void flushAdversaryNames() {
    this.adversaryNames = new String[0];
  }

}
