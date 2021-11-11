//package testHarness.Level.TestHarness;
//
//import Game.controllers.Command.Command;
//import Game.controllers.Command.CommandInitialize;
//import Game.controllers.Command.CommandMove;
//import Game.controllers.adversaryController.AdversaryController;
//import Game.controllers.playerController.PlayerController;
//import Game.gameManager.GameManager;
//import Game.gameManager.Updates.Update;
//import Game.gameState.GameState;
//import Game.gameState.direction.Direction;
//import Game.gameState.entities.Adversary;
//import Game.gameState.entities.Entity;
//import Game.gameState.entities.EntityStatus;
//import Game.gameState.entities.Player;
//import Game.gameState.interaction.item.ItemType;
//import Game.gameState.level.Level;
//import Game.gameState.level.tiles.Tile;
//import Game.gameState.posn.Posn;
//import com.google.gson.*;
//import com.google.gson.stream.JsonReader;
//import testHarness.Level.TestHarness.LevelTest.LevelTestHarnessInput;
//import testHarness.Level.TestHarness.LevelTest.LevelTestHarnessParser;
//import testHarness.Level.TestHarness.LevelTest.LevelTestHarnessResult;
//import testHarness.Level.TestHarness.LevelTest.Structures.tLevel;
//import testHarness.Level.TestHarness.StateTest.StateTestInput;
//import testHarness.Level.TestHarness.StateTest.TestParser;
//import testHarness.Level.TestHarness.StateTest.StateTestResult;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.Scanner;
//
///**
// * Class represents the testing harness
// */
//public class TestHarness {
//
//    public static void main(String[] args) throws IOException {
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        gson.serializeNulls();
//        StringReader readin = getStdin();
//        JsonReader reader = new JsonReader(readin);
//
//        JsonArray jsonArray = JsonParser.parseReader(readin).getAsJsonArray();
//        try {
//
//            JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
//            if (firstObject.get("type").getAsString().equals("level")) {
//                levelTest(jsonArray);
//            } else if (firstObject.get("type").getAsString().equals("state")) {
//                stateTest(jsonArray);
//            } else { //is manager test
//                managerTest(jsonArray);
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//        } catch (IllegalStateException e) {
//            managerTest(jsonArray);
//        }
//    }
//
//    private static void levelTest(JsonArray input) throws IOException {
//        tLevel level = LevelTestHarnessParser.parseLevel(input.get(0).getAsJsonObject());
//        Posn pos = LevelTestHarnessParser.parsePosn(input.get(1).getAsJsonArray());
//        LevelTestHarnessInput testInput = new LevelTestHarnessInput(level, pos);
//        LevelTestHarnessResult result = testInput.test();
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(result));
//    }
//
//    private static void stateTest(JsonArray input) throws IOException {
//        StateTestInput stateInput = TestParser.parseAll(input);
//        Tile[][] level = stateInput.getGameState().getLevel().getTiles();
//        StateTestResult result = null;
//        for (Posn p : stateInput.getGameState().getPosnPlayer()) {
//            Entity player = level[p.getRow()][p.getCol()].getEntity();
//            if (player.isPlayer()) {
//                if (player.getName().equals(stateInput.getName())) {
//                    result = tryMove(stateInput.getGameState(), player, stateInput.getPosition());
//                }
//
//            }
//        }
//        if (result == null) {
//            result = StateTestResult.FAILURE_NO_NAME;
//        }
//        JsonObject outLevelState = input.get(0).getAsJsonObject();
//        outLevelState.addProperty("exit-locked", stateInput.getGameState().getIsLocked());
//        String outputJson = getStateTestResult(outLevelState, result, stateInput.getName(),
//                TestParser.parsePosn(input.get(2).getAsJsonArray()));
//        System.out.println(outputJson);
//    }
//
//    private static String getStateTestResult(JsonObject game, StateTestResult result, String playerName, Posn dest) {
//        Gson gson = new Gson();
//        JsonArray jsonOutput = new JsonArray();
//        JsonArray playerArray = game.get("players").getAsJsonArray();
//        JsonElement playerToRemove = null;
//        switch (result) {
//            case SUCCESS:
//                jsonOutput.add("Success");
//                jsonOutput.add(game);
//                break;
//            case SUCCESS_EJECT:
//                jsonOutput.add("Success");
//                jsonOutput.add("Player ");
//                jsonOutput.add(playerName);
//                jsonOutput.add(" was ejected.");
//                for (JsonElement e : playerArray) {
//                    if (e.getAsJsonObject().get("name").getAsString().equals(playerName)) {
//                        playerToRemove = e;
//                    }
//                }
//                playerArray.remove(playerToRemove);
//                game.add("players", playerArray);
//                jsonOutput.add(game);
//                break;
//            case SUCCESS_EXIT:
//                jsonOutput.add("Success");
//                jsonOutput.add("Player ");
//                jsonOutput.add(playerName);
//                jsonOutput.add(" exited.");
//                for (JsonElement e : playerArray) {
//                    if (e.getAsJsonObject().get("name").getAsString().equals(playerName)) {
//                        playerToRemove = e;
//                    }
//                }
//                playerArray.remove(playerToRemove);
//                game.add("players", playerArray);
//                jsonOutput.add(game);
//                break;
//            case FAILURE_NO_NAME:
//                jsonOutput.add("Failure");
//                jsonOutput.add("Player ");
//                jsonOutput.add(playerName);
//                jsonOutput.add(" is not part of the game");
//                break;
//            case FAILURE_NOT_STEPPABLE:
//                jsonOutput.add("Failure");
//                jsonOutput.add("The destination position ");
//                jsonOutput.add(gson.toJson(dest.toArray()));
//                jsonOutput.add(" is not part of the game");
//                break;
//
//        }
//
//        return gson.toJson(jsonOutput);
//    }
//
//    private static StateTestResult tryMove(GameState game, Entity player, Posn p) {
//        Tile t = game.getLevel().getTiles()[p.getRow()][p.getCol()];
//        if (t.isFloorTile() && t.getEntity() == null) {
//            game.getLevel().getTiles()[player.getPosn().getRow()][player.getPosn().getCol()].removeEntity();
//            game.placeEntity(player, p);
//            return StateTestResult.SUCCESS;
//        } else if (t.isFloorTile() && t.getEntity().isAdversary()) {
//
//            return StateTestResult.SUCCESS_EJECT;
//        } else if (t.getItem() == ItemType.EXIT && !game.getIsLocked()) {
//            return StateTestResult.SUCCESS_EXIT;
//        } else if (t.isWallTile() || t.getEntity().isPlayer()) {
//            return StateTestResult.FAILURE_NOT_STEPPABLE;
//        } else return null;
//    }
//
//    private static void managerTest(JsonArray jsonArray) throws IOException {
//        JsonArray names = jsonArray.get(0).getAsJsonArray();
//        Level level = TestParser.parseLevel(jsonArray.get(1).getAsJsonObject());
//        JsonArray objects = jsonArray.get(1).getAsJsonObject().get("objects").getAsJsonArray();
//        int numMoves = jsonArray.get(2).getAsInt();
//        JsonArray pointList = jsonArray.get(3).getAsJsonArray();
//        JsonArray moveList = jsonArray.get(4).getAsJsonArray();
//
//        GameManager gameManager = new GameManager();
//
//        registerNames(gameManager, names, pointList);
//        gameManager.acceptCommand(new CommandInitialize("", new Level[]{level}));
//
//        JsonArray results = tryMoves(gameManager, moveList, objects);
//        System.out.println(results);
//    }
//
//    private static JsonArray tryMoves(GameManager gameManager, JsonArray moveList, JsonArray objects) throws IOException {
//        JsonArray result = new JsonArray();
//        PlayerController[] playerControllers = gameManager.getPlayerControllers();
//        Player[] players = gameManager.getPlayers();
//        int i = 0; //index for which player is moving
//        for (JsonElement e : moveList) {
//            JsonArray moves = e.getAsJsonArray();
//            PlayerController thisController = playerControllers[i];
//            Player thisPlayer = players[i];
//
//            result.addAll(consumeMoveList(gameManager, moves, thisController, thisPlayer, objects));
//            i++;
//        }
//        return result;
//    }
//
//    private static JsonArray consumeMoveList(GameManager manager, JsonArray moves, PlayerController controller, Player player, JsonArray objects) throws IOException {
//        JsonArray result = new JsonArray();
//        for (JsonElement e : moves) {
//            JsonObject move = e.getAsJsonObject();
//            if (move.get("to") == null) {
//                manager.acceptCommand(controller.commandMove(Direction.STAY));
//            } else { //find some path to the dest
//                Posn dest = TestParser.parsePosn(move.get("to").getAsJsonArray());
//                if (dest == null) {
//                    Update[] updates = manager.acceptCommand(controller.commandMove(Direction.STAY));
//                    result.addAll(handleUpdates(manager, updates, player, move, objects));
//                } else {
//                    if (Math.abs(player.getPosn().getRow() - dest.getRow()) + Math.abs(player.getPosn().getCol() - dest.getCol()) <= 2) {
//                        Command[] path = getPath(manager, player.getPosn(), dest, controller);
//                        if (path == null) result.add(invalidMove(player.getName(), move));
//                        for (Command c : path) {
//                            Update[] updates = manager.acceptCommand(c);
//                            result.addAll(handleUpdates(manager, updates, player, move, objects));
//                        }
//                    } else {
//                        result.add(invalidMove(player.getName(), move));
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    public static JsonArray invalidMove(String name, JsonObject move) {
//        JsonArray invalidMove = new JsonArray();
//        invalidMove.add(name);
//        invalidMove.add(move);
//        invalidMove.add(move.get("to").getAsJsonArray());
//        return invalidMove;
//    }
//
//    private static Command[] getPath(GameManager manager, Posn origin, Posn dest, PlayerController pc) {
//        if (origin.getRow() == dest.getRow()) {
//            if (origin.getCol() - dest.getCol() == -1) {
//                return new Command[]{pc.commandMove(Direction.RIGHT)};
//            }
//            if (origin.getCol() - dest.getCol() == -2) {
//                return new Command[]{pc.commandMove(Direction.RIGHT), pc.commandMove(Direction.RIGHT)};
//            }
//            if (origin.getCol() - dest.getCol() == 1) {
//                return new Command[]{pc.commandMove(Direction.LEFT)};
//            }
//            if (origin.getCol() - dest.getCol() == 2) {
//                return new Command[]{pc.commandMove(Direction.LEFT), pc.commandMove(Direction.LEFT)};
//            }
//        }
//        if (origin.getCol() == dest.getCol()) {
//            if (origin.getRow() - dest.getRow() == -1) {
//                return new Command[]{pc.commandMove(Direction.DOWN)};
//            }
//            if (origin.getRow() - dest.getRow() == -2) {
//                return new Command[]{pc.commandMove(Direction.DOWN), pc.commandMove(Direction.DOWN)};
//            }
//            if (origin.getRow() - dest.getRow() == 1) {
//                return new Command[]{pc.commandMove(Direction.UP)};
//            }
//            if (origin.getRow() - dest.getRow() == 2) {
//                return new Command[]{pc.commandMove(Direction.UP), pc.commandMove(Direction.UP)};
//            }
//        }
//        if (origin.getRow() - 1 == dest.getRow() && origin.getCol() - 1 == dest.getCol()) {
//            Command[] a = new Command[]{pc.commandMove(Direction.LEFT), pc.commandMove(Direction.UP)};
//            if (acceptCommands(a, manager)) return a;
//            Command[] b = new Command[]{pc.commandMove(Direction.UP), pc.commandMove(Direction.LEFT)};
//            if (acceptCommands(b, manager)) return b;
//        }
//        if (origin.getRow() - 1 == dest.getRow() && origin.getCol() + 1 == dest.getCol()) {
//            Command[] a = new Command[]{pc.commandMove(Direction.RIGHT), pc.commandMove(Direction.UP)};
//            if (acceptCommands(a, manager)) return a;
//            Command[] b = new Command[]{pc.commandMove(Direction.UP), pc.commandMove(Direction.RIGHT)};
//            if (acceptCommands(b, manager)) return b;
//        }
//        if (origin.getRow() + 1 == dest.getRow() && origin.getCol() - 1 == dest.getCol()) {
//            Command[] a = new Command[]{pc.commandMove(Direction.LEFT), pc.commandMove(Direction.DOWN)};
//            if (acceptCommands(a, manager)) return a;
//            Command[] b = new Command[]{pc.commandMove(Direction.DOWN), pc.commandMove(Direction.LEFT)};
//            if (acceptCommands(b, manager)) return b;
//        }
//        if (origin.getRow() + 1 == dest.getRow() && origin.getCol() + 1 == dest.getCol()) {
//            Command[] a = new Command[]{pc.commandMove(Direction.RIGHT), pc.commandMove(Direction.DOWN)};
//            if (acceptCommands(a, manager)) return a;
//            Command[] b = new Command[]{pc.commandMove(Direction.DOWN), pc.commandMove(Direction.RIGHT)};
//            if (acceptCommands(b, manager)) return b;
//        }
//        return null;
//    }
//
//    private static boolean acceptCommands(Command[] commands, GameManager manager) {
//        boolean result = true;
//        for (Command c : commands) {
//            manager.acceptCommand(c);
//            result = result && c.getIsValid();
//        }
//        return result;
//    }
//
//    //creates name, player-update pair
//    private static JsonArray handleUpdates(GameManager manager, Update[] updates, Player player, JsonElement move, JsonArray objects) throws IOException {
//        JsonArray result = new JsonArray();
//        for (Update u : updates) {
//            JsonArray newEntry = new JsonArray();
//            newEntry.add(u.getTo());
//            JsonObject playerUpdate = new JsonObject();
//            Gson gson = new Gson();
//            playerUpdate.add("layout", gson.toJsonTree(parseToIntArray(u.getLevel())).getAsJsonArray());
//            playerUpdate.add("position", gson.toJsonTree(player.getPosn().toArray()).getAsJsonArray());
//            playerUpdate.add("objects", gson.toJsonTree(getObjectsInRange(player.getPosn(), objects)).getAsJsonArray());
//            playerUpdate.add("actors", gson.toJsonTree(getActorsInRange(player.getPosn(), manager, player)).getAsJsonArray());
//            newEntry.add(playerUpdate);
//
//            result.add(newEntry);
//        }
//        return result;
//    }
//
//    private static JsonArray getActorsInRange(Posn center, GameManager manager, Player player) {
//        JsonArray ja = new JsonArray();
//        Player[] players = manager.getPlayers();
//        Adversary[] adversaries = manager.getAdversaries();
//        ja.addAll(getEntityInRange(center, players, player));
//        ja.addAll(getEntityInRange(center, adversaries, player));
//        return ja;
//    }
//
//    private static JsonArray getEntityInRange(Posn center, Entity[] entities, Player player) {
//        Gson gson = new Gson();
//        JsonArray result = new JsonArray();
//        for (Entity e : entities) {
//            if (!(player.getName().equals(e.getName()))) {
//                if (inBounds(center, e.getPosn())) {
//                    result.add(gson.toJsonTree(e));
//                }
//            }
//        }
//        return result;
//    }
//
//    private static JsonArray getObjectsInRange(Posn center, JsonArray objects) throws IOException {
//        JsonArray result = new JsonArray();
//        for (JsonElement je : objects) {
//            JsonObject jo = je.getAsJsonObject();
//            Posn posn = TestParser.parsePosn(jo.get("position").getAsJsonArray());
//            if (inBounds(center, posn)) {
//                result.add(je);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * Turn a string into an array of int tiles
//     *
//     * @param tiles
//     */
//    private static int[][] parseToIntArray(String tiles) {
//        int[][] result = new int[5][5];
//        for (int i = 0; i < 5; i++) {
//            int[] row = new int[5];
//            for (int j = 0; j < 5; j++) {
//                char c = tiles.charAt((5 * i) + j + i);
//                switch (c) {
//                    case '■':
//                        row[j] = 0;
//                        break;
//                    case 'P':
//                    case 'A':
//                    case 'E':
//                    case 'h':
//                    case '□':
//                    case 'K':
//                        row[j] = 1;
//                        break;
//                    case 'd':
//                        row[j] = 2;
//                        break;
//                }
//            }
//            result[i] = row;
//        }
//        return result;
//    }
//
//    private static boolean inBounds(Posn center, Posn input) {
//        Posn topleft = new Posn(center.getRow() - 2, center.getCol() - 2);
//        Posn bottomright = new Posn(center.getRow() + 3, center.getCol() + 3);
//        return inBounds(topleft, bottomright, input);
//    }
//
//    /**
//     * Determine if posn input is within the bounding box defined by topleft and bottomright
//     *
//     * @param topLeft
//     * @param bottomRight
//     * @param input
//     * @return
//     */
//    private static boolean inBounds(Posn topLeft, Posn bottomRight, Posn input) {
//        return (topLeft.getRow() < input.getRow() && input.getRow() < bottomRight.getRow())
//                && (topLeft.getCol() < input.getCol() && input.getCol() < bottomRight.getCol());
//    }
//
//    private static String getUpdateResult(Update oldUpdate, Update newUpdate) {
//        if (oldUpdate.getStatus() == EntityStatus.ALIVE && (newUpdate.getStatus() == EntityStatus.DEAD)) {
//            return "EJECTED";
//        }
//        if (oldUpdate.getStatus() == EntityStatus.ALIVE && (newUpdate.getStatus() == EntityStatus.EXITED)) {
//            return "EXIT";
//        }
//        if (oldUpdate.isLocked() && !newUpdate.isLocked()) {
//            return "KEY";
//        }
//        if (!oldUpdate.isExit() && newUpdate.isExit()) {
//            return "EXIT";
//        } else return "OK";
//    }
//
//    /**
//     * Get the update of a specific name
//     *
//     * @param updates
//     * @param name
//     * @return
//     */
//    private static Update getUpdate(Update[] updates, String name) {
//        for (Update u : updates) {
//            if (u.getTo().equals(name)) return u;
//        }
//        return null;
//    }
//
//    private static Direction findDirection(Posn from, Posn to) {
//        if (from.equals(to)) {
//            return Direction.STAY;
//        }
//        if (from.getCol() == to.getCol()) {
//            if (from.getRow() - to.getRow() == 1) {
//                return Direction.UP;
//            }
//            if (from.getRow() - to.getRow() == -1) {
//                return Direction.DOWN;
//            }
//        }
//        if (from.getRow() == to.getRow()) {
//            if (from.getCol() - to.getCol() == 1) {
//                return Direction.LEFT;
//            }
//            if (from.getCol() - to.getCol() == -1) {
//                return Direction.RIGHT;
//            }
//        }
//        return null; //ERROR CASE, this move is definitely invalid
//    }
//
//    /**
//     * Register the players and adversaries
//     *
//     * @param gameManager manager to register to
//     * @param names       names of players to register (must be unique)
//     * @param pointList   list of points to place players and adversaries. Number of adversaries is given by diff
//     *                    of names.size and pointList.size.
//     *                    Used to determine number of adversaries to register
//     */
//    private static void registerNames(GameManager gameManager, JsonArray names, JsonArray pointList) {
//        for (JsonElement e : names) {
//            String name = e.getAsString();
//            PlayerController pc = new PlayerController(name);
//            gameManager.acceptCommand(pc.commandRegister());
//        }
//        int numAdv = pointList.size() - names.size();
//        for (int i = 0; i < numAdv; i++) {
//            String name = "ghost" + i;
//            AdversaryController ac = new AdversaryController(name);
//            gameManager.acceptCommand(ac.commandRegister());
//        }
//    }
//
//
//    private static StringReader getStdin() {
//        Scanner scanner = new Scanner(System.in);
//        String stringInput = "";
//        while (scanner.hasNextLine()) {
//            stringInput += scanner.nextLine();
//        }
//        return new StringReader(stringInput);
//    }
//}
