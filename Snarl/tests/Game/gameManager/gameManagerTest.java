//package Game.gameManager;
//
//import Game.controllers.Observer;
//import Game.controllers.adversaryController.AdversaryController;
//import Game.controllers.adversaryController.GhostController;
//import Game.gameState.direction.Direction;
//import Game.gameState.entities.EntityStatus;
//import Game.gameState.entities.Player;
//import Game.gameState.level.Level;
//import Game.gameState.posn.Posn;
//import Game.controllers.playerController.PlayerController;
//import static org.junit.Assert.*;
//
//import com.google.gson.JsonParser;
//import org.junit.Test;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//
//import java.io.IOException;
//import java.io.StringReader;
//
//import static org.junit.Assert.*;
//
//public class gameManagerTest {
//
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
//    String SMALL_LEVEL = "{ \"type\": \"level\",\n" +
//            "    \"rooms\": [ { \"type\": \"room\",\n" +
//            "    \"origin\": [ 0, 0 ],\n" +
//            "    \"bounds\": { \"rows\": 1, \"columns\": 2 },\n" +
//            "    \"layout\": [ [1, 2] ]},\n" +
//            "    { \"type\": \"room\",\n" +
//            "    \"origin\": [ 0, 3 ],\n" +
//            "    \"bounds\": { \"rows\": 1, \"columns\": 2 },\n" +
//            "    \"layout\": [ [2, 1] ] } ],\n" +
//            "\n" +
//            "    \"objects\": [ ],\n" +
//            "    \"hallways\": [ { \"type\": \"hallway\",\n" +
//            "    \"from\": [ 0, 1 ],\n" +
//            "    \"to\": [ 0, 3 ],\n" +
//            "    \"waypoints\": [ ] }]\n" +
//            "    }";
//
//    String SMALL_LEVEL_1 = "{ \"type\": \"level\",\n" +
//            "    \"rooms\": [ { \"type\": \"room\",\n" +
//            "    \"origin\": [ 0, 0 ],\n" +
//            "    \"bounds\": { \"rows\": 1, \"columns\": 3 },\n" +
//            "    \"layout\": [ [1, 1, 2] ]},\n" +
//            "    { \"type\": \"room\",\n" +
//            "    \"origin\": [ 0, 4 ],\n" +
//            "    \"bounds\": { \"rows\": 1, \"columns\": 3 },\n" +
//            "    \"layout\": [ [2, 1, 1] ] } ],\n" +
//            "\n" +
//            "    \"objects\": [{ \"type\": \"key\", \"position\": [0 , 1] },\n" +
//            "    { \"type\": \"exit\", \"position\": [ 0, 5 ] } ],\n" +
//            "    \"hallways\": [ { \"type\": \"hallway\",\n" +
//            "    \"from\": [ 0, 2 ],\n" +
//            "    \"to\": [ 0, 4 ],\n" +
//            "    \"waypoints\": [ ] }]\n" +
//            "    }";
//
//    @Test
//    public void testRegister() {
//        PlayerController pc = new PlayerController("player1");
//        GameManager gameManager = new GameManager();
//        assertEquals(0, gameManager.getPlayers().length);
//        gameManager.acceptCommand(pc.commandRegister());
//        assertEquals(1, gameManager.getPlayers().length);
//
//        //invalid command, no change (+ logged in stdout) cannot register more than one player of same name
//        gameManager.acceptCommand(pc.commandRegister());
//        assertEquals(1, gameManager.getPlayers().length);
//
//        PlayerController pc1 = new PlayerController("player2");
//        gameManager.acceptCommand(pc1.commandRegister());
//        assertEquals(2, gameManager.getPlayers().length);
//        assertEquals("player1", gameManager.getPlayers()[0].getName());
//        assertEquals("player2", gameManager.getPlayers()[1].getName());
//
//        AdversaryController ad = new GhostController("ghost1");
//        gameManager.acceptCommand(ad.commandRegister());
//        assertEquals(1, gameManager.getAdversaries().length);
//        assertEquals("ghost1", gameManager.getAdversaries()[0].getName());
//
//        AdversaryController ad1 = new GhostController("ghost2");
//        gameManager.acceptCommand(ad1.commandRegister());
//        assertEquals(2, gameManager.getAdversaries().length);
//        assertEquals("ghost1", gameManager.getAdversaries()[0].getName());
//        assertEquals("ghost2", gameManager.getAdversaries()[1].getName());
//
//        Observer observer = new Observer("observer");
//        gameManager.acceptCommand(observer.commandRegister());
//
//        assertEquals(gameManager.getPlayers().length, gameManager.getPlayerControllers().length);
//        assertEquals("player1", gameManager.getPlayerControllers()[0].getName());
//        assertEquals("player2", gameManager.getPlayerControllers()[1].getName());
//
//        assertEquals(gameManager.getAdversaries().length, gameManager.getAdversaryControllers().length);
//        assertEquals("ghost1", gameManager.getAdversaries()[0].getName());
//        assertEquals("ghost2", gameManager.getAdversaries()[1].getName());
//
//        assertEquals(1, gameManager.getObservers().length);
//        assertEquals("observer", gameManager.getObservers()[0].getName());
//    }
//
//    @Test
//    public void badAdversaryTester() {
//        PlayerController pc = new PlayerController("player1");
//        GameManager gameManager = new GameManager();
//        assertEquals(0, gameManager.getPlayers().length);
//        gameManager.acceptCommand(pc.commandRegister());
//        assertEquals(1, gameManager.getPlayers().length);
//
//        //invalid command, no change (+ logged in stdout) cannot register more than one player of same name
//        gameManager.acceptCommand(pc.commandRegister());
//        assertEquals(1, gameManager.getPlayers().length);
//
//        PlayerController pc1 = new PlayerController("player2");
//        gameManager.acceptCommand(pc1.commandRegister());
//        assertEquals(2, gameManager.getPlayers().length);
//        assertEquals("player1", gameManager.getPlayers()[0].getName());
//        assertEquals("player2", gameManager.getPlayers()[1].getName());
//
//        AdversaryController ad = new GhostController("ghost1");
//        gameManager.acceptCommand(ad.commandRegister());
//        assertEquals(1, gameManager.getAdversaries().length);
//        //assertEquals("ghost1", gameManager.getAdversaries()[0].getName());
//
//        //invalid add adversary of same name
//        AdversaryController bad = new GhostController("ghost1");
//        gameManager.acceptCommand(bad.commandRegister());
//        assertEquals(1, gameManager.getAdversaries().length);
//    }
//
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
//    }
//
//    //Only one invalid move should be moving player 1 left (shown with comment)
//    @Test
//    public void testMoveInteract() throws IOException {
//        PlayerController pc = new PlayerController("player1");
//        PlayerController pc1 = new PlayerController("player2");
//        AdversaryController ad = new GhostController("ghost1");
//        AdversaryController ad1 = new GhostController("ghost2");
//        Observer observer = new Observer("observer");
//        GameManager gameManager = new GameManager();
//        gameManager.acceptCommand(pc.commandRegister());
//        gameManager.acceptCommand(pc1.commandRegister());
//        gameManager.acceptCommand(ad.commandRegister());
//        gameManager.acceptCommand(ad1.commandRegister());
//        gameManager.acceptCommand(observer.commandRegister());
//
//
//        Level level = TestParser.parseLevel(JsonParser.parseReader(new StringReader(LEVEL)).getAsJsonObject());
//        gameManager.acceptCommand(pc.commandInitialize(new Level[]{level}));
//        assertNotNull(pc.getUpdate());
//        assertNotNull(pc1.getUpdate());
//        assertNotNull(observer.getUpdate());
//        assertEquals(gameManager.getGameState().getLevel().toString(), observer.getUpdate().getLevel());
////
////        Posn pcPosn = pc.getUpdate().getPosition();
////        gameManager.acceptCommand(pc.commandMove(Direction.UP));
////        assertNull(gameManager.getGameState().getLevel().tileAt(pcPosn).getEntity());
//
//        assertEquals(EntityStatus.ALIVE, pc.getUpdate().getStatus());
//        assertNull(ad.getUpdate());
//        assertNull(ad1.getUpdate());
//        assertEquals(gameManager.getGameState().getLevel().toString(), observer.getUpdate().getLevel());
//
//        gameManager.acceptCommand(pc.commandMove(Direction.DOWN));
////        Update update = gameManager.getPlayerControllers()[0].getUpdate();
////        gameManager.acceptCommand(pc.commandMove(Direction.LEFT)); //INVALID COMMAND
////        assertEquals(update, gameManager.getPlayerControllers()[0].getUpdate()); //no new update received
//    }
//}
//
////    @Test
////    public void testPlayerIntoAdversary() throws IOException {
////        Level level = TestParser.parseLevel(JsonParser.parseReader(new StringReader(SMALL_LEVEL)).getAsJsonObject());
////        PlayerController pc = new PlayerController("player1");
////        PlayerController pc1 = new PlayerController("player2");
////        AdversaryController ad = new GhostController("ghost1");
////        AdversaryController ad1 = new GhostController("ghost2");
////
////        GameManager gameManager = new GameManager();
////
////        assertEquals("□dhd□■\n" +
////                "■■■■■■\n", level.toString());
////        gameManager.acceptCommand(ad.commandRegister());
////        gameManager.acceptCommand(pc.commandRegister());
////        //Initialize spit out some "Illegal initial placement" in the console as attempt to place in door tile until can in placeable tile
////        gameManager.acceptCommand(pc.commandInitialize(new Level[]{level}));
////        assertEquals("PdhdA■\n" +
////                "■■■■■■\n", gameManager.getGameState().getLevel().toString());
////
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        gameManager.acceptCommand(ad.commandMove(Direction.LEFT));
////
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        assertEquals(EntityStatus.DEAD, gameManager.getGameState().getPlayers()[0].getStatus());
////        assertEquals(EntityStatus.DEAD, gameManager.getPlayerControllers()[0].getUpdate().getStatus());
////        assertEquals(new Posn(0, 2), gameManager.getPlayerControllers()[0].getUpdate().getPosition());
////        assertEquals("■■■■■\n" + "■■■■■\n" + "□dhA□\n" + "■■■■■\n" + "■■■■■\n", gameManager.getPlayerControllers()[0].getUpdate().getLevel());
////        Update update = gameManager.getPlayerControllers()[0].getUpdate();
////        assertEquals(new Posn(0, 2), gameManager.getGameState().getPlayers()[0].getPosn());
////        assertNull(gameManager.getGameState().getLevel().tileAt(new Posn(0, 2)).getEntity());
////
////        assertEquals(EntityStatus.ALIVE, gameManager.getGameState().getAdversaries()[0].getStatus());
////        assertEquals(new Posn(0, 3), gameManager.getGameState().getAdversaries()[0].getPosn());
////        assertEquals(gameManager.getGameState().getAdversaries()[0], gameManager.getGameState().getLevel().tileAt(new Posn(0, 3)).getEntity());
////
////        assertEquals("□dhA□■\n" +
////                "■■■■■■\n", gameManager.getGameState().getLevel().toString());
////
////        gameManager.acceptCommand(ad.commandMove(Direction.LEFT));
////        assertEquals(update, gameManager.getPlayerControllers()[0].getUpdate()); //dead player does not receive updates
////    }
////
////    @Test
////    public void testAdversaryIntoPlayer() throws IOException {
////        Level level = TestParser.parseLevel(JsonParser.parseReader(new StringReader(SMALL_LEVEL_1)).getAsJsonObject());
////        PlayerController pc = new PlayerController("player1");
////        GameManager gameManager = new GameManager();
////
////        assertEquals("□KdhdE□■\n" +
////                "■■■■■■■■\n", level.toString());
////        AdversaryController ad = new GhostController("ghost1");
////
////        gameManager.acceptCommand(ad.commandRegister());
////        gameManager.acceptCommand(pc.commandRegister());
////        //Initialize spit out some "Illegal initial placement" in the console as attempt to place in door tile until can in placeable tile
////        gameManager.acceptCommand(pc.commandInitialize(new Level[]{level}));
////        assertEquals("PKdhdEA■\n" +
////                "■■■■■■■■\n", gameManager.getGameState().getLevel().toString());
////
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT)); //walk onto key
////        assertFalse(gameManager.getGameState().getIsLocked());
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        gameManager.acceptCommand(ad.commandMove(Direction.STAY));
////
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////        gameManager.acceptCommand(ad.commandMove(Direction.STAY));
////
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT)); //walk onto unlocked exit
////        assertNull(gameManager.getGameState().getLevel().tileAt(new Posn(0, 5)).getEntity());
////
////        //should be invalid player move
////        gameManager.acceptCommand(pc.commandMove(Direction.RIGHT));
////    }
////}
//
////  //Exaples of a single level:
////  /* Tile[8][5]
////
////       01234567
////    r0 XXOOXXXX
////    r1 XXO>-<OE
////    r2 XXOOXO>O
////    r3 XXXKOO|X
////    r4 XXXOO>|X
////
////   */
////
////
////  Tile[] ROW0 = {new WallTile(), new WallTile(), new FloorTile(), new FloorTile(), new WallTile(),
////      new WallTile(), new WallTile(), new WallTile()};
////  Tile[] ROW1 = {new WallTile(), new WallTile(), new FloorTile(), new DoorTile(),
////      new HallwayTile(), new DoorTile(), new FloorTile(), new FloorTile().placeItem(ItemType.EXIT)};
////  Tile[] ROW2 = {new WallTile(), new WallTile(), new FloorTile(), new FloorTile(), new WallTile(),
////      new FloorTile(), new DoorTile(), new FloorTile()};
////  Tile[] ROW3 = {new WallTile(), new WallTile(), new WallTile(), new FloorTile().placeItem(ItemType.KEY),
////      new FloorTile(), new FloorTile(), new HallwayTile(), new WallTile()};
////  Tile[] ROW4 = {new WallTile(), new WallTile(), new WallTile(), new FloorTile(), new FloorTile(),
////      new DoorTile(), new HallwayTile(), new WallTile()};
////  Tile[][] TILES = {ROW0, ROW1, ROW2, ROW3, ROW4};
////  Level LEVEL = new Level(TILES);
////
////  int NUMPLAYERS = 3; //player id : 1,2,3
////  int NUMADVERSARY = 5; //adversary id : 5,6,7,8,9
////
////  Posn p02 = new Posn(0, 2);
////  Posn p03 = new Posn(0, 3);
////  Posn p12 = new Posn(1, 2);
////  Posn p45 = new Posn(4, 5);
////  Posn p44 = new Posn(4, 4);
////  Posn p43 = new Posn(4, 3);
////  Posn p35 = new Posn(3, 5);
////  Posn p34 = new Posn(3, 4);
////  Posn key = new Posn(3, 3);
////
////  Direction UP;
////  Direction LEFT;
////
////  Player playerKilled1 = new Player(1, p44);
////  Player playerFindKey = new Player(3, key);
////  Player playerKilled2 = new Player(2, p03);
////  Adversary adversaryKillPayer2 = new Adversary(6, p03);
////
////
////  //TEST INITIALIZAION OF GAME
////  GameState gs = new GameState();
////  IGameManager gm = new gameManager(gs, LEVEL);
////  IGameState gameState;
////  IGameState intermediateGS;
////
////  @Before
////  public void initialGame() {
////    assertEquals(0, gm.getPlayers().size());
////    gm.registerPlayer("David");
////    gm.registerPlayer("Tom");
////    gm.registerPlayer("Sophia");
////    assertEquals(0, gm.getAdversaries().size());
////    gm.registerAdversary(5);
////
////  }
////
////  @Test
////  public void testRegisterPlayer() {
////    assertEquals(3, gm.getPlayers().size());
////  }
////
////  @Test(expected = IllegalArgumentException.class)
////  public void testInvalidRegisterPlayer() {
////    gm.registerPlayer("Tom2");
////    gm.registerPlayer("Sophia2");
////  }
////
////  @Test
////  public void testRegisterAdversary() {
////    assertEquals(5, gm.getAdversaries().size());
////  }
////
////  @Test
////  public void testStartGameState() {
////    IGameState newGameState = gm.startGame();
////    assertEquals(3, newGameState.getPosnPlayer().size());
////    assertEquals(5, newGameState.getPosnAdversary().size());
////  }
////
////  @Test
////  public void testSuperviseGame() {
////    gm.startGame();
////    IGameState newGameState = gm.superviseGame();
////    assertEquals(3, newGameState.getPosnPlayer().size());
////    assertEquals(5, newGameState.getPosnAdversary().size());
////  }
////
////  //todo more tests for accurate posn of player and adversray
////
////  @Test
////  public void testUpdatePlayerPosn() {
////    gm.startGame();
////    IGameState newGameState = gm.superviseGame();
////    gm.updatePlayerPosn();
////    assertEquals(p02, gm.getPlayers().get(0).getPosn());
////    assertEquals(p03, gm.getPlayers().get(1).getPosn());
////  }
////
//////  @Test
//////  public void testUpdatePlayerStatus() {
//////    gm.startGame();
//////    IGameState newGameState = gm.superviseGame();
//////    Player p = gm.getPlayers().get(0);
//////    Tile[][] tile = newGameState.getLevel().getTiles();
//////    gm.interact(p, tile[1][2]);
//////    assertEquals(EntityStatus.ALIVE, p.getStatus());
//////  }
////
////
////}