package Game.gameState.posn;

import Game.gameState.direction.Direction;

/**
 * Class representing a position
 * Holds an X and Y integer to store position information
 */
public class Posn {
    private int row;
    private int col;

    /**
     * Constructor for Game.gameState.posn
     * @param row x-coord
     * @param col y-coord
     */
    public Posn(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get row coord
     * @return row coord
     */
    public int getRow() {
        return row;
    }

    /**
     * Get col coord
     * @return col coord
     */
    public int getCol() {
        return col;
    }

    /**
     * Set the row value
     * @param row new row value
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Set the col value
     * @param col new col value
     */
    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Posn) {
            Posn b = (Posn) o;
            return this.row == b.getRow() && this.col == b.getCol();
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = "[";
        result += this.row;
        result += ", ";
        result += this.col;
        result += "]";
        return result;
    }

    public static Posn[] append(Posn[] a, Posn b) {
        Posn[] result = new Posn[a.length + 1];
        int index = 0;
        while (index < a.length) {
            result[index] = a[index];
            index++;
        }
        result[index] = b;

        return result;
    }

    public static Posn[] append(Posn[] a, Posn[] b) {
        Posn[] result = new Posn[a.length + b.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i];
        }
        int offset = a.length;
        for (int i = 0; i < b.length; i++) {
            result[i + offset] = b[i];
        }
        return result;
    }

    /**
     * Returns the manhattan distance of a player
     * @param a position a
     * @param b position b
     * @return
     */
    public static int distance(Posn a, Posn b) {
        return Math.abs(a.col - b.col) + Math.abs(a.row - b.row);
    }

    /**
     * Convert a posn to int array for easy json-ification
     * @return int[2] representation of this posn
     */
    public int[] toArray() {
        return new int[]{row, col};
    }

    /**
     * Convenience method to turn an array of posns into an int array for easy json-ification
     * @param posns list of posns
     * @return int[][2] represnting posns as a list of ints
     */
    public static int[][] convertToArray(Posn[] posns) {
        int[][] result = new int[posns.length][];
        for (int i = 0; i < posns.length; i++) {
            result[i] = posns[i].toArray();
        }
        return result;
    }

    public static Posn[] convertToPosn(int[][] posns) {
        Posn[] result = new Posn[posns.length];
        for (int i = 0; i < posns.length; i++) {
            result[i] = new Posn(posns[i][0], posns[i][1]);
        }
        return result;
    }

    /**
     * Calculate the position of a POSN if headed in either 5 directions
     * @param direction
     * @return
     */
    public Posn goDirection(Direction direction) {
        switch (direction) {
            case UP:
                return new Posn(row - 1, col);
            case DOWN:
                return new Posn(row + 1, col);
            case LEFT:
                return new Posn(row, col - 1);
            case RIGHT:
                return new Posn(row, col + 1);
            case STAY:
                return this;
        }
        return this;
    }
}
