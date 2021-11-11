package Game;

import Game.controllers.Command.CommandMove;
import Game.controllers.Observer;
import Game.gameManager.Updates.Update;
import Game.gameState.ActorPosition;
import Game.gameState.ObjectPosition;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.EntityType;
import Game.gameState.interaction.MoveResult;
import Game.gameState.interaction.item.ItemType;
import Game.gameState.level.tiles.Tile;
import Game.gameState.level.tiles.TileType;
import Game.gameState.posn.Posn;
import com.google.gson.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoteAdversaryClient {

  String name;
  EntityStatus status;

  /**
   * Constructors for remote adversary client
   */
  public RemoteAdversaryClient() {
    name = "";
    status = EntityStatus.ALIVE;
  }

  /**
   * Locally verify that an input is a well-formed posn, then turn into a json
   *
   * @return the string of the move command according to the given Json input
   */
  private static String getMoveJson() {
    System.out.println("Input move: ");
    Scanner scan = new Scanner(System.in);

    String s = scan.nextLine();
    try {
      JsonArray posn = JsonParser.parseString(s).getAsJsonArray();
      Posn dest = TestParser.parsePosn(posn);
      //if did not throw exception we know the supplied input is some variety of valid posn
      JsonObject moveObject = new JsonObject();
      moveObject.addProperty("type", "move");
      moveObject.add("to", posn);
      Gson gson = new Gson();
      return gson.toJson(moveObject);

    } catch (IOException e) {
      System.out.println(e);
    } catch (Exception e) { //catch misc json parsing exceptions
      System.out.println(e);
      return getMoveJson();
    }
    return null;
  }

  /**
   * Show contents of this update
   *
   * @param update the update of the gameState
   */
  public static void handleUpdate(JsonObject update, String name, EntityStatus status)
      throws IOException {
    JsonArray actors = update.get("actors").getAsJsonArray();
    for (JsonElement je : actors) {
      JsonObject actor = je.getAsJsonObject();
      if (actor.get("name").getAsString().equals(name)) { //all good we're still alive
        System.out.println("status: " + status);
        System.out.println("position: " + update.get("position"));
        Posn posn = TestParser.parsePosn(update.get("position").getAsJsonArray());

        render(update, posn);
        return;
      }
    }
    if (status == EntityStatus.ALIVE) {
      status = EntityStatus.DEAD;
    }
  }

  /**
   * Render the current adversary client
   *
   * @param update     the update of the gameState
   * @param playerPosn the position of a player
   */
  private static void render(Update update, Posn playerPosn) {
    //create int[][] layout
    int[][] layout = update.getTileLayout();

    //create ActorPosition[] actorpositions
    ActorPosition[] actorPositions = update.actorPositionList;

    //create ObjectPosition[] objectPositions
    ObjectPosition[] objectPositions = update.objectPositionList;

    String toPrint = Observer.layoutToString(layout, actorPositions,
        objectPositions, playerPosn);

    System.out.println(toPrint);
  }

  /**
   * Render the current adversary client
   *
   * @param update     the update of the gameState
   * @param playerPosn the position of a player
   */
  public static void render(JsonObject update, Posn playerPosn) {
    JsonArray tiles = update.get("layout").getAsJsonArray();
    //step 1: create int[][] layout
    int[][] layout = new int[tiles.size()][tiles.get(0).getAsJsonArray().size()];
    int rowIter = 0;
    for (JsonElement je : tiles) {
      int colIter = 0;
      for (JsonElement jje : je.getAsJsonArray()) {
        int i = jje.getAsInt();
        layout[rowIter][colIter] = i;
        colIter++;
      }
      rowIter++;
    }

    //step 2: create ActorPosition[] actorpositions
    ArrayList<ActorPosition> actorPositions = new ArrayList<>();
    for (JsonElement je : update.get("actors").getAsJsonArray()) {
      JsonObject jo = je.getAsJsonObject();
      EntityType type = EntityType.PLAYER;
      switch (jo.get("type").getAsString()) {
        case "ZOMBIE":
          type = EntityType.ZOMBIE;
          break;
        case "GHOST":
          type = EntityType.GHOST;
          break;
        case "PLAYER":
          type = EntityType.PLAYER;
          break;
      }
      int row = jo.get("position").getAsJsonObject().get("row").getAsInt();
      int col = jo.get("position").getAsJsonObject().get("col").getAsInt();
      ActorPosition toAppend = new ActorPosition(type, jo.get("name").getAsString(),
          new Posn(row, col));
      actorPositions.add(toAppend);
    }

    //step 3: create ObjectPosition[] objectPositions
    ArrayList<ObjectPosition> objectPositions = new ArrayList<>();
    for (JsonElement je : update.get("objects").getAsJsonArray()) {
      JsonObject jo = je.getAsJsonObject();
      ItemType type = ItemType.NONE;
      switch (jo.get("type").getAsString()) {
        case "KEY":
          type = ItemType.KEY;
          break;
        case "EXIT":
          type = ItemType.EXIT;
          break;
      }
      int row = jo.get("position").getAsJsonObject().get("row").getAsInt();
      int col = jo.get("position").getAsJsonObject().get("col").getAsInt();
      ObjectPosition op = new ObjectPosition(type, new Posn(row, col));
      objectPositions.add(op);
    }

    String toPrint = Observer
        .layoutToString(layout, actorPositions.toArray(new ActorPosition[actorPositions.size()]),
            objectPositions.toArray(new ObjectPosition[objectPositions.size()]), new Posn(0, 0));

    System.out.println(toPrint);
  }

  /**
   * Handle the method for start the level
   *
   * @param message the json input
   * @param status  the entity status
   */
  public static void handleStartLevel(JsonObject message, EntityStatus status) {
    status = EntityStatus.ALIVE;
    System.out.println("Starting level: " + message.get("level")); //show level number
    System.out.println("Players: " + message.get("players"));
  }

  /**
   * Handle the method for exit the level
   *
   * @param message the json input
   */
  public static void handleEndLevel(JsonObject message) {
    System.out.println("Level ended");
    System.out.println("Key holder: " + message.get("key").getAsString());
    System.out.print("Exits: ");
    for (JsonElement je : message.get("exits").getAsJsonArray()) {
      System.out.print(je.getAsString());
      System.out.print(", ");
    }
    System.out.print("\n");
    System.out.print("Ejects: ");
    for (JsonElement je : message.get("ejects").getAsJsonArray()) {
      System.out.print(je.getAsString());
      System.out.print(", ");
    }
  }

  /**
   * Handle for the end game
   *
   * @param message the json input
   */
  public static void handleEndGame(JsonObject message) {
    System.out.println("Game over!");
    for (JsonElement je : message.get("scores").getAsJsonArray()) {
      JsonObject entry = je.getAsJsonObject();
      System.out
          .println(entry.get("name").getAsString() + " exited " + entry.get("exits").getAsInt()
              + " ejected " + entry.get("ejects").getAsInt() + " keys " + entry.get("keys")
              .getAsInt());
    }
  }

  /**
   * Handle the registration process
   *
   * @param outputStream the data output
   * @param inputStream  the data input
   * @throws IOException the exception for invalid arguments
   */
  public void handleName(DataOutputStream outputStream, DataInputStream inputStream, String type)
      throws IOException {
    System.out.println("Enter your name: ");
    Scanner scan = new Scanner(System.in);

    name = scan.nextLine();
    JsonObject reqRegister = new JsonObject();
    reqRegister.addProperty("type", "register-adversary");
    reqRegister.addProperty("name", name);
    reqRegister.addProperty("entity-type", type);
    outputStream.writeUTF(reqRegister.toString());
    outputStream.flush();
  }

  private void playGame(Socket socket, DataInputStream inputStream, DataOutputStream outputStream,
      String type) throws IOException {
    while (true) { //listen loop while the connection is active
      String m = inputStream.readUTF();
      System.out.println(m);
      if (m.equals("name")) {
        handleName(outputStream, inputStream, type);
        continue;
      }
      if (m.equals("move")) {
        String move = getMoveJson();
        outputStream.writeUTF(move);
        outputStream.flush();
        continue;
      }

      if (m.equals(MoveResult.OK.toString())) {
        continue;
      }
      if (m.equals(MoveResult.INVALID.toString())) {
        System.out.println(m);
        continue; //out implementation the server will re-prompt with "move"
      }
      if (m.equals(MoveResult.EJECT.toString())) {
        System.out.println(m);
        status = EntityStatus.DEAD;
        continue;
      }
      if (m.equals(MoveResult.EXIT.toString())) {
        System.out.println(m);
        status = EntityStatus.EXITED;
        continue;
      }
      if (m.equals(MoveResult.KEY.toString())) {
        System.out.println(m);
        continue;
      }

      //otherwise is some variety of jsonobject to display or some shit
      JsonObject message = JsonParser.parseString(m).getAsJsonObject();
      if (message.get("type").getAsString().equals("player-update")) {
        handleUpdate(message, name, status);
        continue;
      }

      if (message.get("type").getAsString().equals("end-level")) {
        handleEndLevel(message);
        continue;
      }
      if (message.get("type").getAsString().equals("start-level")) {
        handleStartLevel(message, status);
        continue;
      }
      if (message.get("type").getAsString().equals("end-game")) {
        handleEndGame(message);
        break; //get out of here
      }
    }
    socket.close();
    System.exit(0);
  }

  public static void main(String[] args) throws IOException {
    ArgumentParser parser = ArgumentParsers.newFor("Client").build();

    parser.addArgument("--address").setDefault("127.0.0.1");
    parser.addArgument("--port").type(Integer.class).setDefault(45678);
    parser.addArgument("--type").type(String.class).setDefault("ZOMBIE");

    String address = "", type = "";
    int port = 0;

    try {
      Namespace ns = parser.parseArgs(args);
      address = ns.getString("address");
      port = ns.getInt("port");
      type = ns.getString("type");


    } catch (ArgumentParserException e) {
      System.out.println(e);
      System.exit(-1);
    }

    //System.out.println(address); System.out.println(port); //for debugging

    Socket socket = null;
    try {
      socket = new Socket(address, port); //establish tcp connection
    } catch (ConnectException e) {
      System.out.println("Ensure the server is already running");
      System.exit(-1);
    }
    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

    String poo = inputStream.readUTF();
    System.out.println(poo);

    JsonObject greeting = JsonParser.parseString(poo).getAsJsonObject();
    System.out.println(greeting.toString());

    RemoteAdversaryClient rc = new RemoteAdversaryClient();
    rc.playGame(socket, inputStream, outputStream, type);

  }
}

