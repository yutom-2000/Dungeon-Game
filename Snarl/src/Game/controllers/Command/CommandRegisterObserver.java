package Game.controllers.Command;

import Game.controllers.Observer;
import Game.gameManager.GameManager;
import Game.ruleChecker.RuleChecker;

/**
 * A command to register an observer
 */
public class CommandRegisterObserver extends Command {

  Observer observer;

  /**
   * Constructors for CommandRegisterObserver
   *
   * @param name     the name of the observer
   * @param observer the observer used for registering the command
   */
  public CommandRegisterObserver(String name, Observer observer) {
    super(name);
    this.observer = observer;
  }

  @Override
  public void visitGameManager(GameManager manager) {
    manager.addObserver(observer);
  }

  @Override
  public void visitRuleChecker(RuleChecker ruleChecker) {
    makeValid(); //this command should always be valid
  }

  @Override
  public void invalidHandle(GameManager manager) {
    System.out.println("Invalid Register Observer");
    //do nothing
  }
}
