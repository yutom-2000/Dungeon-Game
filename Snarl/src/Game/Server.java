package Game;

import Game.controllers.Command.CommandInitialize;
import Game.controllers.Observer;
import Game.gameManager.NetworkGameManager;
import Game.gameState.level.Level;
import Game.ruleChecker.RuleChecker;
import com.google.gson.JsonParser;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import testHarness.Level.TestHarness.StateTest.TestParser;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class Server {
    public static ArgumentParser createParser() {
        ArgumentParser parser = ArgumentParsers.newFor("Server").build();
        parser.addArgument("--level").setDefault("Snarl.levels");
        parser.addArgument("--clients").type(Integer.class).setDefault(4);
        parser.addArgument("--wait").type(Integer.class).setDefault(60);
        parser.addArgument("--observer").action(Arguments.storeTrue());
        parser.addArgument("--address").setDefault("127.0.0.1");
        parser.addArgument("--port").type(Integer.class).setDefault(45678);
        parser.addArgument("--adversaries").type(Integer.class).setDefault(0);
        return parser;
    }


    public static void main(String[] args) throws IOException {
        ArgumentParser parser = createParser();
        
        String filename = "", address = "";
        int clients = 0, wait = 0, portNum = 0, adversaries = 0;
        boolean observer = false;


        try {
            Namespace ns = parser.parseArgs(args);
            filename = ns.getString("level");
            clients = ns.getInt("clients");
            wait = ns.getInt("wait");
            observer = ns.get("observer");
            address = ns.get("address");
            portNum = ns.getInt("port");
            adversaries = ns.getInt("adversaries");

            
        } catch (ArgumentParserException e) {
            System.out.println(e);
        }

        InetAddress addr = InetAddress.getByName(address);
        //System.out.println(portNum); System.out.println(addr); //for debugging
        ServerSocket serverSocket = new ServerSocket(portNum, 4, addr);
        serverSocket.setSoTimeout(wait * 1000);

        NetworkGameManager gameManager = new NetworkGameManager(serverSocket);
        RuleChecker rulechecker = new RuleChecker().setNumPlayers(clients).setNumAdversaries(adversaries);
        gameManager.addRuleChecker(rulechecker);
        int connected = 0;

        Level[] levels = null;

        try {
            String levelsJsonString = String.join("\n", (Files.readAllLines(Paths.get(filename))));
            String[] levelJsonArray = levelsJsonString.split("\n\n");
            levels = new Level[levelJsonArray.length];
            for (int i = 0; i < levelJsonArray.length; i++) {
                String json = levelJsonArray[i];
                levels[i] = TestParser.parseLevel(JsonParser.parseString(json).getAsJsonObject());
            }
        } catch (NoSuchFileException e) {
            System.out.println("Ensure the .levels file is in the same directory as the executable");
            System.out.println(Paths.get(filename));
            System.exit(-1);
        }

        if (levels == null) throw new IllegalArgumentException("Ensure that levels are not null");

        while (connected != clients + adversaries) { //todo add adversary count
            try {
                gameManager.promptRegister();
            } catch (InterruptedIOException t) {
                System.out.println("Done waiting... starting game");
                break;
            } catch (Exception e) {
                System.out.println(e);
            }
            connected++;
        }
        if (observer) {
            gameManager.acceptCommand(new Observer("observer").commandRegister());
        }

        gameManager.playGame(levels);
    }
}
