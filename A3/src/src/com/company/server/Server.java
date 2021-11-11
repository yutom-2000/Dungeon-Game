package com.company.server;

import com.company.NumJSON.NumJSON;
import com.company.NumJSON.NumJSONParser;
import com.company.Result;
import com.google.gson.Gson;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private void connect(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8000.");
            System.exit(1);
        }
        clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
    }

    private void initializeIO() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
        } catch (IOException e) {
            System.err.println("Failed to initialize outputstream");
            System.exit(1);
        }
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Failed to initialize inputstream");
            System.exit(1);
        }
    }

    private void showResults() throws IOException {
        String inputLine;
        String inputString = "";

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.equals("END")) {
                ArrayList<NumJSON> inputNumJSON = NumJSONParser.parseAll(new StringReader(inputString));
                ArrayList<Result> results = getResults("--sum", inputNumJSON);
                Gson gson = new Gson();
                for (Result r : results) {
                    out.println(gson.toJson(r));
                }
                break;
            }
            inputString += inputLine;

        }
    }

    public void start(int port) {
        serverSocket = null;
        connect(port);

        initializeIO();

        try {
            showResults();
        } catch (IOException e) {
            System.err.println("Unable to read line");
            System.exit(1);
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    /**
     * Return results of operation given
     *
     * @param operation specified operation
     * @param inputs    input ArrayList of NumJSON
     * @return
     */
    private static ArrayList<Result> getResults(String operation, ArrayList<NumJSON> inputs) {
        ArrayList<Result> results = new ArrayList();
        if (operation.equals("--sum")) {
            for (NumJSON n : inputs) {
                results.add(new Result(n.getData(), n.sum()));
            }
        } else if (operation.equals("--product")) {
            for (NumJSON n : inputs) {
                results.add(new Result(n.getData(), n.product()));
            }
        } else {
            throw new IllegalArgumentException("Unrecognized Operation");
        }
        return results;
    }
}
