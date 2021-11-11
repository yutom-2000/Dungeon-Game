package testHarness.Level.TestHarness.LevelTest.Structures;
import Game.gameState.posn.Posn;

public class tRoom {
    Posn origin;
    int rows;
    int columns;
    int[][] tiles; //0 means wall, 1 means empty tile, 2 means door

    public tRoom(Posn origin, int rows, int columns) {
        this.origin = origin;
        this.rows = rows;
        this.columns = columns;
        this.tiles = new int[rows][columns];
    }

    public tRoom(Posn origin, int rows, int columns, int[][] input) {
        this.origin = origin;
        this.rows = rows; this.columns = columns;
        this.tiles = input;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof testHarness.Level.tRoom) {
            tRoom other = (tRoom) o;
            return this.origin.equals(other.origin)
                    && this.columns == other.columns
                    && this.rows == other.rows
                    && java.util.Arrays.deepToString(this.tiles).equals(java.util.Arrays.deepToString(other.tiles));
        }
        else {
            return false;
        }
    }

    /**
     * Determine if a point p is in this room
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

        int tile = this.tiles[adjustedP.getRow()][adjustedP.getCol()];
        if (tile == 1 || tile == 2) {
            return true;
        } else {
            return false;
        }
    }

    public Posn getOrigin() {
        return this.origin;
    }
}
