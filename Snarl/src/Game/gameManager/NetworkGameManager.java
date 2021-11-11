package Game.gameManager;

import Game.controllers.Command.Command;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.adversaryController.ServerAdversaryController;
import Game.controllers.playerController.PlayerController;
import Game.controllers.playerController.ServerPlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameState.GameState;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.EntityType;
import Game.gameState.entities.Player;
import Game.gameState.interaction.MoveResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represent a networked game
 */
public class NetworkGameManager extends GameManager {
    ServerSocket serverSocket;

    public NetworkGameManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    @Override
    public void notifyAllLoadLevel() {
        JsonArray playerNameList = new JsonArray();
        for (int i = 0; i < playerControllers.length; i++) {
            playerNameList.add(playerControllers[i].getName());
        }


        JsonObject message = new JsonObject();
        message.addProperty("type", "start-level");
        message.addProperty("level", levelNum);
        message.add("players", playerNameList);

        //send notice load level
        for (PlayerController p : playerControllers) {
            p.notifyLoadLevel(message);
        }
        for (AdversaryController a : adversaryControllers) {
            a.notifyLoadLevel(message);
        }

        //send update to everybody to kick off the load level
        sendAllUpdates();
    }

    @Override
    public void notifyAllGameOver(PlayerController[] playerControllers) {
        for (PlayerController pc : this.getPlayerControllers()) {
            pc.notifyGameOver(playerControllers);
        }
    }

    /**
     * Tells client "name" until is valid again
     * Client will be notified of invalid command from acceptCommand (notifyInvalid)
     * @param socket socket the connection formed
     * @return a valid commandRegister
     * @throws IOException
     */
    private Command reprompt(Socket socket) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        outputStream.writeUTF("name");
        outputStream.flush();

        JsonObject response = JsonParser.parseString(inputStream.readUTF()).getAsJsonObject();
        if (response.get("type").getAsString().equals("register-player")) {
            ServerPlayerController sc = new ServerPlayerController(response.get("name").getAsString(), socket);
            Command regCommand = sc.commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) {
                return regCommand;
            } else {
                return reprompt(socket);
            }
        }
        if (response.get("type").getAsString().equals("register-adversary")) {
            EntityType et = EntityType.ZOMBIE;
            switch (response.get("entity-type").getAsString()) {
                case "ZOMBIE":
                    et = EntityType.ZOMBIE;
                    break;
                case "GHOST":
                    et = EntityType.GHOST;
            }
            ServerAdversaryController sc = new ServerAdversaryController(response.get("name").getAsString(), et, socket);
            Command regCommand = sc.commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) {
                return regCommand;
            } else {
                return reprompt(socket);
            }
        }
        System.out.println("reprompt should not get here");
        System.exit(-1);
        return null;
    }

    /**
     * Read from socket until get a valid jsonObject
     * @param socket socket to read from
     * @return
     */
    private JsonObject readUntilValidJsonObject(Socket socket) throws IOException {
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        try {
            outputStream.writeUTF("name"); outputStream.flush();
            JsonObject response = JsonParser.parseString(inputStream.readUTF()).getAsJsonObject();
            return response;
        } catch (IllegalStateException e) {
            return readUntilValidJsonObject(socket);
        }
    }


    @Override
    public void promptRegister() throws IOException {

        Socket socket = serverSocket.accept();
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        outputStream.writeUTF("{ \"type\": \"welcome\", \"info\": info }");
        outputStream.flush();

//        JsonObject response = null;
//        Command regCommand = null;
//        try {
//            response = JsonParser.parseString(inputStream.readUTF()).getAsJsonObject();
//        } catch (Exception e) {
//            regCommand = reprompt(socket);
//        }
        JsonObject response = readUntilValidJsonObject(socket);

        if (response.get("type").getAsString().equals("register-player")) {
            ServerPlayerController sc = new ServerPlayerController(response.get("name").getAsString(), socket);
            Command regCommand = sc.commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) {
                regCommand.visitGameManager(this);
            } else {
                regCommand = reprompt(socket);
                regCommand.visitGameManager(this);
            }
        }
        if (response.get("type").getAsString().equals("register-adversary")) {
            EntityType et = EntityType.ZOMBIE;
            switch (response.get("type").getAsString()) {
                case "ZOMBIE":
                    et = EntityType.ZOMBIE;
                    break;
                case "GHOST":
                    et = EntityType.GHOST;
            }
            ServerAdversaryController sc = new ServerAdversaryController(response.get("name").getAsString(), et, socket);
            Command regCommand = sc.commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) {
                regCommand.visitGameManager(this);
            } else {
                regCommand = reprompt(socket);
                regCommand.visitGameManager(this);
            }
        }

    }

    @Override
    public void notifyAllEndLevel() {
        String[] exited = new String[0];
        String[] ejected = new String[0];
        String keyHolderName = "";
        for (Player p : players) {
            if (p.getStatus() == EntityStatus.EXITED) {
                exited = growStringArray(exited, p.getName());
            }
            if (p.getStatus() == EntityStatus.DEAD) {
                ejected = growStringArray(ejected, p.getName());
            }
            if (p.getKeyHolder()) {
                keyHolderName = p.getName();
            }
        }

        NotifyEndLevel notice = new NotifyEndLevel(keyHolderName, exited, ejected);
        for (PlayerController pc : playerControllers) {
            pc.notifyEndLevel(notice);
        }

    }

    @Override
    public void intermediateInit(PlayerController[] playerControllers, AdversaryController[] adversaryControllers, GameState gameState) {

    }

    @Override
    public void doMove() {
        if (isPlayerTurn()) {
            acceptCommand(playerControllers[turn].promptMove());
        } else {
            acceptCommand(adversaryControllers[turn % players.length].promptMove());
        }
    }


    @Override
    public void sendResult(String name, MoveResult result) {
        for (PlayerController pc : playerControllers) {
            if (pc.getName().equals(name)) {
                pc.receiveResult(result);
            }
        }
    }


}
