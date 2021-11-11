import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MockTravellerServer {

  public static void main(String[] args) throws IOException {
    // local host: 127.0.0.1
    // port: 8080

    //STEP1:
    //Take the number from user
    //Create the socket -> Print stream object P and passing it to the socket

    int number, temp;
    ServerSocket s1 = new ServerSocket(8080);
    Socket ss = s1.accept();
    Scanner sc = new Scanner(ss.getInputStream());
    number = sc.nextInt();

    //STEP2:
    //Accept the result from server
    temp = number * 2;
    PrintStream p = new PrintStream(ss.getOutputStream());
    p.println(temp);
  }

  //IN NODE CLASS
  public static void createNode(int key, String name) {
  }

  public static void addNeighbors(Node node) {
  }

  public static boolean findNode(Node node) {
    return false;
  }

  public static void placePlayer(Character character) {
  }

  //IN NETWORK CLASS
  public static Network create(List<Node> nodes) {
    return new Network(nodes);
  }

  public static void planRoute(Node from, Node to) {
  }

  public static void placePlayer(Character character, String nodeName) {
  }
}

class Node {

  int key;
  String name;
  List<Node> neighbors;
  Character character;

  Node(int key, String name, List<Node> neighbors, Character character) {
    this.key = key;
    this.name = name;
    this.neighbors = neighbors;
    this.character = character;
  }

  Node(int key, String name) {
    this.key = key;
    this.name = name;
    this.neighbors = new ArrayList<>();
    this.character = null;
  }

  public void addNeighbors(Node node) {
  }

  public boolean findNode(Node node) {
    return false;
  }

  public void placePlayer(Character character) {
  }
}


class Network {

  List<Node> nodeList;

  Network(List<Node> nodeList) {
    this.nodeList = nodeList;
  }

  public Node getTown(String name) {
    return null;
  }
}

class Character {

  String name;

  Character(String name) {
    this.name = name;
  }

}
