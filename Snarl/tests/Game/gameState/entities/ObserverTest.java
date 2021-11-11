//package Game.gameState.entities;
//
//import static org.junit.Assert.*;
//
//import Game.controllers.Observer;
//import Game.controllers.adversaryController.AdversaryController;
//import Game.gameManager.GameManager;
//import Game.gameState.GameState;
//import Game.gameState.direction.Direction;
//import Game.gameState.level.Level;
//import Game.gameState.posn.Posn;
//import Game.controllers.playerController.PlayerController;
//import com.google.gson.JsonParser;
//import java.io.IOException;
//import java.io.StringReader;
//import org.junit.Before;
//import org.junit.Test;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//
//public class ObserverTest {
//
//  Observer gameObserver = new Observer();
//
//  String LEVEL = "{ \"type\": \"level\",\n" +
//          "\"rooms\": [ { \"type\": \"room\",\n" +
//          "\"origin\": [ 3, 1 ],\n" +
//          "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//          "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//          "[ 0, 1, 1, 0 ],\n" +
//          "[ 0, 1, 1, 0 ],\n" +
//          "[ 0, 2, 0, 0 ] ] },\n" +
//          "{ \"type\": \"room\",\n" +
//          "\"origin\": [ 10, 5 ],\n" +
//          "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//          "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//          "[ 0, 1, 1, 1, 0 ],\n" +
//          "[ 2, 1, 1, 1, 0 ],\n" +
//          "[ 0, 1, 1, 1, 0 ],\n" +
//          "[ 0, 0, 0, 0, 0 ] ] },\n" +
//          "{ \"type\": \"room\",\n" +
//          "\"origin\": [ 4, 14 ],\n" +
//          "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//          "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//          "[ 0, 1, 1, 1, 0 ],\n" +
//          "[ 0, 1, 1, 1, 0 ],\n" +
//          "[ 0, 1, 1, 1, 0 ],\n" +
//          "[ 0, 0, 0, 0, 0 ] ] } ],\n" +
//          "\"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n" +
//          "{ \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n" +
//          "\"hallways\": [ { \"type\": \"hallway\",\n" +
//          "\"from\": [ 3, 3 ],\n" +
//          "\"to\": [ 4, 16 ],\n" +
//          "\"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n" +
//          "{ \"type\": \"hallway\",\n" +
//          "\"from\": [ 6, 2 ],\n" +
//          "\"to\": [ 12, 5 ],\n" +
//          "\"waypoints\": [ [ 12, 2 ] ] } ]\n" +
//          "}";
//
//  String levelView = "■■■■■■■■■■■■■■■■■■■■\n" +
//          "■■■hhhhhhhhhhhhhh■■■\n" +
//          "■■■h■■■■■■■■■■■■h■■■\n" +
//          "■■■d■■■■■■■■■■■■h■■■\n" +
//          "■■KP■■■■■■■■■■■■d■■■\n" +
//          "■■P□■■■■■■■■■■■AA□■■\n" +
//          "■■d■■■■■■■■■■■■□□□■■\n" +
//          "■■h■■■■■■■■■■■■□□E■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■□□□■■■■■■■■■■■\n" +
//          "■■hhhd□□□■■■■■■■■■■■\n" +
//          "■■■■■■□□□■■■■■■■■■■■\n" +
//          "■■■■■■■■■■■■■■■■■■■■\n" +
//          "■■■■■■■■■■■■■■■■■■■■\n";
//  String viewMoveUp = "■■■■■■■■■■■■■■■■■■■■\n" +
//          "■■■hhhhhhhhhhhhhh■■■\n" +
//          "■■■h■■■■■■■■■■■■h■■■\n" +
//          "■■■P■■■■■■■■■■■■h■■■\n" +
//          "■■K□■■■■■■■■■■■■d■■■\n" +
//          "■■P□■■■■■■■■■■■AA□■■\n" +
//          "■■d■■■■■■■■■■■■□□□■■\n" +
//          "■■h■■■■■■■■■■■■□□E■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■■■■■■■■■■■■■■■\n" +
//          "■■h■■■□□□■■■■■■■■■■■\n" +
//          "■■hhhd□□□■■■■■■■■■■■\n" +
//          "■■■■■■□□□■■■■■■■■■■■\n" +
//          "■■■■■■■■■■■■■■■■■■■■\n" +
//          "■■■■■■■■■■■■■■■■■■■■\n";
//
//  GameManager gameManager = new GameManager();
//  PlayerController pc, pc1;
//  AdversaryController ad, ad1;
//
//  @Before
//  public void initializeGame() throws IOException {
//
//    pc = new PlayerController("player1");
//    gameManager.acceptCommand(pc.commandRegister());
//    pc1 = new PlayerController("player2");
//    gameManager.acceptCommand(pc1.commandRegister());
//    ad = new AdversaryController("ghost1");
//    ad1 = new AdversaryController("ghost2");
//    gameManager.acceptCommand(ad.commandRegister());
//    gameManager.acceptCommand(ad1.commandRegister());
//    gameManager.acceptCommand(gameObserver.commandRegister());
//
//    Level level = TestParser
//            .parseLevel(JsonParser.parseReader(new StringReader(LEVEL)).getAsJsonObject());
//    gameManager.acceptCommand(pc.commandInitialize(new Level[]{level}));
//  }
//
//  @Test
//  public void testRenderInitialGame() {
//    assertNotNull(levelView, gameObserver.getUpdate());
//  }
//
////  @Test
////  public void testRenderIntermediateGame() {
////    gameManager.acceptCommand(pc.commandMove(Direction.UP));
////    assertEquals(new Posn(3, 3), gameManager.getGameState().getPlayers()[0].getPosn());
////    assertNull(gameManager.getGameState().getLevel().tileAt(new Posn(4, 3)).getEntity());
////    assertEquals(viewMoveUp, gameObserver.render());
////  }
//}