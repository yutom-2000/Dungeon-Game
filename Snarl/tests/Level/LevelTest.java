//package Level;
//
//import Game.gameState.entities.Adversary;
//import Game.gameState.entities.Ghost;
//import Game.gameState.entities.Player;
//import Game.gameState.interaction.item.Item;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.level.generation.Hallway;
//import Game.gameState.level.generation.Room;
//
//import Game.gameState.level.tiles.*;
//import Game.gameState.level.Level;
//
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.stream.JsonReader;
//import org.junit.Test;
//import Game.gameState.posn.Posn;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//
//import java.io.IOException;
//import java.io.StringReader;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Testing class to test Level functionality
// */
//public class LevelTest {
//    Tile[] ROW0 = {new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL),
//            new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL)};
//    Tile[] ROW1 = {new Tile(TileType.WALL), new Tile(TileType.FLOOR).placeItem(ItemType.KEY), new Tile(TileType.FLOOR), new Tile(TileType.WALL),
//            new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.WALL)};
//    Tile[] ROW2 = {new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR),
//            new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.WALL)};
//    Tile[] ROW3 = {new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.WALL),
//            new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR).placeItem(ItemType.EXIT), new Tile(TileType.WALL)};
//    Tile[] ROW4 = {new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL),
//            new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL)};
//    Tile[][] EXAMPLE = {ROW0, ROW1, ROW2, ROW3, ROW4};
//
//    Room ROOM = new Room(new Posn(0, 0), EXAMPLE);
//
//    @Test
//    public void testProductVisitor() {
//        Level testLevel =
//                new Level(new Room[]{ROOM}, new Hallway[]{},
//                        new Item[]{});
//        assertEquals("■■■■■■■■■\n" +
//                "■K□■■■□■■\n" +
//                "■□□□□□□■■\n" +
//                "■□□■■□E■■\n" +
//                "■■■■■■■■■\n" +
//                "■■■■■■■■■\n", testLevel.toString());
//    }
//
//    @Test
//    public void testExample2() {
//        Tile[] ROW5 = {new Tile(TileType.WALL), new Tile(TileType.FLOOR).placeEntity(new Player(), false),
//                new Tile(TileType.FLOOR).placeEntity(new Ghost(), false),
//                new Tile(TileType.WALL),
//                new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR).placeItem(ItemType.EXIT), new Tile(TileType.WALL)};
//        Tile[] ROW6 = new Tile[] {new Tile(TileType.WALL), new Tile(TileType.DOOR), new Tile(TileType.WALL),
//                new Tile(TileType.WALL),
//                new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.WALL)};
//        Tile[][] EXAMPLE2 = {ROW0, ROW1, ROW2, ROW5, ROW6};
//        Room room = new Room(new Posn(0,0), EXAMPLE2);
//
//        Level testLevel2 = new Level(new Room[]{room}, new Hallway[]{}, new Item[]{});
//        assertEquals("■■■■■■■■■\n" +
//                "■K□■■■□■■\n" +
//                "■□□□□□□■■\n" +
//                "■PA■■□E■■\n" +
//                "■d■■■■■■■\n" +
//                "■■■■■■■■■\n", testLevel2.toString());
//    }
//
//    @Test
//    public void testRooms() {
//        Tile[][] ROOM0 = {
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//        };
//        Room r1 = new Room(new Posn(1, 1), ROOM0[0].length, ROOM0.length, ROOM0);
//        Level testLevel = new Level(new Room[]{r1}, new Hallway[]{}, new Item[]{});
//        assertEquals("■■■■\n" +
//                "■□□■\n" +
//                "■□□■\n" +
//                "■■■■\n", testLevel.toString());
//        Tile[][] ROOM1 = {
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)}
//        };
//        Room r2 = new Room(new Posn(1, 4), ROOM1[0].length, ROOM1.length, ROOM1);
//        testLevel = new Level(new Room[]{r1, r2}, new Hallway[]{}, new Item[]{});
//        assertEquals("■■■■■■■■\n" +
//                "■□□■□□□■\n" +
//                "■□□■□□□■\n" +
//                "■■■■□□□■\n" +
//                "■■■■■■■■\n", testLevel.toString());
//    }
//
//
//    @Test
//    public void testHallways() {
//        Tile[][] ROOM0 = {
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//        };
//        Tile[][] ROOM1 = {
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)}
//        };
//
//        Tile[][] ROOM2 = {
//                {new Tile(TileType.FLOOR), new Tile(TileType.FLOOR)},
//                {new Tile(TileType.DOOR), new Tile(TileType.FLOOR)},
//        };
//
//        Room r1 = new Room(new Posn(1, 1), ROOM0[0].length, ROOM0.length, new Posn[]{new Posn(0, 1)}, ROOM0);
//        Room r2 = new Room(new Posn(1, 4), ROOM1[0].length, ROOM1.length, new Posn[]{new Posn(0, 0)}, ROOM1);
//        Hallway h1 = new Hallway(new Posn(1, 2), new Posn(1, 4));
//        Level testLevel = new Level(new Room[]{r1, r2}, new Hallway[]{h1}, new Item[]{});
//        assertEquals("■■■■■■■■\n" +
//                "■□dhd□□■\n" +
//                "■□□■□□□■\n" +
//                "■■■■□□□■\n" +
//                "■■■■■■■■\n", testLevel.toString());
//
//        r1 = new Room(new Posn(1, 1), ROOM0[0].length, ROOM0.length, ROOM2);
//        r2 = new Room(new Posn(4, 1), ROOM1[0].length, ROOM1.length, ROOM1);
//        h1 = new Hallway(new Posn(2, 1), new Posn(4, 1));
//        testLevel = new Level(new Room[]{r1, r2}, new Hallway[]{h1}, new Item[]{});
//        assertEquals("■■■■■\n" +
//                "■□□■■\n" +
//                "■d□■■\n" +
//                "■h■■■\n" +
//                "■d□□■\n" +
//                "■□□□■\n" +
//                "■□□□■\n" +
//                "■■■■■\n", testLevel.toString());
//
//
//        r1 = new Room(new Posn(1, 1), ROOM0[0].length, ROOM0.length, ROOM0);
//        r2 = new Room(new Posn(2, 6), ROOM1[0].length, ROOM1.length, ROOM1);
//        Posn[] waypoints1 = {new Posn(1,4), new Posn(2,4)};
//        Hallway h2 = new Hallway(new Posn(1, 2), new Posn(2, 6), waypoints1);
//        testLevel = new Level(new Room[]{r1, r2}, new Hallway[]{h2}, new Item[]{});
//        assertEquals("■■■■■■■■■■\n" +
//                "■□dhh■■■■■\n" +
//                "■□□■hhd□□■\n" +
//                "■■■■■■□□□■\n" +
//                "■■■■■■□□□■\n" +
//                "■■■■■■■■■■\n", testLevel.toString());
//
//        r1 = new Room(new Posn(1, 1), ROOM0[0].length, ROOM0.length, new Posn[]{new Posn(1, 0)},
//                ROOM2);
//        r2 = new Room(new Posn(5, 6), ROOM1[0].length, ROOM1.length, new Posn[]{new Posn(0, 0)},
//                ROOM1);
//        Posn[] waypoints2 = {new Posn(5,1)};
//        Hallway h3 = new Hallway(new Posn(2, 1), new Posn(5, 6), waypoints2);
//        testLevel = new Level(new Room[]{r1, r2}, new Hallway[]{h3}, new Item[]{});
//        assertEquals("■■■■■■■■■■\n" +
//                "■□□■■■■■■■\n" +
//                "■d□■■■■■■■\n" +
//                "■h■■■■■■■■\n" +
//                "■h■■■■■■■■\n" +
//                "■hhhhhd□□■\n" +
//                "■■■■■■□□□■\n" +
//                "■■■■■■□□□■\n" +
//                "■■■■■■■■■■\n", testLevel.toString());
//
//    }
//
//    @Test
//    public void testRoomParseFromJson() {
//        String testInputRoom = "{ \"type\" : \"room\",\n" +
//                "\"origin\" : [0, 1],\n" +
//                "\"bounds\" : { \"rows\" : 3,\n" +
//                "\"columns\" : 5 },\n" +
//                "\"layout\" : [ [0, 0, 2, 0, 0],\n" +
//                "[0, 1, 1, 1, 0],\n" +
//                "[0, 0, 2, 0, 0] ] }";
//        StringReader r = new StringReader(testInputRoom);
//        JsonReader reader = new JsonReader(r);
//
//        try {
//            JsonObject jo = JsonParser.parseReader(reader).getAsJsonObject();
//            Room room = TestParser.parseRoom(jo);
//            Tile[] row0 = {new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.DOOR), new Tile(TileType.WALL), new Tile(TileType.WALL)};
//            Tile[] row1 = {new Tile(TileType.WALL), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.FLOOR), new Tile(TileType.WALL)};
//            Tile[] row2 = {new Tile(TileType.WALL), new Tile(TileType.WALL), new Tile(TileType.DOOR), new Tile(TileType.WALL), new Tile(TileType.WALL)};
//            Tile[][] tiles = new Tile[][]{row0, row1, row2};
//            assertEquals(new Room(new Posn(0, 1), 3, 5, tiles), room);
//        } catch (IOException e) {
//            System.out.print(e);
//        }
//    }
//
//    @Test
//    public void toStringFromJson() {
//        String testInputRoom = "{ \"type\": \"level\",\n" +
//                "\"rooms\": [ { \"type\": \"room\",\n" +
//                "\"origin\": [ 3, 1 ],\n" +
//                "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//                "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//                "[ 0, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 0 ],\n" +
//                "[ 0, 2, 0, 0 ] ] },\n" +
//                "{ \"type\": \"room\",\n" +
//                "\"origin\": [ 10, 5 ],\n" +
//                "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//                "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 2, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 0, 0, 0, 0 ] ] },\n" +
//                "{ \"type\": \"room\",\n" +
//                "\"origin\": [ 4, 14 ],\n" +
//                "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//                "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 0, 0, 0, 0 ] ] } ],\n" +
//                "\"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n" +
//                "{ \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n" +
//                "\"hallways\": [ { \"type\": \"hallway\",\n" +
//                "\"from\": [ 3, 3 ],\n" +
//                "\"to\": [ 4, 16 ],\n" +
//                "\"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n" +
//                "{ \"type\": \"hallway\",\n" +
//                "\"from\": [ 6, 2 ],\n" +
//                "\"to\": [ 12, 5 ],\n" +
//                "\"waypoints\": [ [ 12, 2 ] ] } ]\n" +
//                "}";
//
//        StringReader r = new StringReader(testInputRoom);
//        JsonReader reader = new JsonReader(r);
//
//        try {
//            JsonObject jo = JsonParser.parseReader(reader).getAsJsonObject();
//            Level level = TestParser.parseLevel(jo);
//
//            assertEquals("■■■■■■■■■■■■■■■■■■■■\n" +
//                    "■■■hhhhhhhhhhhhhh■■■\n" +
//                    "■■■h■■■■■■■■■■■■h■■■\n" +
//                    "■■■d■■■■■■■■■■■■h■■■\n" +
//                    "■■K□■■■■■■■■■■■■d■■■\n" +
//                    "■■□□■■■■■■■■■■■□□□■■\n" +
//                    "■■d■■■■■■■■■■■■□□□■■\n" +
//                    "■■h■■■■■■■■■■■■□□E■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■□□□■■■■■■■■■■■\n" +
//                    "■■hhhd□□□■■■■■■■■■■■\n" +
//                    "■■■■■■□□□■■■■■■■■■■■\n" +
//                    "■■■■■■■■■■■■■■■■■■■■\n" +
//                    "■■■■■■■■■■■■■■■■■■■■\n", level.toString());
//
//        } catch (IOException e) {
//            System.out.print(e);
//        }
//    }
//
//    @Test
//    public void testGetSubset() {
//        String testInputRoom = "{ \"type\": \"level\",\n" +
//                "\"rooms\": [ { \"type\": \"room\",\n" +
//                "\"origin\": [ 3, 1 ],\n" +
//                "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//                "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//                "[ 0, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 0 ],\n" +
//                "[ 0, 2, 0, 0 ] ] },\n" +
//                "{ \"type\": \"room\",\n" +
//                "\"origin\": [ 10, 5 ],\n" +
//                "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//                "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 2, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 0, 0, 0, 0 ] ] },\n" +
//                "{ \"type\": \"room\",\n" +
//                "\"origin\": [ 4, 14 ],\n" +
//                "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//                "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 1, 1, 1, 0 ],\n" +
//                "[ 0, 0, 0, 0, 0 ] ] } ],\n" +
//                "\"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n" +
//                "{ \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n" +
//                "\"hallways\": [ { \"type\": \"hallway\",\n" +
//                "\"from\": [ 3, 3 ],\n" +
//                "\"to\": [ 4, 16 ],\n" +
//                "\"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n" +
//                "{ \"type\": \"hallway\",\n" +
//                "\"from\": [ 6, 2 ],\n" +
//                "\"to\": [ 12, 5 ],\n" +
//                "\"waypoints\": [ [ 12, 2 ] ] } ]\n" +
//                "}";
//
//        StringReader r = new StringReader(testInputRoom);
//        JsonReader reader = new JsonReader(r);
//
//        try {
//            JsonObject jo = JsonParser.parseReader(reader).getAsJsonObject();
//            Level level = TestParser.parseLevel(jo);
//
//            assertEquals("■■■■■■■■■■■■■■■■■■■■\n" +
//                    "■■■hhhhhhhhhhhhhh■■■\n" +
//                    "■■■h■■■■■■■■■■■■h■■■\n" +
//                    "■■■d■■■■■■■■■■■■h■■■\n" +
//                    "■■K□■■■■■■■■■■■■d■■■\n" +
//                    "■■□□■■■■■■■■■■■□□□■■\n" +
//                    "■■d■■■■■■■■■■■■□□□■■\n" +
//                    "■■h■■■■■■■■■■■■□□E■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■■■■■■■■■■■■■■■\n" +
//                    "■■h■■■□□□■■■■■■■■■■■\n" +
//                    "■■hhhd□□□■■■■■■■■■■■\n" +
//                    "■■■■■■□□□■■■■■■■■■■■\n" +
//                    "■■■■■■■■■■■■■■■■■■■■\n" +
//                    "■■■■■■■■■■■■■■■■■■■■\n", level.toString());
//
//            assertEquals("■■hh\n" +
//                    "■■h■\n" +
//                    "■■d■\n" +
//                    "■K□■\n", level.getSubsetString(new Posn(1, 1), new Posn(5, 5)));
//
//            assertEquals("■■■■■\n" +
//                    "■■■■■\n" +
//                    "■■■■■\n", level.getSubsetString(new Posn(-2, -2), new Posn(1, 3)));
//
//            assertEquals(level.toString(), level.getSubsetString(new Posn(0, 0)));
//
//        } catch (IOException e) {
//            System.out.print(e);
//        }
//    }
//
//
//    /**
//     * For testing to get the walkable positions reachable in 1 step from given position at given room
//     * @param r
//     * @param position
//     * @return
//     */
//    private Posn[] walkablePositions(Room r, Posn position) {
//        int posRow = position.getRow();
//        int posCol = position.getCol();
//        Posn[] testPositions = new Posn[]{
//                new Posn(posRow - 1, posCol),
//                new Posn(posRow + 1, posCol),
//                new Posn(posRow, posCol - 1),
//                new Posn(posRow, posCol + 1)
//        };
//        Level testLevel = new Level(new Room[]{r}, new Hallway[]{}, new Item[]{});
//        Posn[] result = new Posn[4];
//        int i = 0;
//        for (Posn p : testPositions) {
//            try {
//                if (testLevel.tileAt(p).isStepable(true)) {
//                    result[i] = p;
//                    i++;
//                }
//            } catch (IndexOutOfBoundsException e) {
//                //pass
//            }
//        }
//        return result;
//    }
//
//    @Test
//    public void testValidMoves() {
//        String testInput = "[" +
//                "{ \"type\" : \"room\",\n" +
//                "\"origin\" : [0, 1],\n" +
//                "\"bounds\" : { \"rows\" : 3,\n" +
//                "\"columns\" : 5 },\n" +
//                "\"layout\" : [ [0, 0, 2, 0, 0],\n" +
//                "[0, 1, 1, 1, 0],\n" +
//                "[0, 0, 2, 0, 0] ] }," +
//                "[1, 3]" +
//                "]";
//        try {
//            JsonArray input = JsonParser.parseReader(new StringReader(testInput)).getAsJsonArray();
//            JsonObject roomJson = input.get(0).getAsJsonObject();
//            JsonArray positionJson = input.get(1).getAsJsonArray();
//
//            Room room = TestParser.parseRoom(roomJson);
//            Posn position = TestParser.parsePosn(positionJson);
//            Posn[] result = walkablePositions(room, position);
//            assertEquals(new Posn[]{new Posn(0, 3), new Posn(2, 3),
//                            new Posn(1, 2), new Posn(1, 4)}
//            , result);
//
//            result = walkablePositions(room, new Posn(0, 1));
//            assertEquals(new Posn[4], result);
//
//            result = walkablePositions(room, new Posn(0, 2));
//            assertEquals(new Posn[]{new Posn(1, 2), new Posn(0, 3), null, null}, result);
//
//        } catch (IOException e) {
//            System.out.print(e);
//        }
//    }
//
//}
