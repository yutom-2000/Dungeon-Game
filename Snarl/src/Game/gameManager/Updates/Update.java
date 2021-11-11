package Game.gameManager.Updates;

import Game.gameState.ActorPosition;
import Game.gameState.GameState;
import Game.gameState.ObjectPosition;
import Game.gameState.entities.EntityStatus;

import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Object represents a single update sent to clients (aka controllers)
 * <p>
 * Contains information about
 * 1. The controller's representative entity's position in the level
 * 2. The surrounding level (how much of the level can be seen depends on type (player/adversary/observer)
 * This will be held as string because player will not need to interact with a tile
 * Level information is display-only
 */
public class Update {
    String to;
    Posn position;
    EntityStatus status;
    String level;
    boolean isLocked;
    boolean isExit;
    GameState gameState;
    public ActorPosition[] actorPositionList;
    public ObjectPosition[] objectPositionList;
    Tile[][] tiles;
    int[][] tileLayout;
    String message;

    /**
     * Construct and Update object
     *
     * @param position
     * @param status
     * @param level
     */
    public Update(String to, Posn position, EntityStatus status, String level, boolean isLocked, boolean isExit) {
        this.to = to;
        this.position = position;
        this.status = status;
        this.level = level;
        this.isLocked = isLocked;
        this.isExit = isExit;
        this.gameState = null;
    }

    public Update(String to, Posn position, EntityStatus status, String level, boolean isLocked, boolean isExit, GameState gs) {
        this.to = to;
        this.position = position;
        this.status = status;
        this.level = level;
        this.isLocked = isLocked;
        this.isExit = isExit;
        this.gameState = gs;
    }

    /**
     * Null constructor for builder pattern style
     */
    public Update() {
    }

    public Update addTileLayout(int[][] tileLayout) {
        this.tileLayout = tileLayout;
        return this;
    }

    public Update addMessage(String message) {
        this.message = message;
        return this;
    }

    public Update addPosition(Posn p) {
        this.position = p;
        return this;
    }

    public Update addEntityStatus(EntityStatus status) {
        this.status = status;
        return this;
    }

    public Update addLevel(String level) {
        this.level = level;
        return this;
    }

    public Update addIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public Update addIsExit(boolean isExit) {
        this.isExit = isExit;
        return this;
    }

    public Update addGamestate(GameState gs) {
        this.gameState = gs;
        return this;
    }

    public Update addActorPositionList(ActorPosition[] actorPositionList) {
        this.actorPositionList = actorPositionList;
        return this;
    }

    public Update addObjectPositionList(ObjectPosition[] objectPositionList) {
        this.objectPositionList = objectPositionList;
        return this;
    }

    public Update(Posn position, String level) {
        this.position = position;
        this.level = level;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public int[][] getTileLayout() {
        return this.tileLayout;
    }

    public Posn getPosition() {
        return this.position;
    }

    public EntityStatus getStatus() {
        return this.status;
    }

    public String getLevel() {
        return this.level;
    }

    public String getTo() {
        return to;
    }

    public boolean isExit() {
        return isExit;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Update addLevelString(String level) {
        this.level = level;
        return this;
    }

    private int[] expandRow(int[] original, int toAppend) {
        int[] result = new int[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

    private int[][] appendRow(int[][] original, int[] toAppend) {
        int[][] result = new int[original.length + 1][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

    /**
     * Turn this level string into a valid layout int array
     *
     * @return
     */
    private int[][] levelToLayout() {
        int[][] result = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                switch (tiles[i][j].getTileType()) {
                    case WALL: result[i][j] = 0;
                    break;
                    case FLOOR: result[i][j] = 1;
                    break;
                    case HALLWAY: result[i][j] = 1;
                    break;
                    case DOOR: result[i][j] = 2;
                    break;
                }
            }
        }
        return result;
    }


    @Override
    public String toString() {
        JsonObject jo = new JsonObject();
        Gson gson = new Gson();
        JsonArray layout =  gson.toJsonTree(this.tileLayout).getAsJsonArray();
        JsonArray position = gson.toJsonTree(this.position.toArray()).getAsJsonArray();
        JsonArray objects = gson.toJsonTree(this.objectPositionList).getAsJsonArray();
        JsonArray actors = gson.toJsonTree(this.actorPositionList).getAsJsonArray();

        jo.addProperty("type", "player-update");
        jo.add("layout", layout);
        jo.add("position", position);
        jo.add("objects", objects);
        jo.add("actors", actors);
        jo.addProperty("message", message); //todo remove this eventually

        return jo.toString();
    }
}
