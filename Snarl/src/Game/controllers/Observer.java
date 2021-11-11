package Game.controllers;

import Game.controllers.Command.Command;
import Game.controllers.Command.CommandRegisterObserver;
import Game.gameManager.Updates.Update;
import Game.gameState.ActorPosition;
import Game.gameState.ObjectPosition;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;

/**
 * Observer used to monitor/debug game
 */
public class Observer implements UpdateReceiver {

  String name;
  Update update;
  boolean isRender;

  /**
   * Constructor of the Observer class
   *
   * @param name name of the observer.
   */
  public Observer(String name) {
    this.name = name;
  }

  /**
   * Get the rendering observer.
   *
   * @param isRender whether the game is render.
   * @return the observer.
   */
  public Observer makeRender(boolean isRender) {
    this.isRender = isRender;
    return this;
  }

  /**
   * Get the name of the observer.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  @Override
  public Command commandRegister() {
    return new CommandRegisterObserver(name, this);
  }

  @Override
  public void receiveUpdate(Update update) {
    this.update = update;
    render(update, new Posn(0, 0));
  }

  private static void render(Update update, Posn playerPosn) {
    //create int[][] layout
    int[][] layout = update.getTileLayout();

    //create ActorPosition[] actorpositions
    ActorPosition[] actorPositions = update.actorPositionList;

    //create ObjectPosition[] objectPositions
    ObjectPosition[] objectPositions = update.objectPositionList;

    //print out the integer layout view of the gameState with all players and adversaries.
    String toPrint = Observer.layoutToString(layout, actorPositions,
        objectPositions, playerPosn);

    System.out.println(toPrint);
  }

  //some method to render given the tiles and positions of stuff
  private void render() {
    int[][] layout = update.getTileLayout(); //draw the actors and stuff on top
    //Tile[][] result = new Tile[layout.length][layout[0].length];
    String result = "";

    //create some 2d array of tiles
    //int rowIter = 0;
    for (int[] row : layout) {
      //int colIter = 0;
      for (int t : row) {
        switch (t) {
          case 0:
            result += new Tile(TileType.WALL).toString();
            break;
          case 1:
            result += new Tile(TileType.FLOOR).toString();
            break;
          case 2:
            result += new Tile(TileType.DOOR).toString();
        }
      }
      result += "\n";
    }
    System.out.println(result);
  }

  public static String layoutToString(int[][] layout, ActorPosition[] actorPositions,
      ObjectPosition[] itemPositions, Posn topLeft) {
    String result = "";
    int r = topLeft.getRow();
    int c = topLeft.getCol();
    //4 to encode key
    //5 to encode exit
    for (ObjectPosition p : itemPositions) {
      Posn posn = p.position;
      int toPlace = 0;
      switch (p.type) {
        case KEY:
          toPlace = 4;
          break;
        case EXIT:
          toPlace = 5;
      }
      layout[posn.getRow() - r][posn.getCol() - c] = toPlace;
    }
    for (ActorPosition p : actorPositions) { //player is 6, zombie is 7, ghost is 8
      Posn posn = p.position;
      int toPlace = 0;
      switch (p.type) {
        case PLAYER:
          toPlace = 6;
          break;
        case ZOMBIE:
          toPlace = 7;
          break;
        case GHOST:
          toPlace = 8;
      }
      layout[posn.getRow() - r][posn.getCol() - c] = toPlace;
    }
    for (int[] row : layout) {
      for (int t : row) {
        switch (t) {
          case 0:
            result += new Tile(TileType.WALL).toString();
            break;
          case 1:
            result += new Tile(TileType.FLOOR).toString();
            break;
          case 2:
            result += new Tile(TileType.DOOR).toString();
            break;
          case 4:
            result += ItemType.KEY.toString();
            break;
          case 5:
            result += ItemType.EXIT.toString();
            break;
          case 6:
            result += "P";
            break;
          case 7:
            result += "Z";
            break;
          case 8:
            result += "G";
            break;
        }
      }
      result += "\n";
    }
    return result;
  }
}
