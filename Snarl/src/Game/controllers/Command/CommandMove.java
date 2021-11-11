package Game.controllers.Command;

import Game.controllers.EntityController;
import Game.controllers.playerController.PlayerController;
import Game.gameManager.GameManager;
import Game.gameManager.Updates.Update;
import Game.gameState.ActorPosition;
import Game.gameState.ObjectPosition;
import Game.gameState.direction.Direction;
import Game.gameState.entities.Adversary;
import Game.gameState.entities.Entity;
import Game.gameState.entities.EntityStatus;
import Game.gameState.entities.Player;
import Game.gameState.interaction.MoveResult;
import Game.gameState.level.tiles.Tile;
import Game.gameState.posn.Posn;
import Game.ruleChecker.RuleChecker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.IOException;

/**
 * Command for player movement
 */
public class CommandMove extends Command {

    Posn dest;
    boolean isPlayer; //true if the issuer of command is a player


    /**
     * Constructors for move Command.
     * @param name  the name of entity needs to move
     * @param dest the destination the entity plan to move
     * @param isPlayer whether the entity is player
     */
    public CommandMove(String name, Posn dest, boolean isPlayer) {
        super(name);
        this.dest = dest;
        this.isPlayer = isPlayer;
    }

    @Override
    public void visitGameManager(GameManager manager) {
        //step 1: increment the number of turns
        manager.incrementTurn();
        //step 2: get the moving objcts: either player or adversary
        Entity subject = null;
        Entity predicate = null;
        if (isPlayer) {
            subject = getPlayerWithName();
        } else {
            subject = getAdversaryWithName();
            predicate = manager.getGameState().getLevel().tileAt(dest).getEntity();
        }
        if (subject == null) {
            throw new IllegalArgumentException("What the hell if rule checker says ok should not throw");
        }

        //step 3: do move and interaction in gameState
        MoveResult result = manager.getGameState()
                .moveEntity(subject, subject.getPosn(), dest);
        //step 4: make updates
        EntityController c = null;
        if (isPlayer) {
            PlayerController pc = getPlayerControllerByName(manager);
            pc.receiveResult(result);

            if (result == MoveResult.KEY) {
                pc.numTimesKey++;
            }

            if (subject.getStatus() == EntityStatus.DEAD) {
                pc.receiveUpdate(manager.generateEntityUpdate(subject));
                pc.numTimesDie++;
                pc.receiveUpdate(manager.generateEntityUpdate(subject));
            }

            if (subject.getStatus() == EntityStatus.EXITED) {
                pc.numTimesExit++;
            }
        }

        if (!isPlayer) {

            if (predicate != null && predicate.getStatus() == EntityStatus.DEAD) {
                PlayerController pc = getPlayerControllerByName(manager, predicate.getName());
                pc.numTimesDie++;
                pc.receiveUpdate(manager.generateEntityUpdate(predicate));
            }
        }
        manager.sendAllUpdates();
    }

    /**
     * Get a 5x5 square around the entity
     *
     * @param subject the focus entity want to make a move
     * @return the string of the limited view of the entity
     */
    private String getSubsetString(Entity subject) {
        Posn posn = subject.getPosn();
        Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
        Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
        return gameState.getLevel().getSubsetString(topleft, bottomRight);
    }

    private Tile[][] getSubsetTile(Entity subject) {
        Posn posn = subject.getPosn();
        Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
        Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
        return gameState.getGameState().getLevel().getSubsetTile(topleft, bottomRight);
    }

    private ActorPosition[] getActorPositionList(Entity subject) {
        Posn posn = subject.getPosn();
        Posn topleft = new Posn(posn.getRow() - 2, posn.getCol() - 2);
        Posn bottomRight = new Posn(topleft.getRow() + 5, topleft.getCol() + 5);
        Tile[][] tiles = gameState.getLevel().getSubsetTile(topleft, bottomRight);

        return gameState.getLevel().getActorPositionList(tiles);
    }

    /**
     * Get a player controller by name
     *
     * @return the PlayerController of the given name
     */
    private PlayerController getPlayerControllerByName(GameManager manager) {
        for (PlayerController pc : manager.getPlayerControllers()) {
            if (pc.getName().equals(name)) {
                return pc;
            }
        }
        throw new IllegalArgumentException("No such player controller with name");
    }

    private PlayerController getPlayerControllerByName(GameManager manager, String name) {
        for (PlayerController pc : manager.getPlayerControllers()) {
            if (pc.getName().equals(name)) {
                return pc;
            }
        }
        throw new IllegalArgumentException("No such player controller with name");
    }

    /**
     * Get a player with the name given from sender of this command look in the gamestate fetched when
     * command passed thru manager's accept method
     *
     * @return the player of the given name.
     */
    private Player getPlayerWithName() {
        for (Player p : gameState.getPlayers()) {
            if (p.getName().equals(this.name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get the adversary with the name given from the sender of this command Look in the gameState
     * fetched when command passed thru manager's accept method
     *
     * @return the adversary of the given name.
     */
    private Adversary getAdversaryWithName() {
        for (Adversary a : gameState.getAdversaries()) {
            if (a.getName().equals(this.name)) {
                return a;
            }
        }
        return null;
    }


    @Override
    public void visitRuleChecker(RuleChecker ruleChecker) {
        Entity subject = null;
        if (isPlayer) {
            subject = getPlayerWithName();
        } else {
            subject = getAdversaryWithName();
        }

        //RULE 1 : move is not valid if cannot find player
        if (subject == null) {
            return;
        }
        //RULE 2 : Entity MUST start move alive
        EntityStatus status = subject.getStatus();
        if (status != EntityStatus.ALIVE) {
            return;
        }
        //RULE 3: adversary cannot stay in place
        Posn origin = subject.getPosn();
        if (subject.isAdversary() && (origin == dest || dest == null)) {
            return;
        }
        //RULE 4: ensure dest will never be null
        if (dest == null) {
            dest = subject.getPosn();
        }
        //RULE 5: ensure the dest will not out of bound
        if (dest.getRow() < 0 || dest.getCol() < 0) {
            return;
        }
        //RULE 6: MoveCounts - check if violate move distance for player/adversary (2/1 respectively)
        int dist = Posn.distance(origin, dest);
        if (dist > subject.getMaxMoves()) {
            return;
        }
        //RULE 7:  check that adjacent tile is moveable/stepable
        if (dist == 2) {
            if (!checkAdjacent(origin, dest, subject)) {
                return; //no adjacency means this step is illegal
            }
        }
        Tile destTile = gameState.getGameState().getLevel().tileAt(dest);

        //can always stay?
        if (subject.getPosn().equals(dest)) {
            makeValid();
        }
        //can step?
        if (subject.canStep(destTile)) {
            makeValid();
        }
    }

    @Override
    public void invalidHandle(GameManager manager) {
        //do nothing, manager already has logic to retry
    }

    /**
     * For a move of distance two, check that the dest and origin share a steppable tile 1 dist away
     * @return
     */
    private boolean checkAdjacent(Posn origin, Posn dest, Entity subject) {
        boolean hasStep = false;
        if (origin.getRow() - dest.getRow() < 0) { //dest is south of origin
            hasStep = hasStep || subject.canStep(gameState.getLevel().tileAt(origin.goDirection(Direction.DOWN)));
        }
        else {
            hasStep = hasStep || subject.canStep(gameState.getLevel().tileAt(origin.goDirection(Direction.UP)));
        }
        if (origin.getCol() - dest.getCol() < 0) { //dest is right of origin
            hasStep = hasStep || subject.canStep(gameState.getLevel().tileAt(origin.goDirection(Direction.RIGHT)));
        }
        else {
            hasStep = hasStep || subject.canStep(gameState.getLevel().tileAt(origin.goDirection(Direction.LEFT)));
        }
        return hasStep;
    }

    @Override
    public String toString() {
        String result = "";
        if (isPlayer) {
            result = "Command move player: ";
        } else {
            result = "Command move adversary: ";
        }
        result += this.name;
        result += " ";
        result += this.dest;
        return result;
    }

    public static Command parseCommandMove(String jsonMove, String name, boolean isPlayer) {
        try {
            JsonObject jo = JsonParser.parseString(jsonMove).getAsJsonObject();
            if (!jo.get("type").getAsString().equals("move")) throw new IllegalArgumentException("bad format jsonMove");
            return new CommandMove(name, TestParser.parsePosn(jo.get("to").getAsJsonArray()), isPlayer);
        } catch (JsonParseException | IOException e) {
            System.out.println(e);
        }
        throw new IllegalArgumentException("Bad jsonMove provided");
    }
}
