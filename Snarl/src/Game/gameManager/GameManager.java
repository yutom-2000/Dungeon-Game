package Game.gameManager;

import Game.controllers.Command.CommandInitialize;
import Game.controllers.Observer;
import Game.controllers.adversaryController.AdversaryController;
import Game.controllers.adversaryController.LocalAdversaryController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.Updates.NotifyEndLevel;
import Game.gameManager.Updates.Update;
import Game.gameState.ActorPosition;
import Game.gameState.ObjectPosition;
import Game.gameState.entities.*;
import Game.gameState.GameState;

import Game.controllers.Command.Command;
import Game.gameState.interaction.MoveResult;
import Game.gameState.level.Level;
import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;

import Game.ruleChecker.RuleChecker;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

/**
 * Manages the registration of players and adversaries into a game.
 * <p>
 * Also holds information regarding number of levels, and the status of the entire game (vs the status of level held in GameState)
 * <p>
 * - Aggregate user registration into Game
 * - Check inputs against RuleChecker to determine if valid move
 * - Call correct methods on gamestate to perform commands if valid move
 * - update all the clients after each move
 */
public abstract class GameManager implements CommandAcceptor {

    Player[] players = new Player[0];
    Adversary[] adversaries = new Adversary[0];
    RuleChecker ruleChecker = new RuleChecker();
    GameState gameState = new GameState();
    PlayerController[] playerControllers = new PlayerController[0];
    AdversaryController[] adversaryControllers = new AdversaryController[0];
    Observer[] observers = new Observer[0];
    AdversaryController[] remoteAdversaries = new AdversaryController[0];

    //keep track of the updatereceivers for update sending
    int turn = 0;
    int levelNum = 0;

    /**
     * After initialize, run the for loop to play the game
     */
    public void playGame(Level[] levels) {
        registerAdversaries(remoteAdversaries);
        acceptCommand(new CommandInitialize("admin", levels));
        while (!gameOver()) {
            notifyAllLoadLevel();
            while (!levelOver()) {
                doMove();
            }
            if (gameState.isExit()) {
                levelNum++;
                if (notLastLevel()) { //load gamestate for this level
                    changeLevel(remoteAdversaries);
                } else break; //game over, no more levels
            } else break; //you have lost

        }
        notifyAllGameOver(playerControllers);
    }

    private void flushAdversaries() {
        this.adversaries = new Adversary[0];
    }

    private void registerAdversaries(AdversaryController[] remoteAdversaries) {
        ruleChecker.flushAdversaryNames();
        this.flushAdversaries();
        this.flushAdversaryControllers(); //now ready to re-register

        int numZombies = (this.levelNum / 2) + 1; //levelNum is iterated before this method is called, so don't need to worry about it
        int numGhosts = (this.levelNum - 1) / 2;

        for (AdversaryController ac : remoteAdversaries) {
            if (ac.getType() == EntityType.ZOMBIE) {
                if (numZombies > 0) {
                    acceptCommand(ac.commandRegister()); //register this controller
                    numZombies --; //de-increment
                }
                else ac.notifyNotRegistered();
            }
            if (ac.getType() == EntityType.GHOST) {
                if (numGhosts > 0) {
                    acceptCommand(ac.commandRegister());
                    numGhosts--;
                }
                else ac.notifyNotRegistered();
            }
        }
        fillLocalAdversaries(numZombies, numGhosts);
    }

    /**
     * Send appropriate updates to all Observer, player, and adversary after a move
     * Mostly for convenience/readability
     */
    public void sendAllUpdates() {
        updatePlayers();
        updateObservers();
        updateAdversaries();
    }

    /**
     * Notify all controllers worth notifying that new level is being loaded
     */
    public abstract void notifyAllLoadLevel();

    /**
     * Notify all players the game is over
     */
    public abstract void notifyAllGameOver(PlayerController[] playerControllers);

    /**
     * Mutate gamestate to hold next level
     * Mutate turn to go back to 0 (first player turn again)
     */
    public void loadNextLevel() {
        registerAdversaries(remoteAdversaries);
        gameState = new GameState().initialize(ruleChecker.getLevels()[levelNum], players, adversaries);
        System.out.println("levelNum: " + levelNum);
        turn = 0;
    }

    /**
     * Prompt the controller to register
     * Ask it to do commandRegister basically
     */
    public abstract void promptRegister() throws IOException;

    /**
     * Determine if this level is last level
     *
     * @return
     */
    public boolean notLastLevel() {
        return levelNum < ruleChecker.getLevels().length;
    }

    public static String[] growStringArray(String[] original, String toAppend) {
        String[] result = new String[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

    public abstract void notifyAllEndLevel();


    public void incrementTurn() {
        turn = (turn + 1) % (players.length + adversaries.length);

    }

    /**
     * Start a game midway through
     */
    public abstract void intermediateInit(PlayerController[] playerControllers, AdversaryController[] adversaryControllers, GameState gameState);

    /**
     * Consume a move
     */
    public abstract void doMove();

//    /**
//     * Render everybody
//     */
//    public abstract void renderAll();

    //Level is over if all players are either dead or exited
    public boolean levelOver() {
        return allDeadOrExit();
    }


    //Game is over if advance past last level, or all players are dead
    public boolean gameOver() {
        return levelNum == ruleChecker.getLevels().length
                || allPlayersDead();
    }

    private boolean allDeadOrExit() {
        for (Player p : players) {
            if (p.getStatus() == EntityStatus.ALIVE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check to see if all players are dead
     * Return false if any player is exited/alive
     *
     * @return
     */
    private boolean allPlayersDead() {
        for (Player p : players) {
            if (!p.getStatus().equals(EntityStatus.DEAD)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Default constructor for GameManager
     */
    public GameManager() {
    }

    /**
     * Determine if is players' turn based on turn number
     *
     * @return
     */
    public boolean isPlayerTurn() {
        return turn < players.length;
    }


    /**
     * Notify a client with name a result
     * @param name
     * @param result
     */
    public abstract void sendResult(String name, MoveResult result);

    /**
     * Notify a client that the command was invalid
     * @param c
     */
    public void notifyInvalid(Command c) {
        sendResult(c.getName(), MoveResult.INVALID);
    }

    /**
     * Accept a command
     *
     * @param c command to accept
     * @return list of updates sent out after accepting command
     */
    @Override
    public Update[] acceptCommand(Command c) {
        Update[] updates = new Update[0];

        c.addGameState(gameState);
        if (checkRules(c)) { //if valid propogate change to the manager
            c.visitGameManager(this);
        } else {
            notifyInvalid(c);
            c.invalidHandle(this);
            //printError(c);
            return updates;
        }
//        if (isGameStateInitialized()) {
//            updates = listAppend(updates, updateObservers()); //always update observers
//            if (!isPlayerTurn()) {
//                updates = listAppend(updates, updateAdversaries()); //adversaries are only updated on their turn
//            }
//            updates = listAppend(updates, updatePlayers()); //always update players (only if alive)
//
//        }
        return updates;
    }


    private void printError(Command c) {
        Gson gson = new Gson();
        gson.serializeNulls();
        System.out.println("Invalid command: " + c.toString());
        System.out.println("Gamemanager: ");
        System.out.println(gson.toJson(this));
        //todo can probably do some NACK activity here
    }

    /**
     * Convenience method for appending two lists of Updates
     *
     * @param original
     * @param toAppend
     * @return
     */
    private Update[] listAppend(Update[] original, Update[] toAppend) {

        for (Update u : toAppend) {
            original = expandUpdates(original, u);
        }
        return original;
    }

    /**
     * Send an update to observers
     * should be done after every move
     *
     * @return the list of updates delivered
     */
    public Update[] updateObservers() {
        Update[] result = new Update[0];
        for (Observer o : observers) {
//            Update update = new Update(
//                    o.getName(),
//                    new Posn(0, 0),
//                    EntityStatus.ALIVE,
//                    gameState.getLevel().getSubsetString(new Posn(0, 0)),
//                    gameState.getIsLocked(),
//                    gameState.isExit(),
//                    gameState
//            ).addTiles(getAllTile());
            Update update = generateObserverUpdate();
            o.receiveUpdate(update);
            result = expandUpdates(result, update);
        }
        return result;
    }

    /**
     * Updates all players that are still alive
     *
     * @return the updates sent out this time the method was called
     */
    public Update[] updatePlayers() {
        Update[] result = new Update[0];
        for (PlayerController pc : playerControllers) {
            Player player = getPlayerByName(pc.getName());
            if (player.getStatus() == EntityStatus.ALIVE || player.getStatus() == EntityStatus.EXITED) {
                Posn posn = player.getPosn();
                Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
                Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
//                Update update = new Update(
//                        player.getName(),
//                        player.getPosn(),
//                        player.getStatus(),
//                        gameState.getLevel().getSubsetString(topleft, bottomRight),
//                        gameState.getIsLocked(),
//                        gameState.isExit()
//                ).addTiles(getSubsetTile(player.getPosn()));
                Update update = this.generateEntityUpdate(getPlayerByName(pc.getName()));
                pc.receiveUpdate(update);
                result = expandUpdates(result, update);
            }
        } //otherwise do not send update at all
        return result;
    }

    /**
     * Update adversaries if alive
     *
     * @return list of updates delivered
     */
    public Update[] updateAdversaries() {
        Update[] result = new Update[0];
        for (AdversaryController ac : adversaryControllers) {
            Adversary adversary = getAdversaryByName(ac.getName());
            if (adversary.getStatus() == EntityStatus.ALIVE) {
//                Update update = new Update(
//                        adversary.getName(),
//                        adversary.getPosn(),
//                        adversary.getStatus(),
//                        gameState.getLevel().getSubsetString(new Posn(0, 0)),
//                        gameState.getIsLocked(),
//                        gameState.isExit(),
//                        this.gameState
//                );
                Update update = this.generateEntityUpdate(getAdversaryByName(ac.getName()));
                ac.receiveUpdate(update);
                result = expandUpdates(result, update);
            }
        }
        return result;
    }

    /**
     * Flush the adversary controller list for re-registering
     */
    public void flushAdversaryControllers() {
        this.adversaryControllers = new AdversaryController[0];
    }

    /**
     * Fill with local adversary controllers
     * @param numZombies number of zombies to fill
     * @param numGhosts number of ghosts to fill
     */
    private void fillLocalAdversaries(int numZombies, int numGhosts) {
        while (numZombies > 0) {
            int i = 1;
            Command regCommand = new LocalAdversaryController("zombie " + i, EntityType.ZOMBIE).commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) { //check if the command is valid, if not, ignore the controller basically and reroll for a unique name by adding i
                regCommand.visitGameManager(this); //can't use accept because double visit to rulechecker will cause bad things to happen
                numZombies--; //deincrement
            }
            i++;
        }
        while (numGhosts > 0) {
            int i = 1;
            Command regCommand = new LocalAdversaryController("ghost " + i, EntityType.GHOST).commandRegister();
            ruleChecker.acceptCommand(regCommand);
            if (regCommand.getIsValid()) {
                regCommand.visitGameManager(this);
                numGhosts--;
            }
            i++;
        }
    }

    /**
     * Perform tasks to change level.
     * 1. Flush the adversary controllers list
     * 2. Try to register from remote adversary controller list first, then use computer-controlled
     * 3. Let people know  gameManager.notifyAllEndLevel();
     *                     gameManager.loadNextLevel();
     *                     gameManager.updatePlayers();
     *                     gameManager.updateObservers();
     * @param remoteAdversaries
     */
    public void changeLevel(AdversaryController[] remoteAdversaries) {
        registerAdversaries(remoteAdversaries);

        notifyAllEndLevel(); //first tell everyone the level ended
        loadNextLevel(); //then load the next level
        updatePlayers(); //update players
        updateObservers(); //update observers
        updateAdversaries(); //update Adversaries
    }

    /**
     * Add a remote adversary
     * By adding to remoteAdversaries, ensure will not be flushed on levelChange
     */
    public void addRemoteAdversaryController(AdversaryController toAppend) {
        this.remoteAdversaries = expandAdversaryControllers(this.remoteAdversaries, toAppend);
    }

    /**
     * Return the player with given name
     *
     * @return
     */
    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Update generateAdversaryUpdate(Adversary subject) {
        Update result = new Update();
        Posn posn = subject.getPosn();
        Posn topleft = new Posn(0, 0);
        Posn bottomRight = new Posn(gameState.getLevel().getTiles().length, gameState.getLevel().getTiles()[0].length);

        Tile[][] tiles = gameState.getLevel().getSubsetTile(topleft, bottomRight);
        ActorPosition[] actorPositions = gameState.getLevel().getActorPositionList(tiles);
        ObjectPosition[] objectPositions = gameState.getLevel().getObjectPositionList(tiles, topleft);
        int[][] layout = gameState.getLevel().getTileLayout(topleft, bottomRight);

        result.addPosition(posn);
        result.addObjectPositionList(objectPositions);
        result.addActorPositionList(actorPositions);
        result.addEntityStatus(subject.getStatus());
        result.addLevelString(gameState.getLevel().getSubsetString(topleft, bottomRight)); //todo remove
        if (subject.isAdversary()) {
            result.addGamestate(this.gameState);
        }

        return result;
    }

    /**
     * Generate an update for a given entity
     * @param subject
     * @return
     */
    public Update generateEntityUpdate(Entity subject) {
        Update result = new Update();
        Posn posn = subject.getPosn();
        Posn topLeft = new Posn(0, 0);
        Posn bottomRight = new Posn(gameState.getLevel().getTiles().length, gameState.getLevel().getTiles()[0].length);
        if (subject.isPlayer()) {
            topLeft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
            bottomRight = new Posn(topLeft.getRow() + 5, topLeft.getCol() + 5);
        }

        Tile[][] tiles = gameState.getLevel().getSubsetTile(topLeft, bottomRight);
        ActorPosition[] actorPositions = gameState.getLevel().getActorPositionList(tiles);
        ObjectPosition[] objectPositions = gameState.getLevel().getObjectPositionList(tiles, topLeft);
        int[][] layout = gameState.getLevel().getTileLayout(topLeft, bottomRight);

        result.addTileLayout(layout);
        result.addPosition(posn);
        result.addObjectPositionList(objectPositions);
        result.addActorPositionList(actorPositions);
        result.addEntityStatus(subject.getStatus());
        result.addMessage(gameState.getLevel().getSubsetString(topLeft, bottomRight)) ; //todo remove
        if (subject.isAdversary()) {
            result.addGamestate(this.gameState);
        }

        return result;
    }

    /**
     * Generate the observer's updates
     * @return
     */
    public Update generateObserverUpdate() {
        Update result = new Update();
        Posn topleft = new Posn(0, 0);
        Posn bottomRight = new Posn(gameState.getLevel().getTiles().length, gameState.getLevel().getTiles()[0].length);

        Tile[][] tiles = gameState.getLevel().getSubsetTile(topleft, bottomRight);
        ActorPosition[] actorPositions = gameState.getLevel().getActorPositionList(tiles);
        ObjectPosition[] objectPositions = gameState.getLevel().getObjectPositionList(tiles, topleft);
        int[][] layout = gameState.getLevel().getTileLayout(topleft, bottomRight);

        result.addTileLayout(layout);
        result.addPosition(new Posn(0, 0));
        result.addObjectPositionList(objectPositions);
        result.addActorPositionList(actorPositions);
        result.addEntityStatus(EntityStatus.ALIVE);

        return result;
    }

    /**
     * Return the adversary with given name
     *
     * @param name
     */
    public Adversary getAdversaryByName(String name) {
        for (Adversary a : adversaries) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    private boolean isGameStateInitialized() {
        return gameState.getLevel() != null
                && gameState.getPlayers() != null
                && gameState.getAdversaries() != null;
    }

    public boolean checkRules(Command c) {
        ruleChecker.acceptCommand(c);
        return c.getIsValid();
    }

    public void addRuleChecker(RuleChecker rc) {
        this.ruleChecker = rc;
    }

    public void addPlayer(Player toAppend) {
        this.players = expandPlayers(this.players, toAppend);
    }

    public void addAdversary(Adversary toAppend) {
        this.adversaries = expandAdversaries(this.adversaries, toAppend);
    }



    public void addPlayerController(PlayerController toAppend) {
        this.playerControllers = expandPlayerControllers(this.playerControllers, toAppend);
    }

    public void addAdversaryController(AdversaryController toAppend) {
        this.adversaryControllers = expandAdversaryControllers(this.adversaryControllers, toAppend);
    }

    public void addObserver(Observer toAppend) {
        this.observers = expandObservers(this.observers, toAppend);
    }


    /**
     * Grow Update player array by 1
     *
     * @param original
     * @param toAppend
     * @return
     */
    public Update[] expandUpdates(Update[] original, Update toAppend) {
        if (original == null) {
            return new Update[]{toAppend};
        }
        Update[] result = new Update[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

    /**
     * Grow the player array by 1
     *
     * @param original original player array
     * @param toAppend player to append to the list
     * @return new Player[] with appended element
     */
    private Player[] expandPlayers(Player[] original, Player toAppend) {
        if (original == null) {
            return new Player[]{toAppend};
        }
        Player[] result = new Player[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;
        return result;
    }

    /**
     * Grow the adversary array by 1
     *
     * @param original original adversary array
     * @param toAppend adversary to append to the list
     * @return new Adversary[] with appended element
     */
    private Adversary[] expandAdversaries(Adversary[] original, Adversary toAppend) {
        if (original == null) {
            return new Adversary[]{toAppend};
        }
        Adversary[] result = new Adversary[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;

        return result;
    }

    /**
     * Set the gameState
     *
     * @param gameState
     */
    public void addGamestate(GameState gameState) {
        this.gameState = gameState;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Adversary[] getAdversaries() {
        return this.adversaries;
    }

    public RuleChecker getRuleChecker() {
        return this.ruleChecker;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public int getLevelNum() {
        return this.levelNum;
    }



    /**
     * Grow the playerController array by 1
     *
     * @param original
     * @param toAppend
     * @return
     */
    private PlayerController[] expandPlayerControllers(PlayerController[] original, PlayerController toAppend) {
        if (original == null) {
            return new PlayerController[]{toAppend};
        }
        PlayerController[] result = new PlayerController[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            if (original[i].getName().equals(toAppend.getName())) throw new IllegalArgumentException("Duplicate name");
            result[i] = original[i];
        }
        result[original.length] = toAppend;

        return result;
    }

    /**
     * Grow the adversaryController array by 1
     *
     * @param original
     * @param toAppend
     * @return
     */
    private AdversaryController[] expandAdversaryControllers(AdversaryController[] original, AdversaryController toAppend) {
        if (original == null) {
            return new AdversaryController[]{toAppend};
        }
        AdversaryController[] result = new AdversaryController[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;

        return result;
    }

    /**
     * Grow Observer array by 1
     *
     * @param original
     * @param toAppend
     * @return
     */
    private Observer[] expandObservers(Observer[] original, Observer toAppend) {
        if (original == null) {
            return new Observer[]{toAppend};
        }
        Observer[] result = new Observer[original.length + 1];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i];
        }
        result[original.length] = toAppend;

        return result;
    }

    /**
     * Get a player controller by name
     *
     * @return
     */
    PlayerController getPlayerControllerByName(String name) {
        for (PlayerController pc : playerControllers) {
            if (pc.getName().equals(name)) {
                return pc;
            }
        }
        throw new IllegalArgumentException("No such player controller with name");
    }


    public PlayerController[] getPlayerControllers() {
        return playerControllers;
    }


    public AdversaryController[] getAdversaryControllers() {
        return adversaryControllers;
    }

    public Observer[] getObservers() {
        return observers;
    }

}
