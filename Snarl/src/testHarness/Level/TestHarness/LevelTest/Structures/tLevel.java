package testHarness.Level.TestHarness.LevelTest.Structures;

/**
 * Represent a level following testing schema
 */
public class tLevel {
    tRoom[] rooms;
    tHallway[] hallways;
    tObject[] objects;

    public tLevel(tRoom[] rooms, tHallway[] hallways, tObject[] objects) {
        this.rooms = rooms;
        this.hallways = hallways;
        this.objects = objects;
    }

    public tRoom[] getRooms() {
        return this.rooms;
    }

    public tHallway[] getHallways() {
        return this.hallways;
    }

    public tObject[] getObjects() {
        return  this.objects;
    }
}
