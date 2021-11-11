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

/**
 * Class for client
 */
public class Client {

  /**
   * Locally verify that an input is a well-formed posn, then turn into a json
   *
   * @return the string of the move message from json input
   */
  public static String getMoveJson() {
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

  //some method to render given the tiles and positions of stuff
  private static void render(JsonObject update, Posn playerPosn) {
    JsonArray tiles = update.get("layout").getAsJsonArray();
    //create int[][] layout
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

    //create ActorPosition[] actorpositions
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

    //create ObjectPosition[] objectPositions
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
            objectPositions.toArray(new ObjectPosition[objectPositions.size()]),
            new Posn(playerPosn.getRow() - 2, playerPosn.getCol() - 2));

    System.out.println(toPrint);
  }

  /**
   * Handle the registration process
   *
   * @param data         the client data
   * @param outputStream the data output
   * @param inputStream  the data input
   * @throws IOException the exception for invalid arguments
   */
  public static void handleName(ClientData data, DataOutputStream outputStream,
      DataInputStream inputStream) throws IOException {
    System.out.println("Enter your name: ");

    Scanner scan = new Scanner(System.in);

    data.name = scan.nextLine();

    JsonObject reqRegister = new JsonObject();
    reqRegister.addProperty("type", "register-player");
    reqRegister.addProperty("name", data.name);
    reqRegister.addProperty("entity-type", "player");
    outputStream.writeUTF(reqRegister.toString());
    outputStream.flush();
  }

  public static void main(String[] args) throws IOException {
    ArgumentParser parser = ArgumentParsers.newFor("Client").build();

    parser.addArgument("--address").setDefault("127.0.0.1");
    parser.addArgument("--port").type(Integer.class).setDefault(45678);

    String address = "";
    int port = 0;

    try {
      Namespace ns = parser.parseArgs(args);
      address = ns.getString("address");
      port = ns.getInt("port");

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

    String name = "";
    EntityStatus status = EntityStatus.ALIVE; //know u initialize to being alive
    String welcome = inputStream.readUTF();

    JsonObject greeting = JsonParser.parseString(welcome).getAsJsonObject();
    System.out.println(greeting.toString());

    ClientData cd = new ClientData();
    cd.playGame(socket, inputStream, outputStream);
  }
}
