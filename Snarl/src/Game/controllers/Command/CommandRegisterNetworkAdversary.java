package Game.controllers.Command;

import Game.controllers.EntityController;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.GameManager;
import Game.gameState.entities.*;
import Game.ruleChecker.RuleChecker;

/**
 * Special command for register a remote adversary
 */
public class CommandRegisterNetworkAdversary extends CommandRegisterEntity {

  EntityType type;

  /**
   * Constructor for the networkAdversary registration command
   *
   * @param name       the name of the NetworkAdversary
   * @param controller the controller need to be generated
   * @param isPlayer   whether it is a player
   * @param type       the type of the adversaries (Zombie or Ghost)
   */
  public CommandRegisterNetworkAdversary(String name, EntityController controller, boolean isPlayer,
      EntityType type) {
    super(name, controller, isPlayer);
    this.type = type;
  }

  @Override
  public void visitGameManager(GameManager manager) {
    if (isPlayer) {
      throw new IllegalArgumentException("how did this happen, should always be false");
    }
    Adversary adversary = null;
    switch (type) {
      case PLAYER:
        throw new IllegalArgumentException("should be adversary");
      case GHOST:
        adversary = new Ghost(name);
        break;
      case ZOMBIE:
        adversary = new Zombie(name);
    }
    manager.addAdversary(adversary);
    AdversaryController ac = (AdversaryController) controller;
    manager.addAdversaryController(ac); //this array is accessed in prompting moves i think
    manager.addRemoteAdversaryController(ac);
  }

  @Override
  public void visitRuleChecker(RuleChecker ruleChecker) {
    if (!isPlayer) {
      if (ruleChecker.getNumAdversaries() == ruleChecker.getAdversaryNames().length) {
        return; //already full
      }
    }
    if (ruleChecker.isUniqueName(name, isPlayer)) {
      ruleChecker.expandNameList(name, isPlayer);
      makeValid();
    } else {
      return; //not valid, do not expand the list
    }
  }

}
