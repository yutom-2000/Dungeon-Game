package testHarness.Level;

import Game.gameState.posn.Posn;

import java.util.ArrayList;

/**
 * The java object representing one input
 */
public class LevelTestInput {
    private tRoom room;
    private Posn posn;

    public LevelTestInput(tRoom room, Posn posn) {
        this.room = room;
        this.posn = posn;
    }

    /**
     * Return list of valid moves from this point given this room
     *
     * @return list of posn representing valid moves from this point in this room
     */
    public int[][] getValidMoves() throws IllegalArgumentException {
        ArrayList<int[]> result = new ArrayList<>();
        int maxRow = room.origin.getRow() + room.rows;
        int maxCol = room.origin.getCol() + room.columns;
        int minRow = room.origin.getRow();
        int minCol = room.origin.getCol();
        if (posn.getRow() >= maxRow || posn.getCol() >= maxCol
                || posn.getRow() < minRow || posn.getCol() < minCol) {
            throw new IllegalArgumentException("Failure, point is not in room");
        }
        Posn origin = room.origin;
        Posn[] cardinalMoves = new Posn[]{
                new Posn(posn.getRow() - 1, posn.getCol()),
                new Posn(posn.getRow() + 1, posn.getCol()),
                new Posn(posn.getRow(), posn.getCol() - 1),
                new Posn(posn.getRow(), posn.getCol() + 1)
        };
        for (Posn p : cardinalMoves) {
            if (p.getRow() >= minRow && p.getRow() < maxRow && p.getCol() >= minCol && p.getCol() < maxCol) {
                int tile = room.tiles[p.getRow() - minRow][p.getCol() - minCol];
                if (tile == 1 || tile == 2) result.add(p.toArray());
            }
        }
        int[][] resultArray = new int[result.size()][2];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }

    @Override
    public String toString() {
        String result = "";
        result += room.toString() + "\n";
        result += posn.toString() + "\n";
        return result;
    }

    public Posn getPosn() {
        return this.posn;
    }

    public tRoom getRoom() {
        return this.room;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LevelTestInput) {
            LevelTestInput other = (LevelTestInput) o;
            return this.room.equals(other.room)
                    && this.posn.equals(other.posn);
        } else return false;
    }
}
