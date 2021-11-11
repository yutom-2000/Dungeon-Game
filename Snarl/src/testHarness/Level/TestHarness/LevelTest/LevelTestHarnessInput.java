package testHarness.Level.TestHarness.LevelTest;

import Game.gameState.posn.Posn;
import testHarness.Level.TestHarness.LevelTest.Enums.TileLocationType;
import testHarness.Level.TestHarness.LevelTest.Enums.TileObjectEnum;
import testHarness.Level.TestHarness.LevelTest.Structures.tHallway;
import testHarness.Level.TestHarness.LevelTest.Structures.tLevel;
import testHarness.Level.TestHarness.LevelTest.Structures.tObject;
import testHarness.Level.TestHarness.LevelTest.Structures.tRoom;

/**
 * Holds all the input of a single testHarnessInput json
 * level: the level data input
 * position: the position for testing
 */
public class LevelTestHarnessInput {
    tLevel level;
    Posn position;

    public LevelTestHarnessInput(tLevel level, Posn position) {
        this.level = level;
        this.position = position;
    }

    public LevelTestHarnessResult test() {
        boolean traversable = this.getTraversable();
        TileObjectEnum object = this.getObject();

        TileLocationType type = TileLocationType.VOID;
        if (traversable) {
            if (this.traversable(this.level.getRooms())) {
                type = TileLocationType.ROOM;
            }
            if (this.traversable(this.level.getHallways())) {
                type = TileLocationType.HALLWAY;
            }
        }

        Posn[] reachable = new Posn[0];
        if (type.equals(TileLocationType.HALLWAY)) {
            tHallway hallway = this.getHallwayAtPosition();
            tRoom[] reachableRooms = this.getConnectedRooms(hallway);
            reachable = new Posn[2];
            reachable[0] = reachableRooms[0].getOrigin();
            reachable[1] = reachableRooms[1].getOrigin();
        }
        else if (type.equals(TileLocationType.ROOM)) {
            reachable = this.getRoomsConnectedToRoom(this.getRoomContainingPoint(this.position));
        }

        return new LevelTestHarnessResult(traversable, object, type, reachable);
    }

    /**
     * Get room containing the posn
     * @param p
     * @return room containing given posn
     */
    public tRoom getRoomContainingPoint(Posn p) {
        tRoom[] rooms = this.level.getRooms();
        for (tRoom room : rooms) {
            if (room.inRoomSteppable(p)) {
                return room;
            }
        }
        throw new IllegalArgumentException("Given p is not in any room");
    }

    public Posn[] getRoomsConnectedToRoom(tRoom room) {
        tHallway[] hallways = this.level.getHallways();
        Posn[] result = new Posn[0];
        for (tHallway hallway : hallways) {
            if (room.inRoomSteppable(hallway.getFrom())) {
                result = Posn.append(result, this.getRoomOriginContainingPoint(hallway.getTo()));
            }
            else if (room.inRoomSteppable(hallway.getTo())) {
                result = Posn.append(result, this.getRoomOriginContainingPoint(hallway.getFrom()));
            }
        }
        return result;
    }

    /**
     * Get origin of room containing Posn p as a walkable tile
     * @param p
     * @return origin of room containing p as a walkable tile
     */
    public Posn getRoomOriginContainingPoint(Posn p) {
        tRoom[] rooms = this.level.getRooms();
        for (tRoom room : rooms) {
            if (room.inRoomSteppable(p)) {
                return room.getOrigin();
            }
        }
        throw new IllegalArgumentException("Given position is not in any rooms");
    }

    /**
     * Get the hallway that this point is located within
     * @return
     */
    private tHallway getHallwayAtPosition() {
        tHallway[] hallways = this.level.getHallways();
        for (tHallway hallway : hallways) {
            Posn[] tiles = hallway.getWalkablePosns();
            for (Posn tile : tiles) {
                if (tile.equals(this.position)) {
                    return hallway;
                }
            }
        }
        throw new IllegalArgumentException("The position is not within a hallway");
    }

    /**
     * Get list of rooms connected by this hallway (should be only two)
     * @return list of rooms connected by this hallway
     */
    private tRoom[] getConnectedRooms(tHallway hallway) {
        tRoom[] rooms = this.level.getRooms();
        tRoom[] result = new tRoom[2];
        int i = 0;
        for (tRoom room : rooms) {
            if (room.inRoomSteppable(hallway.getFrom())) {
                result[i] = room;
                i++;
            }
            else if (room.inRoomSteppable(hallway.getTo())) {
                result[i] = room;
                i++;
            }
        }
        return result;
    }

    private TileObjectEnum getObject() {
        tObject[] objects = level.getObjects();
        for (tObject object : objects) {
            if (object.getPosition().equals(this.position)) {
                return object.getType();
            }
        }
        return TileObjectEnum.NONE;
    }

    private boolean getTraversable() {
        tRoom[] rooms = level.getRooms();
        tHallway[] hallways = level.getHallways();
        boolean result = false;

        result = result || this.traversable(rooms);
        result = result || this.traversable(hallways);

        return result;

    }

    /**
     * Determine if position is in a walkable room
     * @param rooms rooms to check if can walk in
     * @return True if there is at least one point where the posn can walk in.
     */
    private boolean traversable(tRoom[] rooms) {
        boolean result = false;

        for (tRoom room : rooms) {
            result = result || room.inRoomSteppable(this.position);
        }
        return result;
    }

    private boolean traversable(tHallway[] hallways) {
        boolean result = false;

        for (tHallway hallway : hallways) {
            for (Posn p : hallway.getWalkablePosns()) {
                result = result || p.equals(this.position);
            }
        }
        return result;
    }
}
