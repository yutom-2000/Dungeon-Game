package Game.gameState;

import Game.gameState.interaction.*;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.Level;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.Entity;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.Player;
import Game.gameState.level.generation.Room;
import Game.gameState.posn.Posn;

/**
 * The class of GameState to track the game process.
 *
 * GAMESTATE CREATION ORDER:
 *    1. Take in already-created level
 *    2. Place players/adversaries on top of the level
 *    3. Fill in state-tracking vars (i.e. isPlayerTurn) and modify level accordingly
 *
 *    Primary focus of gamestate is Entities and interactions on top of the level.
 *    Player movement is conducted in GameState.
 *    Interactions are performed and created in GameState.
 *
 *    GAMESTATE DOES NOT CHECK VALIDITY OF MOVES/INTERACTIONS
 */
public class GameState implements InteractionAccept {

  private Level level;
  private Player[] players;
  private Adversary[] adversaries;
  boolean isLocked;
  boolean isExit;

  public GameState() {
  }

  /**
   * Primary constructor for GameSate for the first time initialization of the Game.
   *
   * @param level a given level of initial game
   */
  public GameState(Level level) {
    this.level = level;
    players = new Player[]{};
    adversaries = new Adversary[]{};
  }

  /**
   * Initialize a gamestate
   * Used for starting game, so must follow initializing rules for placing entities and such
   * @param level the level for initializing the game
   * @param players array of players in the game
   * @param adversaries array of adversaries in the game
   * @return initialized gameState
   */
  public GameState initialize(Level level, Player[] players,
      Adversary[] adversaries) {
    //register the players/adversray in the top-left room in the order
    this.level = level;
    this.players = players;
    this.adversaries = adversaries;
    this.isExit = false;
    this.isLocked = true;
    // randomly place entities in placeable tiles instead of group them in a room
    for (int i = 0; i < players.length; i++) {
      placeEntityRandomly(players[i]);
      players[i].setKeyHolder(false); //make false for any that are true, a 'reset'
    }
    for (int i = 0; i < adversaries.length; i++) {
      placeEntityRandomly(adversaries[i]);
    }
    for (Player p : players) {
      p.setStatus(EntityStatus.ALIVE);
    }
    return this;
  }

  /**
   * Resume a game from the middle of a game
   * Do not have to follow initialization rules
   * @param level the level for initializing the game
   * @param players array of players in the game
   * @param adversaries array of adversaries in the game
   * @return the intermediate gameState.
   */
  public GameState intermediateGame(Level level, Player[] players, Adversary[] adversaries, boolean isLocked, boolean isExit) {
    this.level = level;
    this.players = players;
    this.adversaries = adversaries;

    for (Player p : players) {
      placeEntity(p, p.getPosn());
    }
    for (Adversary a : adversaries) {
      placeEntity(a, a.getPosn());
    }

    this.isLocked = isLocked;
    this.isExit = isExit;

    return this;
  }

  /**
   * Move the entity on to the given  tile
   *
   * @param entity the entity perform tiles.
   * @param origin the origin tile
   * @param dest the tile that the entity want to move on
   * @return the MoveResult function object
   */
  public MoveResult moveEntity(Entity entity, Posn origin, Posn dest) {
    Interaction interaction = level.tileAt(dest).getInteraction(entity);
    accept(interaction);
    level.accept(interaction);
    entity.accept(interaction);

    EntityStatus status = entity.getStatus();
    if (status == EntityStatus.ALIVE) {
      placeEntity(entity, dest);
    }
    else if (status == EntityStatus.EXITED) {
      placeEntity(entity, dest);
      level.tileAt(dest).removeEntity();
    }
    else if (status == EntityStatus.TELEPORT) {
      dest = level.getRandomInRoom();
      placeEntity(entity, dest);
      entity.setStatus(EntityStatus.ALIVE);
    }
    else if (status == EntityStatus.DEAD) {
      //do nothing
    }

    level.tileAt(origin).removeEntity();
    if (isLocked && interaction.getMoveResult() == MoveResult.EXIT) {
      return new NoInteraction().getMoveResult();
    }
    return interaction.getMoveResult();
  }

  /**
   * Return the player with given name
   * @param name the name of the adversary
   * @return player of the given name
   */
  public Player getPlayerByName(String name) {
    for (Player p : players) {
      if (p.getName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  /**
   * Return the adversary with given name
   * @param name the name of the adversary
   * @return adversary of the given name
   */
  public Adversary getAdversaryByName(String name) {
    for (Adversary a : adversaries) {
      if (a.getName().equals(name)) {
        return a;
      }
    }
    return null;
  }

  /**
   * find the limited view of objects positions in 5 squares
   *
   * @param p position of one tile
   * @return the Arrays of position in 5 squares
   */
  public Posn[] getObjectsIn5Square(Posn p) {
    Posn[] result = new Posn[0];
    for (int rowi = -2; rowi <= 2; rowi++) {
      for (int coli = -2; coli <= 2; coli++) {
        int row = p.getRow() + rowi, col = p.getCol() + coli;
        Posn posn = new Posn(row, col);
        if (level.tileAt(posn).getItem() != ItemType.NONE) {
          Posn.append(result, posn);
        }
      }
    }
    return result;
  }

  /**
   * find the limited view of Entity positions in 5 squares
   *
   * @param p position of one tile
   * @return the Arrays of position in 5 squares
   */
  public Posn[] getEntityIn5Square(Posn p) {
    Posn[] result = new Posn[0];
    for (int rowi = -2; rowi <= 2; rowi++) {
      for (int coli = -2; coli <= 2; coli++) {
        int row = p.getRow() + rowi, col = p.getCol() + coli;
        Posn posn = new Posn(row, col);
        if (level.tileAt(posn).getEntity() != null) {
          Posn.append(result, posn);
        }
      }
    }
    return result;
  }

  /**
   * Place an entity at a position
   * @param entity entity to place
   * @param dest destination position
   * @param isInitial whether the game is initial
   * @return whether it is successfully place the entity
   */
  public boolean placeEntity(Entity entity, Posn dest, boolean isInitial) {
    if (this.level.placeEntity(entity, dest, isInitial)) {
      entity.setPosn(dest);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Convenience method for placing an entity during a game
   *    - do not check for initial rules
   * @param entity entity to place
   * @param dest destination position
   * @return if the entity was placed
   */
  public boolean placeEntity(Entity entity, Posn dest) {
    return placeEntity(entity, dest, false);
  }

  /**
   * place entity in a random placeable tile
   * @param entity entity to place
   * @return if the entity was placed
   */
  public boolean placeEntityRandomly(Entity entity) {
    return placeEntity(entity, level.getRandomInRoom(), false);
  }

  /**
   * Return if this level has been exited
   *    If exited, means that once all players are either exited or removed, proceed to next level
   * @return if at least player has reached the unlocked exit
   */
  public boolean isExit() {
    return this.isExit;
  }

  /**
   * Try to place entities in a room
   * Used for initialization (top left for players, bottom right for adversaries)
   * @param room the room accept the entities
   * @param entityList list of entities need to be placed in the room.
   */
  public void placeEntityInRoom(Room room, Entity[] entityList) {
    if (room == null && entityList.length != 0) {
      throw new IllegalArgumentException("Can't place on null room");
    }
    Posn origin = room.getOrigin();
    int index = 0; //index for entity list
    for (int y = 0; y < room.getRows(); y++) {
      for (int x = 0; x < room.getColumns(); x++) {
        if (index == entityList.length) {
          return;
        }
        else {
          if (placeEntity(entityList[index], new Posn(origin.getRow() + y, origin.getCol() + x), true)) {
            index++;
          }
        }
      }
    }
  }

  /**
   * Call once a player has exited the level
   * Change isExit field to true
   */
  public void setIsExit(boolean isExit) {
    this.isExit = isExit;
  }

  /**
   * Place all players onto the next level
   * @param level next Level
   * @param players list of players to place
   */
  public void moveToNextLevel(Level level, Player[] players, Adversary[] adversaries) {
    this.level = level;
    this.placeEntityInRoom(this.level.getTopLeftRoom(), players);
    this.placeEntityInRoom(this.level.getBottomRightRoom(), adversaries);
  }

  /**
   * SHOULD NOT BE USED FOR ANYTHING BUT TESTS
   * In real use case, list of adversaries and players passed in from gameManager should be fairly stagnant (Won't expand partway through game)
   * @param entity
   */
  public void addEntity(Entity entity) {
    if (entity.isPlayer()) {
      Player newPlayer = (Player) entity;
      Player[] newPlayers = new Player[players.length + 1];
      for (int i = 0; i < players.length; i++) {
        newPlayers[i] = players[i];
      }
      newPlayers[newPlayers.length - 1] = newPlayer;
      this.players = newPlayers;
    }
    else {
      Adversary newAdversary = (Adversary) entity;
      Adversary[] newAdversaries = new Adversary[adversaries.length + 1];
      for (int i = 0; i < adversaries.length; i++) {
        newAdversaries[i] = adversaries[i];
      }
      newAdversaries[newAdversaries.length - 1] = newAdversary;
      this.adversaries = newAdversaries;
    }
  }

  /**
   * Getter for players
   * @return array of players
   */
  public Player[] getPlayers() {
    return this.players;
  }

  /**
   * Getter for adversaries
   * @return array of adversary
   */
  public Adversary[] getAdversaries() {
    return  this.adversaries;
  }

  /**
   * Get positions of the players
   * @return positions of the players
   */
  public Posn[] getPosnPlayer() {
    Posn[] result = new Posn[players.length];
    for (int i = 0; i < players.length; i++) {
      result[i] = players[i].getPosn();
    }
    return result;
  }


  /**
   * Get positions of adversaries
   * @return positions of the adversaries
   */
  public Posn[] getPosnAdversary() {
    Posn[] result = new Posn[players.length];
    for (int i = 0; i < players.length; i++) {
      result[i] = players[i].getPosn();
    }
    return result;
  }


  /**
   * @return positions of the level in GM.
   */
  public Level getLevel() {
    return this.level;
  }


  /**
   * @return current gameState.
   */
  public GameState getGameState() {
    return this;
  }

  /**
   * Unlocks the exit for the level.
   */
  public void unlockExit() {
    this.isLocked = false;
  }

  /**
   * Getter for isLocked field.
   * @return
   */
  public boolean getIsLocked() {
    return this.isLocked;
  }


  /**
   * Re-interact all players on their current position
   * Current use case is pretty much only for when the door is unlocked, all players check if they are on a tile
   *      with exit, and exit if they are (since could not exit while locked)
   */
  public void reInteract() {
  }


  @Override
  public void accept(Interaction interaction) {
    interaction.visitState(this);
  }
}

