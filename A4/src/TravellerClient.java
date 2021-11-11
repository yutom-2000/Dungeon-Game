import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class represent the client for Traveller Game
 */
public class TravellerClient {

    String userName;
    String sessionId;

    TravellerClient(String userName) {
        this.userName = userName;
        this.sessionId = "-1";
    }

    /**
     * TCP connection between client and server
     *
     * @param args argument comes from command line
     * @throws IOException throws exception if the input is not well formatted.
     */
    public static void main(String[] args) throws IOException {
        //Global variables:
        String ip = "127.0.0.1";
        int port = 8080;
        String name = "Glorifrir Flintshoulder";
        Socket s;

        // starting up and connecting to the server via TCP
        if (args.length > 0) {
            ip = args[0];
            if (args.length > 1) {
                port = Integer.valueOf(args[1]);
            } if (args.length == 3) {
                name = args[2];
            }
        } else {
            //follow defaults
        }
        s = new Socket("localhost", port);
        //sign up name and receive [session id]
        TravellerClient client = new TravellerClient(name);
        System.out
                .println("Welcome:" + name + ". You are assigned to Traveller Game with session ID 1");
        JSONArray response = new JSONArray();
        //Creat the town and Processing Phase
        run(s);
    }

    /**
     * it performs three operations:
     * - request the user to specify the command they want to do
     * - read the answer from input stream json
     * - accordingly write the output on the output stream json object.
     */
    private static void run(Socket socket) {
        JSONObject output = new JSONObject();
        try {
            Map<String, List<Pair>> jsonData;
            System.out.println("Please enter any valid json command");
            JsonReader in = new JsonReader(new InputStreamReader(System.in, "UTF-8"));
            // Create request
            // sets up a network of towns with roads between them.
            Network network = setUp(in);
            jsonData = readJSON(in, network);
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(jsonData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Please enter the well-formed JSON input");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            System.out.println("Closed: " + socket);
        }
    }


    /**
     * set up the game with given town and road information from json input.
     */
    private static Network setUp(JsonReader reader) throws IOException {
        Network network = null;
        JSONObject output = new JSONObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            // Create request
            // sets up a network of towns with roads between them.
            if (name.equals("towns")) {
                network = initialTowns(reader);
            } else if (name.equals("roads")) {
                initialRoads(network, reader);
            } else {
                output.put("error", "not a request");
                output.put("object", reader);
            }
        }
        return network;
    }

    /**
     * invoke the method in server, creat the towns according to the json input
     */
    public static Network initialTowns(JsonReader reader) {
        List<Node> towns = new ArrayList<>();
        int primaryKey = 0;
        //read the json array follows with the key "towns"
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                primaryKey += 1;
                String nodeName = reader.nextString();
                Node town = new Node(primaryKey, nodeName);
                towns.add(town);
            }
            reader.endArray();
        } catch (Exception e) {
            // a create request is invalid --> SHUT DOWN
            System.exit(0);
        }
        //Create the network of town according to the town list we have
        Network network = new Network(towns);
        return network;
    }

    /**
     * invoke the method in server, connecting the towns in network
     */
    public static void initialRoads(Network network, JsonReader reader) {
        try {
            List<Pair> arraysOfRoads = readArray(reader, "from", "to");
            for (int i = 0; i < arraysOfRoads.size(); i++) {
                Pair pairOfNodes = arraysOfRoads.get(i);
                //initialize the Node
                String from = pairOfNodes.getFirst();
                String to = pairOfNodes.getSecond();
                boolean validRoads = checkValid(from, to, network);
                //if the road is not valid, discarded the current road
                if (validRoads) {
                    //add neighbors to each Node
                    Node townFrom = network.getTown(from);
                    Node townDestination = network.getTown(to);
                    townFrom.addNeighbors(townDestination);
                    townDestination.addNeighbors(townFrom);
                    //send the output to player
                    String temp = "All roads are created successfully to town network.";
                    System.out.println(temp);
                }
            }
        } catch (Exception e) {
            // a create request is invalid --> SHUT DOWN
            System.exit(0);
        }
    }

    /**
     * check whether the given two towns are in the current town network
     */
    private static boolean checkValid(String from, String to, Network network) {
        List<Node> listTown = network.nodeList;
        boolean findFrom = false;
        boolean findTo = false;
        for (int i = 0; i < listTown.size(); i++) {
            Node town = listTown.get(i);
            if (!findFrom) {
                findFrom = town.name.equals(from);
            }
            if (!findTo) {
                findTo = town.name.equals(to);
            }
        }
        return findFrom && findTo;
    }

    /**
     * read the json value from stdin
     */
    private static Map<String, List<Pair>> readJSON(JsonReader reader, Network network) {
        JSONObject output = new JSONObject();
        Map<String, List<Pair>> jsonData;
        String key = "";
        String temp = "";
        Pair pair = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                //Batch request
                if (name.equals("characters")) {
                    key = name;
                    List<Pair> characters = readArray(reader, "name", "town");
                    //adds characters to the town network
                    addCharacter(characters, network);
                } else if (name.equals("query")) {
                    key = name;
                    pair = readSafePass(reader, network);
                    if (pair == null) {
                        //notify the player the character cannot move to the new town
                        temp = pair.getFirst() + "could not safely passage to " + pair.getSecond();
                        System.out.println(temp);
                    } else {
                        //notify the player the character successfully move to the new town
                        temp = pair.getFirst() + "could safely passage to " + pair.getSecond();
                        System.out.println(temp);
                    }
                } else {
                    output.put("error", "not a request");
                    output.put("object", reader);
                    System.exit(0);
                    //Shut down the client because the invalid command
                }
            }
            reader.endObject();
        } catch (Exception e) {
            // a batch request is ill-formed --> SHUT DOWN
            System.exit(0);
        }
        //prepare the readable java object from json value for parsing into server
        jsonData = manipulateJson(pair, key);
        return jsonData;
    }

    /**
     * add characters to the exosted town
     */
    private static void addCharacter(List<Pair> characters, Network network) {
        JSONObject jo = new JSONObject();
        JSONArray response = new JSONArray();
        for (int i = 0; i < characters.size(); i++) {
            Pair pair = characters.get(i);
            String player = pair.getFirst();
            Character c = new Character(player);
            String town = pair.getSecond();
            List<Node> nodes = network.nodeList;
            boolean validCharacterTown = validCharacter(player, nodes) && validTown(town, nodes);
            if (validCharacterTown) {
                Node townAddChararact = network.getTown(town);
                townAddChararact.placePlayer(c);
            } else {
                //give response to player
                jo.put("name", player);
                jo.put("town", town);
                response.put("invalid placement");
                response.put(jo);
            }
            System.out.println(response);
        }
    }

    /**
     * read the json array
     */
    private static List<Pair> readArray(JsonReader reader, String k1, String k2) throws IOException {
        List<Pair> character = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            character.add(readObjects(reader, k1, k2));
        }
        reader.endArray();
        return character;
    }


    /**
     * read the json object
     */
    private static Pair readObjects(JsonReader reader, String key1, String key2) throws IOException {
        String value1 = "";
        String value2 = "";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(key1)) {
                value1 = reader.nextString();
            } else if (name.equals(key2)) {
                value2 = reader.nextString();
            } else {
                //if the command in params is not valid
                System.exit(0);
            }
        }
        reader.endObject();
        return new Pair(value1, value2);
    }


    /**
     * read the params of command "safee-passage?"
     */
    private static Pair readSafePass(JsonReader reader, Network network) {
        String character = "";
        String town = "";
        JSONObject jo = new JSONObject();
        JSONArray response = new JSONArray();
        boolean validQuery = false;
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("character")) {
                    character = reader.nextString();
                } else if (name.equals("destination")) {
                    town = reader.nextString();
                } else {
                    throw new IllegalArgumentException("Invalid query request");
                }
                validQuery = validTown(town, network.nodeList) &&
                        validCharacter(character, network.nodeList);
            }
            reader.endObject();
        } catch (Exception e) {
            //if the command in params is not valid
            System.exit(0);
        }

        //gives the response to player when they query about the roads
        jo.put("character", character);
        jo.put("destination", town);
        response.put("the response for");
        response.put(jo);
        response.put("is");
        response.put(validQuery);

        if (validQuery) {
            return new Pair(character, town);
        } else {
            return null;
        }
    }

    private static Map<String, List<Pair>> manipulateJson(Pair pair, String key) {
        Map<String, List<Pair>> jsonData = new HashMap<>();
        List<Pair> params = new ArrayList<>();
        if (pair != null) {
            if (jsonData.get(key) != null) {
                List<Pair> preParam = jsonData.get(key);
                preParam.add(pair);
            } else {
                if (params.size() == 0) {
                    params.add(pair);
                }
                jsonData.put(key, params);
            }
        }
        return jsonData;
    }

    /**
     * check whether the current town is in town network
     */
    private static boolean validTown(String n, List<Node> nodes) {
        boolean valid = true;
        for (int i = 0; i < nodes.size(); i++) {
            Node town = nodes.get(i);
            valid = town.name.equals(n);
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    /**
     * check whether the current character is placed at the town
     */
    private static boolean validCharacter(String characterName, List<Node> nodes) {
        boolean existCharacter = true;
        for (int i = 0; i < nodes.size(); i++) {
            Node town = nodes.get(i);
            existCharacter = town.character.name.equals(characterName);
            if (existCharacter) {
                break;
            }
        }
        return existCharacter;
    }
}
