//package Game.ruleChecker;
//
//
//import Game.controllers.Command.Command;
//import Game.controllers.Command.CommandMove;
//import Game.controllers.adversaryController.AdversaryController;
//import Game.gameManager.GameManager;
//import Game.controllers.playerController.PlayerController;
//
//import Game.gameState.GameState;
//import Game.gameState.entities.Adversary;
//import Game.gameState.entities.Ghost;
//import Game.gameState.entities.Player;
//import Game.gameState.entities.Zombie;
//import Game.gameState.interaction.item.Item;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.level.Level;
//import Game.gameState.level.generation.Hallway;
//import Game.gameState.level.generation.Room;
//import Game.gameState.posn.Posn;
//import com.google.gson.JsonParser;
//import org.junit.Before;
//import org.junit.Test;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//import java.io.IOException;
//import java.io.StringReader;
//
//import static org.junit.Assert.*;
//
//public class RuleCheckerTest {
//    String ROOM0STRING = "{ \"type\": \"room\",\n" +
//            "\"origin\": [ 3, 1 ],\n" +
//            "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//            "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//            "[ 0, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 0 ],\n" +
//            "[ 0, 2, 0, 0 ] ] }";
//
//    String ROOM1STRING = "{ \"type\": \"room\",\n" +
//            "\"origin\": [ 10, 5 ],\n" +
//            "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//            "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 2, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 0, 0, 0, 0 ] ] }";
//    String ROOM2STRING = "{ \"type\": \"room\",\n" +
//            "\"origin\": [ 4, 14 ],\n" +
//            "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//            "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 0, 0, 0, 0 ] ] }";
//    Room ROOM0, ROOM1, ROOM2 = null; //room object representations
//
//    @Before
//    public void testRoomsParseCorrectly() throws IOException {
//        ROOM0 = TestParser.parseRoom(JsonParser.parseReader(new StringReader(ROOM0STRING)).getAsJsonObject());
//        ROOM1 = TestParser.parseRoom(JsonParser.parseReader(new StringReader(ROOM1STRING)).getAsJsonObject());
//        ROOM2 = TestParser.parseRoom(JsonParser.parseReader(new StringReader(ROOM2STRING)).getAsJsonObject());
//
//        assertEquals("■■d■\n" +
//                "■□□■\n" +
//                "■□□■\n" +
//                "■d■■\n", ROOM0.toString());
//        assertEquals("■■■■■\n" +
//                "■□□□■\n" +
//                "d□□□■\n" +
//                "■□□□■\n" +
//                "■■■■■\n", ROOM1.toString());
//        assertEquals("■■d■■\n" +
//                "■□□□■\n" +
//                "■□□□■\n" +
//                "■□□□■\n" +
//                "■■■■■\n", ROOM2.toString());
//    }
//
//    /**
//     * Initialize testing gamestate 0
//     * @return
//     */
//    private GameState createGameState0() {
//        Room[] rooms = new Room[]{ROOM0, ROOM1, ROOM2};
//        Hallway[] hallways = new Hallway[]{
//                new Hallway(new Posn(3, 3), new Posn(4, 16), new Posn[]{new Posn(1, 3), new Posn(1, 16)}),
//                new Hallway(new Posn(6, 2), new Posn(12, 5), new Posn[]{new Posn(12, 2)})
//        };
//        Item[] items = new Item[]{new Item(ItemType.KEY, new Posn(4, 2)), new Item(ItemType.EXIT, new Posn(7, 17))};
//        Player[] players = new Player[]{new Player("player1", new Posn(4, 3)), new Player("player2", new Posn(5, 2))};
//        Adversary[] adversaries = new Adversary[]{new Ghost(1, new Posn(5, 15))};
//        Level level = new Level(rooms, hallways, items);
//
//        return new GameState().initialize(level, players, adversaries);
//    }
//
//    private GameState createIntermediateGameState0() {
//        Room[] rooms = new Room[]{ROOM0, ROOM1, ROOM2};
//        Hallway[] hallways = new Hallway[]{
//                new Hallway(new Posn (3, 3), new Posn(4, 16), new Posn[]{new Posn(1, 3), new Posn(1, 16)}),
//                new Hallway(new Posn(6, 2), new Posn(12, 5), new Posn[]{new Posn(12, 2)})
//        };
//        Item[] items = new Item[]{new Item(ItemType.KEY, new Posn(4, 2)), new Item(ItemType.EXIT, new Posn(7, 17))};
//        Player[] players = new Player[]{new Player("player1", new Posn(1, 3)), new Player("player2", new Posn(3, 3)),
//                new Player("player3", new Posn(4, 3))};
//        Adversary[] adversaries = new Adversary[]{new Ghost(new Posn(1, 4)),
//                new Zombie(new Posn(4, 16)), new Ghost(new Posn(6, 16))};
//        Level level = new Level(rooms, hallways, items);
//
//        return new GameState().intermediateGame(level, players, adversaries, true, false);
//    }
////intermediate gamestate
////            "■■■■■■■■■■■■■■■■■■■■\n" +
////            "■■■PAhhhhhhhhhhhh■■■\n" +
////            "■■■h■■■■■■■■■■■■h■■■\n" +
////            "■■■P■■■■■■■■■■■■h■■■\n" +
////            "■■KP■■■■■■■■■■■■A■■■\n" +
////            "■■□□■■■■■■■■■■■□□□■■\n" +
////            "■■d■■■■■■■■■■■■□A□■■\n" +
////            "■■h■■■■■■■■■■■■□□E■■\n" +
////            "■■h■■■■■■■■■■■■■■■■■\n" +
////            "■■h■■■■■■■■■■■■■■■■■\n" +
////            "■■h■■■■■■■■■■■■■■■■■\n" +
////            "■■h■■■□□□■■■■■■■■■■■\n" +
////            "■■hhhd□□□■■■■■■■■■■■\n" +
////            "■■■■■■□□□■■■■■■■■■■■\n" +
////            "■■■■■■■■■■■■■■■■■■■■\n" +
////            "■■■■■■■■■■■■■■■■■■■■\n"
////
//
//    @Test
//    public void testRegister() {
//        GameState gameState = createIntermediateGameState0();
//        PlayerController[] pcs = new PlayerController[]{new PlayerController("player1"), new PlayerController("player2"), new PlayerController("player3")};
//        GameManager gameManager = new GameManager();
//        gameManager.intermediateInit(pcs, new AdversaryController[0], gameState);
//
//        RuleChecker rc = new RuleChecker();
//        Command c1 = new CommandMove("player1", new Posn(1, 4), true).addGameState(gameState);
//        rc.acceptCommand(c1);
//
//        assertTrue(c1.getIsValid());
//
//    }
//}
