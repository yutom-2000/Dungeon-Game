package testHarness.Level.TestHarness.LevelTest;

import Game.gameState.posn.Posn;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import testHarness.Level.TestHarness.LevelTest.Enums.TileObjectEnum;
import testHarness.Level.TestHarness.LevelTest.Structures.tLevel;
import testHarness.Level.TestHarness.LevelTest.Structures.tObject;
import testHarness.Level.TestHarness.LevelTest.Structures.tRoom;
import testHarness.Level.TestHarness.LevelTest.Structures.tHallway;

import java.io.IOException;

/**
 * Parser for json tests of format [(level), (point)]
 */
public class LevelTestHarnessParser {

    /**
     * Top level parser
     * @param read jsonReader input
     * @return TestHarnessInput representation of the input
     * @throws IOException if input is malformed in some way
     */
    public static LevelTestHarnessInput parseAll(JsonReader read) throws IOException {
        JsonArray a = JsonParser.parseReader(read).getAsJsonArray();
        JsonObject levelObject = a.get(0).getAsJsonObject();
        JsonArray jsonPosition = a.get(1).getAsJsonArray();

        tLevel level = parseLevel(levelObject);
        Posn position = parsePosn(jsonPosition);

        return new LevelTestHarnessInput(level, position);
    }

    /**
     * Parses a test level
     *
     * @param o json Object
     * @return tLevel object representing the level
     * @throws IOException jsonObject is not of type level
     */
    public static tLevel parseLevel(JsonObject o) throws IOException {
        if (!(o.get("type").getAsString().equals("level"))) {
            throw new IOException("JsonObject is not of type level");
        }

        JsonArray jsonRooms = o.get("rooms").getAsJsonArray();
        tRoom[] rooms = new tRoom[jsonRooms.size()];

        for (int i = 0; i < jsonRooms.size(); i++) {
            rooms[i] = parseRoom(jsonRooms.get(i).getAsJsonObject());
        }

        JsonArray jsonHallways = o.get("hallways").getAsJsonArray();
        tHallway[] hallways = new tHallway[jsonHallways.size()];

        for (int i = 0; i < jsonHallways.size(); i++) {
            hallways[i] = parseHallway(jsonHallways.get(i).getAsJsonObject());
        }

        JsonArray jsonObjects = o.get("objects").getAsJsonArray();
        tObject[] objects = new tObject[jsonObjects.size()];

        for (int i = 0; i < jsonObjects.size(); i++) {
            objects[i] = parseObject(jsonObjects.get(i).getAsJsonObject());
        }

        return new tLevel(rooms, hallways, objects);
    }

    /**
     * Parse a single tObject from a jsonObject
     * @param o jsonObject
     * @return tObject representation
     * @throws IOException if object given is not a valid json representation of a tObject
     */
    public static tObject parseObject(JsonObject o) throws IOException {
        String type = o.get("type").getAsString();
        if (!(type.equals("key") || type.equals("exit"))) {
            throw new IOException("Must be a valid object type");
        }

        TileObjectEnum typeEnum = TileObjectEnum.NONE;
        switch (type) {
            case "key":
                typeEnum = TileObjectEnum.KEY;
                break;
            case "exit":
                typeEnum = TileObjectEnum.EXIT;
                break;
        }

        return new tObject(typeEnum, parsePosn(o.get("position").getAsJsonArray()));
    }

    /**
     * Parse a single posn from a jsonArray
     * @param ja jsonArray
     * @return posn representation
     * @throws IOException if given ja size != 2
     */
    public static Posn parsePosn(JsonArray ja) throws IOException {
        if (ja.size() != 2) {
            throw new IOException("Must be an int array of size 2");
        }
        return new Posn(ja.get(0).getAsInt(), ja.get(1).getAsInt());
    }

    /**
     * Parse a jsonobject of a hallway
     *
     * @param o jsonObject
     * @return hallway object
     * @throws IOException if jsonobject is not of type hallway
     */
    public static tHallway parseHallway(JsonObject o) throws IOException {
        if (!(o.get("type").getAsString().equals("hallway"))) {
            throw new IOException("Must be a hallway json object");
        }

        JsonArray jFrom = o.get("from").getAsJsonArray();
        Posn from = new Posn(jFrom.get(0).getAsInt(), jFrom.get(1).getAsInt());

        JsonArray jTo = o.get("to").getAsJsonArray();
        Posn to = new Posn(jTo.get(0).getAsInt(), jTo.get(1).getAsInt());

        JsonArray jWaypoints = o.get("waypoints").getAsJsonArray();
        Posn[] waypoints = new Posn[jWaypoints.size()];

        for (int i = 0; i < jWaypoints.size(); i++) {
            JsonArray posn = jWaypoints.get(i).getAsJsonArray();
            waypoints[i] = new Posn(posn.get(0).getAsInt(), posn.get(1).getAsInt());
        }

        return new tHallway(from, to, waypoints);
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
