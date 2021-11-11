package Game.controllers.adversaryController;

import Game.controllers.Command.Command;
import Game.controllers.Command.CommandMove;
import Game.controllers.Command.CommandRegisterEntity;
import Game.controllers.EntityController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.entities.EntityType;
import Game.gameState.interaction.MoveResult;
import Game.gameState.posn.Posn;

/**
 * A controller for adversaries
 */
public abstract class AdversaryController implements EntityController {

  String name;
  EntityType type;

  /**
   * Constructor for the controller of Adversary
   *
   * @param name name of the Adversary
   * @param type types of the Adversary
   */
  public AdversaryController(String name, EntityType type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Get the name of this Adversary
   *
   * @return the name of the Adversary
   */
  public String getName() {
    return this.name;
  }


  @Override
  public Command commandRegister() {
    return new CommandRegisterEntity(name, this, false);
  }

  @Override
  public abstract void receiveUpdate(Update update);// {       this.update = update;   }

  @Override
  public Command commandMove(Posn dest) {
    return new CommandMove(name, dest, false);
  }

  @Override
  public abstract Command promptMove();

  public EntityType getType() {
    return this.type;
  }

  @Override
  public abstract void notifyEndLevel(NotifyEndLevel notice);

  @Override
  public abstract void notifyGameOver(PlayerController[] playerControllers);

  @Override
  public abstract void receiveResult(MoveResult result);

  /**
   * For adversaries
   *
   * @param name     adversary's name
   * @param dest     destination of movement
   * @param isPlayer if the given name is a player
   * @return the move command
   */
  public Command commandMove(String name, Posn dest, boolean isPlayer) {
    return new CommandMove(name, dest, isPlayer);
  }

  /**
   * Let this controller know it was not registered for this round, probably because ran out of
   * slots
   */
  public abstract void notifyNotRegistered();


}


