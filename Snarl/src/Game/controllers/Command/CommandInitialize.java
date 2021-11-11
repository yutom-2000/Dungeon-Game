package Game.controllers.Command;

import Game.gameManager.GameManager;

import Game.gameState.level.Level;
import Game.ruleChecker.RuleChecker;

/**
 * A command to initialize a game
 * <p>
 * Gamemanager's gamestate should initialize at this point
 */
public class CommandInitialize extends Command {

  Level[] levels;

  /**
   * Constructors for initialized Command.
   *
   * @param playerName the name of the player
   * @param levels     arrays of the levels
   */
  public CommandInitialize(String playerName, Level[] levels) {
    super(
        playerName); // no gamestate to include in this command because gamestate is created *after* this
    this.levels = levels;
  }

  @Override
  public void visitGameManager(GameManager manager) {
    //todo add setters for numplayers/adversaries/level
    manager.getGameState().initialize(this.levels[0], manager.getPlayers(),
        manager.getAdversaries());
    //initialize mutates the rulechecker only
  }

  @Override
  public void visitRuleChecker(RuleChecker ruleChecker) {
    if (ruleChecker.getLevels() == null) {
      ruleChecker.initialize(levels);
      this.makeValid();
    } else {
      //do nothing, invalid command. A ruleschecker with already populated levels field is sign game has been init
    }
  }

  @Override
  public void invalidHandle(GameManager manager) {
    System.out.println("Unable to initialize");
    System.exit(-1);
  }

}
