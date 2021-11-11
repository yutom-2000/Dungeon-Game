package Game.gameState.entities;

public enum EntityType {
  PLAYER, ZOMBIE, GHOST;

  @Override
  public String toString() {
    switch (this) {
      case PLAYER:
        return "player";
      case ZOMBIE:
        return "zombie";
      case GHOST:
        return "ghost";
      default:
        return "";
    }
  }
}

