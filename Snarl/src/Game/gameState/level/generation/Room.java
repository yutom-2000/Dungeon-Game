package Game.gameState.level.generation;

import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;

/**
 * Class representing a room
 * Consumed in Game.gameState.level creation
 */
public class Room {
    Posn origin;
    int rows;
    int columns;
    Tile[][] tiles; //0 means wall, 1 means empty tile, 2 means door

    /**
     * Primary constructor for room
     * @param origin the orginal point of the room
     * @param rows number of rows of the room
     * @param columns number of columns of the room
     */
    public Room(Posn origin, int rows, int columns) {
        this.origin = origin;
        this.rows = rows;
        this.columns = columns;
        this.tiles = new Tile[rows][columns];
    }

    /**
     * Secondary constructor for room
     * @param origin the orginal point of the room
     * @param rows number of rows of the room
     * @param columns number of columns of the room
     * @param input the Tile[][] for establishing the tiles
     */
    public Room(Posn origin, int rows, int columns, Tile[][] input) {
        this.origin = origin;
        this.rows = rows;
        this.columns = columns;
        this.tiles = input;
    }

    /**
     * Third constructor for room
     * @param origin the orginal point of the room
     * @param input the Tile[][] for establishing the tiles
     */
    public Room(Posn origin, Tile[][] input) {
        this.origin = origin;
        this.rows = input.length;
        this.columns = input[0].length;
        this.tiles = input;
    }

    //Legacy constructor, should not really be used anymore
    public Room(Posn origin, int rows, int columns, Posn[] doors, Tile[][] input) {
        this.origin = origin;
        this.rows = rows;
        this.columns = columns;
        this.tiles = input;
        for (Posn p : doors) {
            try {
                if (this.tiles[p.getRow()][p.getCol()].isStepable()) {
                    this.tiles[p.getRow()][p.getCol()] = new Tile(TileType.DOOR);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Tried to place door out of bounds" + p.toString());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Room) {
            Room other = (Room) o;
            return this.origin.equals(other.origin)
                    && this.columns == other.columns
                    && this.rows == other.rows
                    && java.util.Arrays.deepToString(this.tiles).equals(java.util.Arrays.deepToString(other.tiles));
        } else {
            return false;
        }
    }

    /**
     * Determine if a point p is in this room
     *
     * @param p point
     * @return True if point is in a steppable tile in this room
     */
    public boolean inRoomSteppable(Posn p) {
        Posn adjustedP = new Posn(p.getRow() - this.origin.getRow(), p.getCol() - this.origin.getCol());

        if (adjustedP.getRow() < 0 || adjustedP.getCol() < 0) { //OOB
            return false;
        }
        if (adjustedP.getRow() >= this.tiles.length || adjustedP.getCol() >= this.tiles[0].length) { //OOB
            return false;
        }

        Tile tile = this.tiles[adjustedP.getRow()][adjustedP.getCol()];
        if (tile.isStepable()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int r = 0; r < this.tiles.length; r++) {
            for (int c = 0; c < this.tiles[0].length; c++) {
                result += tiles[r][c].toString();
            }
            result += "\n";
        }
        return result;
    }


    public Posn getOrigin() {
        return this.origin;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }
}
