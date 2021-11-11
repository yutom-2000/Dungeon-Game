package testHarness.Level;

import Game.gameState.posn.Posn;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * Parser for json tests of format [ (room), (point)]
 */
public class RoomTestParser {

    /**
     * Top level parser function
     *
     * @param read jsonreader
     * @return LevelTestInput object representing the input
     * @throws IOException incorrectly formatted input
     */
    public static LevelTestInput parseAll(JsonReader read) throws IOException {
        JsonArray a = JsonParser.parseReader(read).getAsJsonArray();
        JsonObject roomJsonObject = a.get(0).getAsJsonObject();
        tRoom room = parseRoom(roomJsonObject);

        JsonArray pointJsonArray = a.get(1).getAsJsonArray();
        Posn point = new Posn(pointJsonArray.get(0).getAsInt(), pointJsonArray.get(1).getAsInt());
        return new LevelTestInput(room, point);
    }

    /**
     * Returns a room object
     *
     * @param o JsonObject must be of type room
     * @return room
     * @throws IOException json provided is not well-provided room
     */
    public static tRoom parseRoom(JsonObject o) throws IOException {
        if (!(o.get("type").getAsString().equals("room"))) {
            throw new IOException("Must be room json object");
        }

        int originRow = o.get("origin").getAsJsonArray().get(0).getAsInt();
        int originCol = o.get("origin").getAsJsonArray().get(1).getAsInt();
        Posn origin = new Posn(originRow, originCol);

        JsonObject bounds = o.get("bounds").getAsJsonObject();
        int roomRows = bounds.get("rows").getAsInt();
        int roomColumns = bounds.get("columns").getAsInt();

        int[][] layout = new int[roomRows][roomColumns];
        JsonArray layoutArray = o.get("layout").getAsJsonArray();

        for (int y = 0; y < roomRows; y++) {
            for (int x = 0; x < roomColumns; x++) {
                layout[y][x] = get2dJsonArray(layoutArray, y, x);
            }
        }
        return new tRoom(origin, roomRows, roomColumns, layout);
    }

    /**
     * Helper method to use json array like a 2d array
     *
     * @param array json array
     * @param row   row index
     * @param col   col index
     * @return
     */
    private static int get2dJsonArray(JsonArray array, int row, int col) {
        return array.get(row).getAsJsonArray().get(col).getAsInt();
    }
}
