package Game.gameManager;

import Game.controllers.Command.Command;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.playerController.LocalPlayerController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameState.GameState;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.Player;
import Game.gameState.interaction.MoveResult;
import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Scanner;

/**
 * Represent a local game manager
 */
public class LocalGameManager extends GameManager {


    public LocalGameManager() {
    }

    @Override
    public void sendResult(String name, MoveResult result) {
        getPlayerControllerByName(name).receiveResult(result);
    }

    @Override
    public void notifyInvalid(Command c) {
        PlayerController pc = getPlayerControllerByName(c.getName());
        sendResult(c.getName(), MoveResult.INVALID);
    }

//    @Override
//    public Update[] updateObservers() {
//        Update[] result = new Update[0];
//        for (Observer o : observers) {
////            Update update = new Update(
////                    o.getName(),
////                    new Posn(0, 0),
////                    EntityStatus.ALIVE,
////                    gameState.getLevel().getSubsetString(new Posn(0, 0)),
////                    gameState.getIsLocked(),
////                    gameState.isExit(),
////                    gameState
////            ).addTiles(getAllTile());
//            Update update = generateObserverUpdate();
//            o.receiveUpdate(update);
//            result = expandUpdates(result, update);
//        }
//        return result;
//    }

    private Tile[][] getAllTile() {
        Posn topLeft = new Posn(0, 0);
        Posn bottomRIght = new Posn(gameState.getLevel().getTiles().length, gameState.getLevel().getTiles()[0].length);
        return gameState.getLevel().getSubsetTile(topLeft, bottomRIght);
    }

    private Tile[][] getSubsetTile(Posn center) {
        Posn posn = center;
        Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
        Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
        return gameState.getLevel().getSubsetTile(topleft, bottomRight);
    }

//    @Override
//    public Update[] updatePlayers() {
//        Update[] result = new Update[0];
//        for (PlayerController pc : playerControllers) {
//            Player player = getPlayerByName(pc.getName());
//            if (player.getStatus() == EntityStatus.ALIVE || player.getStatus() == EntityStatus.EXITED) {
//                Posn posn = player.getPosn();
//                Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
//                Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
////                Update update = new Update(
////                        player.getName(),
////                        player.getPosn(),
////                        player.getStatus(),
////                        gameState.getLevel().getSubsetString(topleft, bottomRight),
////                        gameState.getIsLocked(),
////                        gameState.isExit()
////                ).addTiles(getSubsetTile(player.getPosn()));
//                Update update = this.generateEntityUpdate(getPlayerByName(pc.getName()));
//                pc.receiveUpdate(update);
//                result = expandUpdates(result, update);
//            }
//        } //otherwise do not send update at all
//        return result;
//    }

//    @Override
//    public Update[] updateAdversaries() {
//        Update[] result = new Update[0];
//        for (AdversaryController ac : adversaryControllers) {
//            Adversary adversary = getAdversaryByName(ac.getName());
//            if (adversary.getStatus() == EntityStatus.ALIVE) {
////                Update update = new Update(
////                        adversary.getName(),
////                        adversary.getPosn(),
////                        adversary.getStatus(),
////                        gameState.getLevel().getSubsetString(new Posn(0, 0)),
////                        gameState.getIsLocked(),
////                        gameState.isExit(),
////                        this.gameState
////                );
//                Update update = this.generateEntityUpdate(getAdversaryByName(ac.getName()));
//                ac.receiveUpdate(update);
//                result = expandUpdates(result, update);
//            }
//        }
//        return result;
//    }
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

        for (PlayerController p : playerControllers) {
            p.notifyLoadLevel(message);
        }
    }

    @Override
    public void notifyAllGameOver(PlayerController[] playerControllers) {
        for (PlayerController pc : playerControllers) {
            pc.notifyGameOver(playerControllers);
        }
    }

    @Override
    public void promptRegister() {
        System.out.println("Enter player " + playerControllers.length + " name: ");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        Command register = new LocalPlayerController(name).setRender(true).commandRegister();
        try {
            acceptCommand(register);
        } catch (IllegalArgumentException e) { //todo this is kind of janky but it works for now i guess
            promptRegister();
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
        this.playerControllers = playerControllers;
        this.adversaryControllers = adversaryControllers;
        this.gameState = gameState;

        for (PlayerController pc : playerControllers) {
            acceptCommand(pc.commandRegister());
        }

        for (AdversaryController ac : adversaryControllers) {
            acceptCommand(ac.commandRegister());
        }
    }

    @Override
    public void doMove() {
        System.out.println("PROMPTING=============================================");
        if (isPlayerTurn()) {
            acceptCommand(playerControllers[turn].promptMove());
        } else {
            acceptCommand(adversaryControllers[turn % players.length].promptMove());
        }
    }


}
