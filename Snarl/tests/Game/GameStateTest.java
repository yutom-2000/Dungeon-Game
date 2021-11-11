//package Game;
//
//import Game.gameState.entities.EntityStatus;
//import Game.gameState.entities.Ghost;
//import Game.gameState.entities.Zombie;
//import Game.gameState.interaction.MoveResult;
//import Game.gameState.level.generation.Hallway;
//import Game.gameState.level.generation.Room;
//import Game.gameState.interaction.item.Item;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.GameState;
//import Game.gameState.level.Level;
//import Game.gameState.entities.Adversary;
//import Game.gameState.entities.Player;
//import Game.gameState.level.tiles.Tile;
//import Game.gameState.posn.Posn;
//
//import java.io.IOException;
//import java.io.StringReader;
//
//import com.google.gson.JsonParser;
//import org.junit.Before;
//import org.junit.Test;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//import static org.junit.Assert.*;
//
//
//public class GameStateTest {
//
///*
//    Level should look like this:
//
//        ■■■■■■■■■■■■■■■■■■■■
//        ■■■h h hhhhhh hhh hhh ■■■
//        ■■■h■■■■■■■■■■■■h■■■
//        ■■■d■■■■■■■■■■■■h■■■
//        ■■□□■■■■■■■■■■■■d■■■
//        ■■□□■■■■■■■■■■■□□□■■
//        ■■d■■■■■■■■■■■■□□□■■
//        ■■h■■■■■■■■■■■■□□E■■
//        ■■h■■■■■■■■■■■■■■■■■
//        ■■h■■■■■■■■■■■■■■■■■
//        ■■h■■■■■■■■■■■■■■■■■
//        ■■h■■■□□□■■■■■■■■■■■
//        ■■hhhd□□□■■■■■■■■■■■
//        ■■■■■■□□□■■■■■■■■■■■
//        ■■■■■■■■■■■■■■■■■■■■
//        ■■■■■■■■■■■■■■■■■■■■
//
//        looks out of alignment but by counting the tiles, it's actually in alignment.
// */
//
//    String gameStateJson = "{\"type\": \"state\",\n" +
//            " \"level\": { \"type\": \"level\",\n" +
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
//            "},\n" +
//            " \"players\": [{\"type\": \"player\", \"name\": \"player1\", \"position\": [4, 1]}],\n" +
//            " \"adversaries\": [{\"type\": \"zombie\", \"name\": \"zombie1\", \"position\": [4, 3]}],\n" +
//            " \"exit-locked\": \"true\"\n" +
//            "}";
//
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
//    //TEST INITIALIZAION OF GAME
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
//    @Test
//    public void testTopLeftBottomRightRoomGet() throws IOException {
//        GameState gameState = TestParser
//                .parseGameState(JsonParser.parseReader(new StringReader(gameStateJson)).getAsJsonObject());
//        assertEquals(ROOM0.getOrigin(), gameState.getLevel().getTopLeftRoom().getOrigin());
//        assertEquals(ROOM2.getOrigin(), gameState.getLevel().getBottomRightRoom().getOrigin());
//    }
//
//    /**
//     * Initialize testing gamestate 0
//     * @return
//     */
//    private GameState createGameState0() {
//        Room[] rooms = new Room[]{ROOM0, ROOM1, ROOM2};
//        Hallway[] hallways = new Hallway[]{
//                new Hallway(new Posn (3, 3), new Posn(4, 16), new Posn[]{new Posn(1, 3), new Posn(1, 16)}),
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
//        new Player("player3", new Posn(4, 3))};
//    Adversary[] adversaries = new Adversary[]{new Ghost(new Posn(1, 4)),
//        new Zombie(new Posn(4, 16)), new Ghost(new Posn(6, 16))};
//    Level level = new Level(rooms, hallways, items);
//
//    return new GameState().intermediateGame(level, players, adversaries, true, false);
//  }
//
//  private void testPlayerList(Player[] gameStatePlayers, Player[] testPlayers) {
//    for (int i = 0; i < gameStatePlayers.length; i++) {
//      Player gsPlayer = gameStatePlayers[i];
//      Player testPlayer = testPlayers[i];
//      assertEquals(gsPlayer.getName(), testPlayer.getName());
//      assertEquals(gsPlayer.getPosn(), testPlayer.getPosn());
//      assertEquals(gsPlayer.getStatus(), testPlayer.getStatus());
//    }
//  }
//
//  private void testAdversaryList(Adversary[] gsAdversaries, Adversary[] testAdversaries) {
//    for (int i = 0; i < gsAdversaries.length; i++) {
//      Adversary gsAdversary = gsAdversaries[i];
//      Adversary testAdversary = testAdversaries[i];
//      assertEquals(gsAdversary.getName(), testAdversary.getName());
//      assertEquals(gsAdversary.getPosn(), testAdversary.getPosn());
//      assertEquals(gsAdversary.getStatus(), testAdversary.getStatus());
//    }
//  }
//
//  @Test
//  public void testGameStateLevelInitialize() throws IOException {
//    GameState gameState = createGameState0();
//    //Cannot check for exact level because initial placements are randomized
//    //Instead check to ensure that players and adversaries' initial placements satisfy placement requirements (empty floor tile)
//
//    Player[] gameStatePlayers = gameState.getPlayers();
//    for (Player p : gameStatePlayers) {
//        Tile t = gameState.getLevel().tileAt(p.getPosn());
//        assertTrue(t.isFloorTile());
//        assertTrue(t.getItem() == ItemType.NONE);
//        assertTrue(t.getEntity() == p);
//    }
//
//    Adversary[] gsAdversaries = gameState.getAdversaries();
//    for (Adversary a : gsAdversaries) {
//        Tile t = gameState.getLevel().tileAt(a.getPosn());
//        assertTrue(t.isFloorTile());
//        assertTrue(t.getItem() == ItemType.NONE);
//        assertTrue(t.getEntity() == a);
//    }
//
//    assertEquals(true, gameState.getIsLocked());
//    assertEquals(false, gameState.isExit());
//  }
//
//  @Test
//  public void testGameStateIntermediate() {
//    GameState gameState = createIntermediateGameState0();
//    assertEquals("■■■■■■■■■■■■■■■■■■■■\n" +
//        "■■■PAhhhhhhhhhhhh■■■\n" +
//        "■■■h■■■■■■■■■■■■h■■■\n" +
//        "■■■P■■■■■■■■■■■■h■■■\n" +
//        "■■KP■■■■■■■■■■■■A■■■\n" +
//        "■■□□■■■■■■■■■■■□□□■■\n" +
//        "■■d■■■■■■■■■■■■□A□■■\n" +
//        "■■h■■■■■■■■■■■■□□E■■\n" +
//        "■■h■■■■■■■■■■■■■■■■■\n" +
//        "■■h■■■■■■■■■■■■■■■■■\n" +
//        "■■h■■■■■■■■■■■■■■■■■\n" +
//        "■■h■■■□□□■■■■■■■■■■■\n" +
//        "■■hhhd□□□■■■■■■■■■■■\n" +
//        "■■■■■■□□□■■■■■■■■■■■\n" +
//        "■■■■■■■■■■■■■■■■■■■■\n" +
//        "■■■■■■■■■■■■■■■■■■■■\n", gameState.getLevel().toString());
//    Player[] players = new Player[]{new Player("player1", new Posn(1, 3)),
//        new Player("player2", new Posn(3, 3)),
//        new Player("player3", new Posn(4, 3))};
//    Adversary[] adversaries = new Adversary[]{new Ghost(new Posn(1, 4)),
//        new Zombie(new Posn(4, 16)), new Ghost(new Posn(6, 16))};
//    testPlayerList(gameState.getPlayers(), players);
//    testAdversaryList(gameState.getAdversaries(), adversaries);
//  }
//
//  @Test
//  public void testMoveInteract() {
//    GameState gameState = createIntermediateGameState0();
//    Player[] players = gameState.getPlayers();
//    Adversary[] adversaries = gameState.getAdversaries();
//
//    //Move player onto key unlocks
//    assertEquals(MoveResult.KEY, gameState.moveEntity(players[2], players[2].getPosn(), new Posn(4, 2)));
//    assertEquals(players[2].getPosn(), new Posn(4, 2));
//    assertEquals(gameState.getLevel().tileAt(new Posn(4, 2)).getEntity(), players[2]);
//    assertNull(gameState.getLevel().tileAt(new Posn(4, 3)).getEntity());
//    assertFalse(gameState.getIsLocked());
//    assertFalse(gameState.isExit());
//
//    //Move player onto exit changes exit status
//    Posn oldPosn = players[1].getPosn();
//    assertEquals(MoveResult.EXIT, gameState.moveEntity(players[1], players[1].getPosn(), new Posn(7, 17)));
//    assertTrue(gameState.isExit());
//    assertNull(gameState.getLevel().tileAt(new Posn(7, 17)).getEntity());
//    assertNull(gameState.getLevel().tileAt(oldPosn).getEntity());
//  }
//
//  @Test
//  public void testKillInteractPlayerSuicide() {
//    GameState gameState = createIntermediateGameState0();
//    Player[] players = gameState.getPlayers();
//    Adversary[] adversaries = gameState.getAdversaries();
//
//    assertEquals(MoveResult.EJECT, gameState.moveEntity(players[0], players[0].getPosn(), new Posn(1, 4)));
//    assertEquals(adversaries[0], gameState.getLevel().tileAt(new Posn(1, 4)).getEntity());
//    assertEquals(EntityStatus.DEAD, players[0].getStatus());
//    assertNull(gameState.getLevel().tileAt(new Posn(1, 3)).getEntity());
//    assertEquals(new Posn(1, 3), players[0]
//        .getPosn()); //kill keeps the *last* living position, so if entity is killed upon movement, keep old posn
//  }
//
//  @Test
//  public void testKillInteractAdversaryKillPlayer() {
//    GameState gameState = createIntermediateGameState0();
//    Player[] players = gameState.getPlayers();
//    Adversary[] adversaries = gameState.getAdversaries();
//    assertEquals(MoveResult.EJECT, gameState.moveEntity(adversaries[0], adversaries[0].getPosn(), new Posn(1, 3)));
//    assertEquals(EntityStatus.DEAD, players[0].getStatus());
//    assertNull(gameState.getLevel().tileAt(new Posn(1, 4)).getEntity());
//    assertEquals(new Posn(1, 3), adversaries[0].getPosn());
//    assertEquals(new Posn(1, 3), players[0].getPosn());
//  }
//
//  @Test
//  public void testAdversaryItemInteraction() {
//    GameState gameState = createIntermediateGameState0();
//    Player[] players = gameState.getPlayers();
//    Adversary[] adversaries = gameState.getAdversaries();
//
//    assertEquals(MoveResult.OK, gameState.moveEntity(adversaries[0], adversaries[0].getPosn(), new Posn(4, 2)));
//    assertEquals(new Posn(4, 2), adversaries[0].getPosn());
//    assertEquals(ItemType.KEY, gameState.getLevel().tileAt(new Posn(4, 2)).getItem());
//    assertTrue(gameState.getIsLocked());
//
//    assertEquals(MoveResult.OK, gameState.moveEntity(adversaries[0], adversaries[0].getPosn(), new Posn(7, 17)));
//    assertFalse(gameState.isExit());
//  }
//
//}