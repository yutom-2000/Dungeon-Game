package Game.controllers.Command;

import Game.controllers.EntityController;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.GameManager;
import Game.gameState.GameState;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.Ghost;
import Game.gameState.entities.Player;
import Game.gameState.entities.Zombie;
import Game.ruleChecker.RuleChecker;

import java.io.IOException;

/**
 * Command to register a player
 */
public class CommandRegisterEntity extends Command {

  boolean isPlayer;
  EntityController controller;

  /**
   * Constructor for CommandRegisterEntity
   *
   * @param name       name of the entity
   * @param controller the corresponding controller
   * @param isPlayer   whether the registering object is player or adversary
   */
  public CommandRegisterEntity(String name, EntityController controller, boolean isPlayer) {
    super(name);
    this.isPlayer = isPlayer;
    this.controller = controller;
  }

  @Override
  public void visitGameManager(GameManager manager) {
    //creat the initialized entity abd generate the corresponding controllers
    if (isPlayer) {
      PlayerController pc = (PlayerController) controller;
      manager.addPlayerController(pc);
      manager.addPlayer(new Player(name));
    } else {
      String zombie = "Z", ghost = "G";
      Adversary adversary = null;
      if (name.length() > 6) {
        zombie = name.substring(0, 6);
      } else {
        ghost = name.substring(0, 5);
      }
      if (ghost.equals("ghost")) {
        adversary = new Ghost(name);
      } else if (zombie.equals("zombie")) {
        adversary = new Zombie(name);
      }
      manager.addAdversary(adversary);
      AdversaryController ac = (AdversaryController) controller;
      manager.addAdversaryController(ac);
    }
  }

  @Override
  public void visitRuleChecker(RuleChecker ruleChecker) {
    if (isPlayer) {
      if (ruleChecker.getNumPlayers() == ruleChecker.getPlayerNames().length) {
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

  @Override
  public void invalidHandle(GameManager manager) {
    try {
      manager.promptRegister(); //prompt to register again
    } catch (IOException e) {
      System.out.println(e);
      System.exit(-1);
    }
  }

  @Override
  public String toString() {
    return "Register Player " + name;
  }

}
