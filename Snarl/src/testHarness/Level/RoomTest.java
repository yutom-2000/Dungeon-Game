package testHarness.Level;

import Game.gameState.posn.Posn;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

/**
 * Class represents the testing harness
 */
public class RoomTest {

    public static void main(String[] args) {
        Gson gson = new Gson();
        StringReader readin = getStdin();
        JsonReader reader = new JsonReader(readin);
        Posn point;
        Posn origin;

        try {
            LevelTestInput tl = RoomTestParser.parseAll(reader);
            point = tl.getPosn();
            origin = tl.getRoom().origin;
            try {
                int[][] validMoves = tl.getValidMoves();
                JsonArray result = new JsonArray();
                result.add("Success: Traversable points from ");
                result.add(gson.toJsonTree(point.toArray()));
                result.add(" in room at ");
                result.add(gson.toJsonTree(origin.toArray()));
                result.add(" are ");
                result.add(gson.toJsonTree(validMoves));
                System.out.println(result.toString());

            } catch (IllegalArgumentException e) {
                JsonArray result = new JsonArray();
                result.add("Failure: point ");
                result.add(gson.toJsonTree(point.toArray()));
                result.add(" is not in room at ");
                result.add(gson.toJsonTree(origin.toArray()));
                System.out.println(result.toString());
            }

        } catch (IOException e) {
            System.out.println("Invalid json input");
        }
    }

    private static StringReader getStdin() {
        Scanner scanner = new Scanner(System.in);
        String stringInput = "";
        while (scanner.hasNextLine()) {
            stringInput += scanner.nextLine();
        }
        return new StringReader(stringInput);
    }
}
