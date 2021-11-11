import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class TravellerClient {

  //Main methods for json value read
  public static void main(String[] args) throws IOException {
    // local host: 127.0.0.1
    // port: 8088

    //Global variables:
    //Represents the key of command, and possible pairs of the params for each command
    Map<String, List<Pair>> jsonData;
    JsonReader reader = new JsonReader(new InputStreamReader(System.in, "UTF-8"));
    String temp;

    //Set up the socket for client-server connection
    Socket s = new Socket("127.0.0.1", 8088);
    Scanner sc1 = new Scanner(s.getInputStream());
    System.out.println("Please enter any valid json command");
    jsonData = readJSON(reader);
    PrintStream p = new PrintStream(s.getOutputStream());
    p.println(jsonData);

    //STEP2:
    //Accept the result from server
    temp = sc1.next();
    System.out.println(temp);

  }

  //read the json value from stdin
  private static Map<String, List<Pair>> readJSON(JsonReader reader)
      throws IOException {
    String key = "";
    String temp = "";
    Map<String, List<Pair>> jsonData = new HashMap<>();
    List<Pair> params = new ArrayList<>();
    Pair pair = null;
    int counts = 0;
    Network network = new Network();

    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("command") && counts <= 1) {
        key = reader.nextString();
      } else if (name.equals("params") && key.equals("roads")) {
        counts += 1;
        params = readArray(reader);
        //Invoke the server methods for initializing the network of nodes
        network = initialize(params);
        //send the output to player
        temp = "All roads are created successfully to town network.";
        System.out.println(temp);
      } else if (name.equals("params") && (key.equals("place"))) {
        pair = readPlace(reader, network);
        //Invoke the server methods for placing the characters
        //update the node with character's information for each pair
        if (pair != null) {
          TravellerServer.placePlayer(new Character(pair.getFirst()), pair.getSecond());
          //notify the player the character is placed successfully to given town
          temp = pair.getFirst() + "is successfully placed at " + pair.getSecond();
          System.out.println(temp);
        }
      } else if (name.equals("params") && key.equals("passage-safe?")) {
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
        System.exit(0);
        //Shut down the client because the invalid command
      }
    }
    reader.endObject();

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

  //read the json array
  private static List<Pair> readArray(JsonReader reader) throws IOException {
    List<Pair> roads = new ArrayList();
    reader.beginArray();
    while (reader.hasNext()) {
      roads.add(readObjects(reader));
    }
    reader.endArray();
    return roads;
  }


  //read the json object
  private static Pair readObjects(JsonReader reader) throws IOException {
    String value1 = "";
    String value2 = "";
    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("from")) {
        value1 = reader.nextString();
      } else if (name.equals("to")) {
        value2 = reader.nextString();
      } else {
        //if the command in params is not valid
        System.exit(0);
      }
    }
    reader.endObject();
    return new Pair(value1, value2);
  }

  //read the command "place"
  private static Pair readPlace(JsonReader reader, Network network) throws IOException {
    String character = "";
    String town = "";
    List<Node> towns = network.nodeList;
    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("character")) {
        character = reader.nextString();
      } else if (name.equals("town")) {
        town = reader.nextString();
        if (!validTown(town, towns)) {
          //discarded the current command
          town = null;
        }
      } else {
        //if the command in params is not valid
        System.exit(0);
      }
    }
    reader.endObject();
    if (town != null) {
      return new Pair(character, town);
    } else {
      return null;
    }
  }

  //read the params of command "safee-passage?"
  private static Pair readSafePass(JsonReader reader, Network network) throws IOException {
    String character = "";
    String town = "";
    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("character")) {
        character = reader.nextString();
      } else if (name.equals("town")) {
        town = reader.nextString();
      } else {
        //if the command in params is not valid
        System.exit(0);
      }
    }
    reader.endObject();

    if (validTown(town, network.nodeList) && validCharacter(character, network.nodeList)) {
      return new Pair(character, town);
    } else {
      return null;
    }
  }

  //create the new node and network according to the list pair read from json
  private static Network initialize(List<Pair> nodes) {
    int keyValue = 1;
    List<Node> nodeList = new ArrayList<>();
    Network network = new Network();
    for (int i = 0; i < nodes.size(); i++) {
      Pair pairOfNodes = nodes.get(i);
      //initialize the Node
      Node from = new TravellerServer.Node(keyValue, pairOfNodes.getFirst());
      keyValue += 1;
      Node to = new TravellerServer.Node(keyValue, pairOfNodes.getSecond());
      keyValue += 1;
      //add neighbors to each Node
      from.addNeighbors(to);
      to.addNeighbors(from);
      //add the current node to network
      nodeList.add(from);
      nodeList.add(to);
      network = TravellerServer.create(nodeList);
    }
    return network;
  }

  //check whether the current town is in town network
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

  //check whether the current character is placed at the town
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
