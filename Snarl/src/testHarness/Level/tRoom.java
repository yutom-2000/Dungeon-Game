package testHarness.Level;

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
        if (o instanceof tRoom) {
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
}
