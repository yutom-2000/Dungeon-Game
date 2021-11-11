//package Game.gameManager;
//
//import Game.controllers.adversaryController.AdversaryController;
//import Game.controllers.playerController.PlayerController;
//import Game.gameState.direction.Direction;
//import Game.gameState.level.Level;
//import Game.gameState.posn.Posn;
//import com.google.gson.JsonParser;
//
//import org.hamcrest.Matcher;
//import org.junit.Test;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
///**
// * Tests regarding the receiving of updates by the controllers/observers
// */
////redundant right now, handled by gameManager right now
//public class updateTest {
//    String LEVEL = "{ \"type\": \"level\",\n" +
//            "\"rooms\": [ { \"type\": \"room\",\n" +
//            "\"origin\": [ 3, 1 ],\n" +
//            "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//            "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//            "[ 0, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 0 ],\n" +
//            "[ 0, 2, 0, 0 ] ] },\n" +
//            "{ \"type\": \"room\",\n" +
//            "\"origin\": [ 10, 5 ],\n" +
//            "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//            "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 2, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 0, 0, 0, 0 ] ] },\n" +
//            "{ \"type\": \"room\",\n" +
//            "\"origin\": [ 4, 14 ],\n" +
//            "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//            "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 1, 1, 1, 0 ],\n" +
//            "[ 0, 0, 0, 0, 0 ] ] } ],\n" +
//            "\"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n" +
//            "{ \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n" +
//            "\"hallways\": [ { \"type\": \"hallway\",\n" +
//            "\"from\": [ 3, 3 ],\n" +
//            "\"to\": [ 4, 16 ],\n" +
//            "\"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n" +
//            "{ \"type\": \"hallway\",\n" +
//            "\"from\": [ 6, 2 ],\n" +
//            "\"to\": [ 12, 5 ],\n" +
//            "\"waypoints\": [ [ 12, 2 ] ] } ]\n" +
//            "}";
//
//    @Test
//    public void testInitialize() throws IOException {
//        PlayerController pc = new PlayerController("player1");
//        PlayerController pc1 = new PlayerController("player2");
//        AdversaryController ad = new AdversaryController("ghost1");
//        AdversaryController ad1 = new AdversaryController("ghost2");
//        GameManager gameManager = new GameManager();
//        gameManager.acceptCommand(pc.commandRegister());
//        gameManager.acceptCommand(pc1.commandRegister());
//        gameManager.acceptCommand(ad.commandRegister());
//        gameManager.acceptCommand(ad1.commandRegister());
//
//
//        Level level = TestParser.parseLevel(JsonParser.parseReader(new StringReader(LEVEL)).getAsJsonObject());
//        gameManager.acceptCommand(pc.commandInitialize(new Level[]{level}));
//
//        assertEquals(gameManager.getPlayers().toString(), gameManager.getGameState().getPlayers().toString());
//
//        assertTrue(pc.getUpdate() != null);
//        assertTrue(pc1.getUpdate() != null);
//    }
//}
