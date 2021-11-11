package Game.controllers.adversaryController;

import Game.controllers.Command.Command;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.GameState;
import Game.gameState.direction.Direction;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.EntityType;
import Game.gameState.entities.Player;
import Game.gameState.interaction.MoveResult;
import Game.gameState.posn.Posn;
import com.google.gson.JsonObject;

/**
 * Local adversary controller
 */
public class LocalAdversaryController extends AdversaryController {

  Update update;

  /**
   * Constructor for the LOCAL controller of Adversary
   *
   * @param name name of the Adversary
   * @param type types of the Adversary
   */
  public LocalAdversaryController(String name, EntityType type) {
    super(name, type);
  }

  @Override
  public void receiveUpdate(Update update) {
    this.update = update;
  }

  @Override
  public Command promptMove() {
    System.out.println("Adversary " + name + " move");
    GameState gameState = update.getGameState();
    Posn adversaryPosition = update.getPosition();
    Adversary thisAdversary = (Adversary) gameState.getLevel().tileAt(adversaryPosition)
        .getEntity();
    Direction[] directions = new Direction[]{Direction.LEFT, Direction.DOWN, Direction.RIGHT,
        Direction.UP};
    Posn[] possibleMoves = new Posn[]{adversaryPosition.goDirection(Direction.LEFT),
        adversaryPosition.goDirection(Direction.DOWN),
        adversaryPosition.goDirection(Direction.RIGHT),
        adversaryPosition.goDirection(Direction.UP)
    };
    int[] distances = new int[4];
    for (int i = 0; i < possibleMoves.length; i++) {
      distances[i] = closestPlayerDistance(possibleMoves[i]);
    }

    int indexMin = 0;
    int min = Integer.MAX_VALUE;
    while (true) {
      for (int i = 0; i < distances.length; i++) {
        if (distances[i] < min) {
          min = distances[i];
          indexMin = i;
        }
      }
      Adversary subject = update.getGameState().getAdversaryByName(name);
      if (subject.canStep(gameState.getLevel().tileAt(possibleMoves[indexMin]))) {
        System.out.println(directions[indexMin]);
        return commandMove(possibleMoves[indexMin]);
      } else {
        distances[indexMin] = Integer.MAX_VALUE; //"pop" this value so it won't be selected next time
        min = Integer.MAX_VALUE;
      }

    }
  }


  //These methods don't do anything because localPlayer doesn't really need to be notified in case any
  // notice is sent out
  @Override
  public void notifyEndLevel(NotifyEndLevel notice) {
  }

  @Override
  public void notifyGameOver(PlayerController[] playerControllers) {

  }

  @Override
  public void receiveResult(MoveResult result) {

  }

  @Override
  public void notifyLoadLevel(JsonObject message) {

  }

  @Override
  public void notifyNotRegistered() {
    //do nothing, local is only computer controlled adversaries
  }

  /**
   * Return the closest distance between this pos and a player Used to implement basic computer
   * decision making for moves
   *
   * @param pos the position of the next move destination of the entity
   * @return the closest distance between this pos and a player
   */
  private int closestPlayerDistance(Posn pos) {
    GameState gameState = update.getGameState();
    int closest = Integer.MAX_VALUE;

    for (Player p : gameState.getPlayers()) {
      if (p.getStatus() == EntityStatus.ALIVE) {
        int distance = Posn.distance(pos, p.getPosn());
        if (closest > distance) {
          closest = distance;
        }
      }
    }
    return closest;
  }
}
