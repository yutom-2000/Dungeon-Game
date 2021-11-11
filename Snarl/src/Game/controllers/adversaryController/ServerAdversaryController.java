package Game.controllers.adversaryController;

import Game.controllers.Command.Command;
import Game.controllers.Command.CommandRegisterNetworkAdversary;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.entities.EntityType;
import Game.gameState.interaction.MoveResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Game.controllers.Command.CommandMove.parseCommandMove;

/**
 * AdversaryController for server to hold
 */
public class ServerAdversaryController extends AdversaryController {

  Socket socket;
  DataOutputStream outputStream;
  DataInputStream inputStream;

  /**
   * Constructor for the NETWORK controller of Adversary
   *
   * @param name name of the Adversary
   * @param type types of the Adversary
   */
  public ServerAdversaryController(String name, EntityType type, Socket socket) throws IOException {
    super(name, type);
    this.socket = socket;
    this.outputStream = new DataOutputStream(socket.getOutputStream());
    this.inputStream = new DataInputStream(socket.getInputStream());
  }

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
      outputStream.writeUTF("move");
      outputStream.flush();
      String command = inputStream.readUTF();
      return parseCommandMove(command, name, false); //false because is always adversary
    } catch (IOException e) {
      System.out.println(e);
      System.exit(-1);
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
  public void notifyGameOver(PlayerController[] playerControllers) {
    JsonObject gameOverNotif = new JsonObject(); gameOverNotif.addProperty("type", "end-game");
    JsonArray scores = new JsonArray();
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
  public void notifyNotRegistered() {
    try {
      outputStream.writeUTF("Not registered for this level, " + this.type + " is full.");
      outputStream.flush();
    } catch (IOException e) {
      System.out.println(e);
      System.exit(-1);
    }

  }

  @Override
  public Command commandRegister() {
    return new CommandRegisterNetworkAdversary(name, this, false, type);
  }
}
