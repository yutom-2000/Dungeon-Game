package testHarness.Level.TestHarness.StateTest;

import Game.gameState.GameState;
import Game.gameState.entities.Ghost;
import Game.gameState.entities.Zombie;
import Game.gameState.interaction.item.Item;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.Level;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.Player;
import Game.gameState.level.generation.Hallway;
import Game.gameState.level.generation.Room;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Parser for a state test This one actually parses to our actual game's objects this time...
 */
public class TestParser {

  /**
   * Top level parser
   *
   * @param input jsonReader input
   * @return TestHarnessInput representation of the input
   * @throws IOException if input is malformed in some way
   */
  public static StateTestInput parseAll(JsonArray input) throws IOException {
    JsonObject gameStateObject = input.get(0).getAsJsonObject();
    String name = input.get(1).getAsString();
    JsonArray jsonPosition = input.get(2).getAsJsonArray();

    GameState gamestate = parseGameState(gameStateObject);
    Posn position = parsePosn(jsonPosition);

    return new StateTestInput(gamestate, name, position);
  }

  public static GameState parseGameState(JsonObject gameStateObject) throws IOException {
    JsonArray playerArray = gameStateObject.get("players").getAsJsonArray();
    JsonArray adversaryArray = gameStateObject.get("adversaries").getAsJsonArray();
    JsonObject levelObject = gameStateObject.get("level").getAsJsonObject();

    Level level = parseLevel(levelObject);

    GameState gamestate = new GameState(level);
    if (!(gameStateObject.get("exit-locked")
        .getAsBoolean())) { //if exit-locked = false, call unlock
      gamestate.unlockExit();
    }
    int i = 0; //rolling iterator for adding players/adversaries to use for ID
    for (JsonElement e : playerArray) {
      JsonObject playerObj = e.getAsJsonObject();
      Player p = new Player(playerObj.get("name").getAsString());
      Posn pPosn = parsePosn(playerObj.get("position").getAsJsonArray());
      gamestate.placeEntity(p, pPosn);
      gamestate.addEntity(p);
      i++;
    }
    for (JsonElement e : adversaryArray) {
      JsonObject adversaryObj = e.getAsJsonObject();
      //Adversary adversary = new Adversary(i);
      Adversary adversary = randomAssignAdversary(i);
      Posn aPosn = parsePosn(adversaryObj.get("position").getAsJsonArray());
      gamestate.placeEntity(adversary, aPosn);
      gamestate.addEntity(adversary);
      i++;
    }
    return gamestate;
  }

  /**
   * Parses a test level
   *
   * @param o json Object
   * @return tLevel object representing the level
   * @throws IOException jsonObject is not of type level
   */
  public static Level parseLevel(JsonObject o) throws IOException {
    if (!(o.get("type").getAsString().equals("level"))) {
      throw new IOException("JsonObject is not of type level");
    }

    JsonArray jsonRooms = o.get("rooms").getAsJsonArray();
    Room[] rooms = new Room[jsonRooms.size()];

    for (int i = 0; i < jsonRooms.size(); i++) {
      rooms[i] = parseRoom(jsonRooms.get(i).getAsJsonObject());
    }

    JsonArray jsonHallways = o.get("hallways").getAsJsonArray();
    Hallway[] hallways = new Hallway[jsonHallways.size()];

    for (int i = 0; i < jsonHallways.size(); i++) {
      hallways[i] = parseHallway(jsonHallways.get(i).getAsJsonObject());
    }

    JsonArray jsonObjects = o.get("objects").getAsJsonArray();
    Item[] items = new Item[jsonObjects.size()];

    for (int i = 0; i < jsonObjects.size(); i++) {
      items[i] = parseObject(jsonObjects.get(i).getAsJsonObject());
    }

    return new Level(rooms, hallways, items);
  }

  /**
   * Parse a single tObject from a jsonObject
   *
   * @param o jsonObject
   * @return tObject representation
   * @throws IOException if object given is not a valid json representation of a tObject
   */
  public static Item parseObject(JsonObject o) throws IOException {
    String type = o.get("type").getAsString();
    if (!(type.equals("key") || type.equals("exit"))) {
      throw new IOException("Must be a valid object type");
    }

    ItemType typeEnum = ItemType.NONE.NONE;
    switch (type) {
      case "key":
        typeEnum = ItemType.KEY;
        break;
      case "exit":
        typeEnum = ItemType.EXIT;
        break;
    }

    return new Item(typeEnum, parsePosn(o.get("position").getAsJsonArray()));
  }

  /**
   * Parse a single posn from a jsonArray
   *
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
  public static Hallway parseHallway(JsonObject o) throws IOException {
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

    return new Hallway(from, to, waypoints);
  }

  /**
   * Returns a room object
   *
   * @param o JsonObject must be of type room
   * @return room
   * @throws IOException json provided is not well-provided room
   */
  public static Room parseRoom(JsonObject o) throws IOException {
    if (!(o.get("type").getAsString().equals("room"))) {
      throw new IOException("Must be room json object");
    }

    int originRow = o.get("origin").getAsJsonArray().get(0).getAsInt();
    int originCol = o.get("origin").getAsJsonArray().get(1).getAsInt();
    Posn origin = new Posn(originRow, originCol);

    JsonObject bounds = o.get("bounds").getAsJsonObject();
    int roomRows = bounds.get("rows").getAsInt();
    int roomColumns = bounds.get("columns").getAsInt();

    Tile[][] layout = new Tile[roomRows][roomColumns];
    JsonArray layoutArray = o.get("layout").getAsJsonArray();

    for (int y = 0; y < roomRows; y++) {
      for (int x = 0; x < roomColumns; x++) {
        layout[y][x] = get2dJsonArray(layoutArray, y, x);
      }
    }
    return new Room(origin, roomRows, roomColumns, layout);
  }

  /**
   * Helper method to use json array like a 2d array
   *
   * @param array json array
   * @param row   row index
   * @param col   col index
   * @return
   */
  private static Tile get2dJsonArray(JsonArray array, int row, int col) {
    int type = array.get(row).getAsJsonArray().get(col).getAsInt();
    if (type == 0) {
      return new Tile(TileType.WALL);
    } else if (type == 1) {
      return new Tile(TileType.FLOOR);
    } else if (type == 2) {
      return new Tile(TileType.DOOR);
    } else {
      throw new IllegalArgumentException("Unrecognized tile type: " + type);
    }
  }

  private static Adversary randomAssignAdversary(int i) {
    if (i % 2 == 0) {
      return new Zombie(i);
    } else {
      return new Ghost(i);
    }
  }

}
