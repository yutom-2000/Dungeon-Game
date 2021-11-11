package Game;

import Game.gameState.entities.EntityStatus;
import Game.gameState.interaction.MoveResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static Game.Client.*;

/**
 * Store some data in a client
 */
public class ClientData {

  public String name;
  public EntityStatus status;

  /**
   * Constructor for the client data
   */
  public ClientData() {
    name = "";
    status = EntityStatus.ALIVE;
  }


  /**
   * Play the game according to corresponding command given through the parameters
   *
   * @param socket       the socket of the game
   * @param inputStream  the data input
   * @param outputStream the data output
   * @throws IOException invalid arguments will throw the IOException
   */
  public void playGame(Socket socket, DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    while (true) { //listen loop while the connection is active
      String m = inputStream.readUTF();
      System.out.println(m);
      if (m.equals("name")) {
        handleName(this, outputStream, inputStream);
        continue;
      }
      if (m.equals("move")) {
        String move = Client.getMoveJson();
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
}
