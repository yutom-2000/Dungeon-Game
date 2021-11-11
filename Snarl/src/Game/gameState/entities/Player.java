package Game.gameState.entities;

import Game.gameState.interaction.Interaction;
import Game.gameState.level.Level;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.*;
import java.util.ArrayList;

public class Player extends Players {

  private int MAXMOVE = 2;
  private boolean keyHolder;

  public Player(String name, EntityType type, Posn position, EntityStatus status) {
    super(name, type, position, status);
  }

  /**
   * Primary constructor for the Player's registration
   *
   * @param name name of the player
   */
  public Player(String name) {
    super(name, EntityType.PLAYER, null, EntityStatus.ALIVE);
  }

  public Player(String name, Posn p) {
    super(name, EntityType.PLAYER, null, EntityStatus.ALIVE);
  }

  public boolean getKeyHolder() {
    return this.keyHolder;
  }

  public void setKeyHolder(Boolean val) {
    keyHolder = val;
  }

  @Override
  public int getMaxMoves() {
    return MAXMOVE;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Player) {
      Player other = (Player) o;
      return other.name.equals(this.name);
    } else {
      return false;
    }
  }

//  @Override
//  public String supervise(Posn currentPosn, Level level) {
//    int col = currentPosn.getCol();
//    int row = currentPosn.getRow();
//    Tile[][] tiles = level.getTiles();
//    int rowLength = tiles.length;
//    int colLength = tiles[0].length;
//    ArrayList<Posn> listPosn = fogWall(col, row, colLength, rowLength);
//    return view(col, row, tiles, listPosn);
//  }

  private ArrayList<Posn> fogWall(int col, int row, int colLength, int rowLength) {
    ArrayList<Posn> listPosn = new ArrayList<>();
    int rowAbove = row - 1;
    int rowBelow = row + 1;
    int colLeft = col - 1;
    int colRight = col + 1;
    for (int i = rowAbove; i < rowLength; i++) {
      for (int j = colLeft; j < colLength; j++) {
        if (i >= row + 2 || j >= col + 2) {
          break;
        }
        if (i >= 0 && j >= 0) {
          Posn p = new Posn(i, j);
          listPosn.add(p);
        }
      }
    }
    return listPosn;
  }

//  @Override
//  public String view(int PlayerCol, int PlayerRow, Tile[][] tiles, ArrayList<Posn> listPosn) {
//    String surroundings = "";
//    String singleTile = "";
//    int previouseRow = -1;
//    for (int i = 0; i < listPosn.size(); i++) {
//      Posn position = listPosn.get(i);
//      int col = position.getCol();
//      int row = position.getRow();
//      // initialize the row
//      if (previouseRow < 0) {
//        previouseRow = row;
//      }
//      if (col == PlayerCol && row == PlayerRow) {
//        singleTile = "*";
//      } else {
//        Tile tile = tiles[row][col];
//        singleTile = tiles[row][col].toString();
//      }
//      if (row != previouseRow) {
//        surroundings = surroundings.concat("\n");
//        surroundings = surroundings.concat(singleTile);
//        //update the row with the new row
//        previouseRow = row;
//        //the other way, using playerCol - 1 + 3 = current column, then move to new line
//      } else {
//        surroundings = surroundings.concat(singleTile);
//      }
//    }
//    surroundings = surroundings.concat("\nNotes: * represents your current position.");
//    System.out.println(surroundings);
//    return surroundings;
//  }

}