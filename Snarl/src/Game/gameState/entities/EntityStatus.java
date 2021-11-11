package Game.gameState.entities;

/**
 * Represent an entity's status Alive: still in level and able to move around and interact Dead:
 * removed by interaction with adversary Exited: removed by interaction with exit
 */
public enum EntityStatus {
  ALIVE, DEAD, EXITED, TELEPORT
}
