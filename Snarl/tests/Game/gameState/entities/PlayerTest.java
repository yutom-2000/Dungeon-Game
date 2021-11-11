//package Game.gameState.entities;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;
//
//import Game.gameManager.GameManager;
//import Game.gameState.GameState;
//import Game.gameState.interaction.item.Item;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.level.Level;
//import Game.gameState.level.generation.Hallway;
//import Game.gameState.level.generation.Room;
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
//public class PlayerTest {
//
//  String LEVEL = "{ \"type\": \"level\",\n" +
//      "\"rooms\": [ { \"type\": \"room\",\n" +
//      "\"origin\": [ 3, 1 ],\n" +
//      "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//      "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//      "[ 0, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 0 ],\n" +
//      "[ 0, 2, 0, 0 ] ] },\n" +
//      "{ \"type\": \"room\",\n" +
//      "\"origin\": [ 10, 5 ],\n" +
//      "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//      "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 2, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 0, 0, 0, 0 ] ] },\n" +
//      "{ \"type\": \"room\",\n" +
//      "\"origin\": [ 4, 14 ],\n" +
//      "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//      "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 0, 0, 0, 0 ] ] } ],\n" +
//      "\"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n" +
//      "{ \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n" +
//      "\"hallways\": [ { \"type\": \"hallway\",\n" +
//      "\"from\": [ 3, 3 ],\n" +
//      "\"to\": [ 4, 16 ],\n" +
//      "\"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n" +
//      "{ \"type\": \"hallway\",\n" +
//      "\"from\": [ 6, 2 ],\n" +
//      "\"to\": [ 12, 5 ],\n" +
//      "\"waypoints\": [ [ 12, 2 ] ] } ]\n" +
//      "}";
//
//  String levelView = "■■■■■■■■■■■■■■■■■■■■\n" +
//      "■■■hhhhhhhhhhhhhh■■■\n" +
//      "■■■h■■■■■■■■■■■■h■■■\n" +
//      "■■■d■■■■■■■■■■■■h■■■\n" +
//      "■■KP■■■■■■■■■■■■d■■■\n" +
//      "■■P□■■■■■■■■■■■AA□■■\n" +
//      "■■d■■■■■■■■■■■■□□□■■\n" +
//      "■■h■■■■■■■■■■■■□□E■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■□□□■■■■■■■■■■■\n" +
//      "■■hhhd□□□■■■■■■■■■■■\n" +
//      "■■■■■■□□□■■■■■■■■■■■\n" +
//      "■■■■■■■■■■■■■■■■■■■■\n" +
//      "■■■■■■■■■■■■■■■■■■■■\n";
//
//  String ROOM0STRING = "{ \"type\": \"room\",\n" +
//      "\"origin\": [ 3, 1 ],\n" +
//      "\"bounds\": { \"rows\": 4, \"columns\": 4 },\n" +
//      "\"layout\": [ [ 0, 0, 2, 0 ],\n" +
//      "[ 0, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 0 ],\n" +
//      "[ 0, 2, 0, 0 ] ] }";
//
//  String ROOM1STRING = "{ \"type\": \"room\",\n" +
//      "\"origin\": [ 10, 5 ],\n" +
//      "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//      "\"layout\": [ [ 0, 0, 0, 0, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 2, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 0, 0, 0, 0 ] ] }";
//  String ROOM2STRING = "{ \"type\": \"room\",\n" +
//      "\"origin\": [ 4, 14 ],\n" +
//      "\"bounds\": { \"rows\": 5, \"columns\": 5 },\n" +
//      "\"layout\": [ [ 0, 0, 2, 0, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 1, 1, 1, 0 ],\n" +
//      "[ 0, 0, 0, 0, 0 ] ] }";
//
//  String PLAYERVIEW = "■d■\n" + "K*■\n" + "P□■\n"+"Notes: * represents your current position.";
//
//  Level level = null;
//  Room ROOM0, ROOM1, ROOM2 = null; //room object representations
//  GameState gameState = null;
//
//  @Before
//  public void initialGame() throws IOException {
//    ROOM0 = TestParser
//        .parseRoom(JsonParser.parseReader(new StringReader(ROOM0STRING)).getAsJsonObject());
//    ROOM1 = TestParser
//        .parseRoom(JsonParser.parseReader(new StringReader(ROOM1STRING)).getAsJsonObject());
//    ROOM2 = TestParser
//        .parseRoom(JsonParser.parseReader(new StringReader(ROOM2STRING)).getAsJsonObject());
//    Room[] rooms = new Room[]{ROOM0, ROOM1, ROOM2};
//    Hallway[] hallways = new Hallway[]{
//        new Hallway(new Posn(3, 3), new Posn(4, 16), new Posn[]{new Posn(1, 3), new Posn(1, 16)}),
//        new Hallway(new Posn(6, 2), new Posn(12, 5), new Posn[]{new Posn(12, 2)})
//    };
//    Item[] items = new Item[]{new Item(ItemType.KEY, new Posn(4, 2)),
//        new Item(ItemType.EXIT, new Posn(7, 17))};
//    Player[] players = new Player[]{new Player("player1", new Posn(4, 3)),
//            new Player("player2", new Posn(5, 2))};
//    Adversary[] adversaries = new Adversary[]{new Ghost(new Posn(5, 15)),
//            new Zombie(new Posn(5, 16))};
//    Level level = new Level(rooms, hallways, items);
//    gameState = new GameState().intermediateGame(level, players, adversaries, true, false);
//  }
//
//  @Test
//  public void testPlayerAdversaryPlace() {
//    level = gameState.getLevel();
//    assertEquals(levelView, level.toString());
//  }
//
//  @Test
//  public void testSupervisePlayer() {
//    level = gameState.getLevel();
//    Player[] players = gameState.getPlayers();
//    Player p01 = players[1];
//    assertEquals(PLAYERVIEW, p01.supervise(new Posn(4,3), level));
//  }
//
//}