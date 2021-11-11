//package Game.gameState.entities;
//
//import static org.junit.Assert.*;
//
//import Game.gameState.GameState;
//import Game.gameState.interaction.MoveResult;
//import Game.gameState.interaction.item.Item;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.level.Level;
//import com.google.gson.JsonParser;
//import java.io.IOException;
//import java.io.StringReader;
//import org.junit.Test;
//
//import Game.gameState.posn.Posn;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//public class GhostTest {
//
//  String SMALL_LEVEL = "{ \"type\": \"level\",\n" +
//      "    \"rooms\": [ { \"type\": \"room\",\n" +
//      "    \"origin\": [ 0, 0 ],\n" +
//      "    \"bounds\": { \"rows\": 1, \"columns\": 2 },\n" +
//      "    \"layout\": [ [1, 2] ]},\n" +
//      "    { \"type\": \"room\",\n" +
//      "    \"origin\": [ 0, 3 ],\n" +
//      "    \"bounds\": { \"rows\": 1, \"columns\": 2 },\n" +
//      "    \"layout\": [ [2, 1] ] } ],\n" +
//      "\n" +
//      "    \"objects\": [ ],\n" +
//      "    \"hallways\": [ { \"type\": \"hallway\",\n" +
//      "    \"from\": [ 0, 1 ],\n" +
//      "    \"to\": [ 0, 3 ],\n" +
//      "    \"waypoints\": [ ] }]\n" +
//      "    }";
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
//      "■■□□■■■■■■■■■■■AA□■■\n" +
//      "■■d■■■■■■■■■■■■□□□■■\n" +
//      "■■h■■■■■■■■■■■■□□E■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■■■■■■■■■■■■■■■\n" +
//      "■■h■■■□□□■■■■■■■■■■■\n" +
//      "■■hhhd□□□■■■■■■■■■■■\n" +
//      "■■■■■■□□□■■■■■■■■■■■\n" +
//      "■■■■■■■■■■■■■■■■■■■■\n" +
//      "■■■■■■■■■■■■■■■■■■■■";
//
//  String viewMoveUp = "■■■■■■■■■■■■■■■■■■■■\n" +
//      "■■■hhhhhhhhhhhhhh■■■\n" +
//      "■■■h■■■■■■■■■■■■h■■■\n" +
//      "■■■P■■■■■■■■■■■■h■■■\n" +
//      "■■K□■■■■■■■■■■■■d■■■\n" +
//      "■■□□■■■■■■■■■■■A□A■■\n" +
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
//  String viewMovetoDoor = "■■■■■■■■■■■■■■■■■■■■\n" +
//      "■■■hhhhhhhhhhhhhh■■■\n" +
//      "■■■h■■■■■■■■■■■■h■■■\n" +
//      "■■■P■■■■■■■■■■■■h■■■\n" +
//      "■■K□■■■■■■■■■■■■A■■■\n" +
//      "■■□□■■■■■■■■■■■A□□■■\n" +
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
//  @Test
//  public void testGhostRegularMove() throws IOException {
//    Item[] items = new Item[]{new Item(ItemType.KEY, new Posn(4, 2)),
//            new Item(ItemType.EXIT, new Posn(7, 17))};
//    Player[] players = new Player[]{new Player("player1", new Posn(4, 3))};
//    Adversary[] adversaries = new Adversary[]{new Ghost(new Posn(5, 15)),
//            new Zombie(new Posn(5, 16))};
//    Level level = TestParser
//            .parseLevel(JsonParser.parseReader(new StringReader(LEVEL)).getAsJsonObject());
//    GameState gameState = new GameState();
//    gameState.intermediateGame(level, players, adversaries, true, false);
//    gameState.moveEntity(adversaries[1], adversaries[1].getPosn(), new Posn(5,17));
//    gameState.moveEntity(players[0], players[0].getPosn(), new Posn(3,3));
//
//    assertEquals(viewMoveUp, gameState.getLevel().toString());
//  }
//
//  @Test
//  public void testGhostMovetoDoor() throws IOException {
//    Item[] items = new Item[]{new Item(ItemType.KEY, new Posn(4, 2)),
//            new Item(ItemType.EXIT, new Posn(7, 17))};
//    Player[] players = new Player[]{new Player("player1", new Posn(4, 3))};
//    Adversary[] adversaries = new Adversary[]{new Zombie(new Posn(5, 15)),
//            new Ghost(new Posn(5, 16))};
//    Level level = TestParser
//            .parseLevel(JsonParser.parseReader(new StringReader(LEVEL)).getAsJsonObject());
//    GameState gameState = new GameState();
//    gameState.intermediateGame(level, players, adversaries, true, false);
//    assertEquals(MoveResult.OK, gameState.moveEntity(adversaries[1], adversaries[1].getPosn(), new Posn(4,16)));
//    gameState.moveEntity(players[0], players[0].getPosn(), new Posn(3,3));
//
//    assertEquals(viewMovetoDoor, gameState.getLevel().toString());
//  }
//
//}