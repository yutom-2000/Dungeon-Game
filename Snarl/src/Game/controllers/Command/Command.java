package Game.controllers.Command;

import Game.gameManager.GameManager;
import Game.gameState.GameState;
import Game.ruleChecker.RuleChecker;

/**
 * Represents a command a player controller gives the gameManager
 */
public abstract class Command {

  String name; //represents name of player that created command
  boolean isValid; //represent if this command is valid. Always initialize to false.
  GameState gameState;

  /**
   * Constructor of the Command objects.
   *
   * @param name the name of the player issuing the command
   */
  public Command(String name) {
    this.name = name;
    isValid = false;
  }

  /**
   * Return the name of the player issuing the command
   *
   * @return the string of the player's name.
   */
  String getSource() {
    return this.name;
  }

  /**
   * Get the validity of this command
   *
   * @return true if command is valid
   */
  public boolean getIsValid() {
    return this.isValid;
  }

  /**
   * Make this command a valid command
   */
  public void makeValid() {
    this.isValid = true;
  }

  /**
   * Add gameManager information to this command
   *
   * @param gameManager the gameManager provides the command.
   */
  public Command addGameState(GameState gameManager) {
    this.gameState = gameManager;
    return this;
  }

  /**
   * Visit the Manager Assumed to be valid because it is validity checked before visiting
   *
   * @param manager the gameManager provides the command.
   */
  public abstract void visitGameManager(GameManager manager);

  /**
   * Get the name of the command's provider
   *
   * @return the string of the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Visit the RuleChecker
   *
   * @param ruleChecker the ruleChecker for checking the valid of commands.
   */
  public abstract void visitRuleChecker(RuleChecker ruleChecker);

  /**
   * get the gameState of the Command.
   *
   * @return the gameState
   */
  public GameState getGameState() {
    return this.gameState;
  }

  /**
   * Do some handling if invalid
   */
  public abstract void invalidHandle(GameManager manager);
}
