package Game.controllers.playerController;

import Game.controllers.Command.Command;
import Game.controllers.Command.CommandMove;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.interaction.MoveResult;
import com.google.gson.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Class representing a networked player's controller Hold on the server, to keep connection
 * information to the player's client
 */
public class ServerPlayerController extends PlayerController {

  Socket socket;
  DataOutputStream outputStream;
  DataInputStream inputStream;

  /**
   * Constructor for network PlayerController
   *
   * @param name   the name of the Player
   * @param socket the connected socket
   * @throws IOException the possible exceptions
   */
  public ServerPlayerController(String name, Socket socket) throws IOException {
    super(name);
    this.socket = socket;
    outputStream = new DataOutputStream(socket.getOutputStream());
    inputStream = new DataInputStream(socket.getInputStream());
  }


  //send this update down out this socket
  @Override
  public void receiveUpdate(Update update) {
    try {
      outputStream.writeUTF(update.toString());
      outputStream.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  public Command promptMove() {
    try {
      //Write message to output stream and send through socket
      outputStream.writeUTF("move"); // writes to output stream
      outputStream.flush(); // sends through socket
      String command = inputStream.readUTF();
      return CommandMove.parseCommandMove(command, name, true); //true because is always player

    } catch (IOException e) {
      System.out.println(e);
    }
    return promptMove();
  }

  @Override
  public void notifyEndLevel(NotifyEndLevel notice) {
    JsonObject message = new JsonObject();
    message.addProperty("type", "end-level");
    message.addProperty("key", notice.keyHolderName);
    JsonArray exited = new JsonArray();
    for (String name : notice.exitedNames) {
      exited.add(name);
    }
    JsonArray ejected = new JsonArray();
    for (String name : notice.ejectedNames) {
      ejected.add(name);
    }
    message.add("exits", exited);
    message.add("ejects", ejected);

    Gson gson = new Gson();
    try {
      outputStream.writeUTF(gson.toJson(message));
      outputStream.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  public void notifyLoadLevel(JsonObject message) {
    Gson gson = new Gson();
    try {
      outputStream.writeUTF(gson.toJson(message));
      outputStream.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  public void notifyGameOver(PlayerController[] playerControllers) {
    JsonObject gameOverNotif = new JsonObject(); gameOverNotif.addProperty("type", "end-game");
    JsonArray scores = new JsonArray();
    for (PlayerController p : playerControllers) {
      JsonObject jo = new JsonObject();
      jo.addProperty("type", "player-score");
      jo.addProperty("name", name);
      jo.addProperty("exits", numTimesExit);
      jo.addProperty("ejects", numTimesDie);
      jo.addProperty("keys", numTimesKey);
      scores.add(jo);
    }
    gameOverNotif.add("scores", scores);
    try {
      Gson gson = new Gson();
      outputStream.writeUTF(gson.toJson(gameOverNotif));
      outputStream.flush();
      socket.close();
    } catch (IOException e) {
      System.out.print(e);
    }
  }

  @Override
  public void receiveResult(MoveResult result) {
    try {
      outputStream.writeUTF(result.toString());
      outputStream.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

}
