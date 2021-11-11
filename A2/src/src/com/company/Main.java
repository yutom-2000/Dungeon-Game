package com.company;

import com.company.NumJSON.*;
import com.google.gson.*;


import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Returns a StringReader containing contents of stdin
     *
     * @return StringReader with contents of stdin
     */
    private static StringReader getStdin() {
        Scanner scanner = new Scanner(System.in);
        String stringInput = "";
        while (scanner.hasNextLine()) {
            stringInput += scanner.nextLine();
        }
        return new StringReader(stringInput);
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


    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Need to specify \"--sum\" or \"--product\"");
        }
        Gson gson = new Gson();

        StringReader sr = getStdin();
        ArrayList<NumJSON> inputs = NumJSONParser.parseAll(sr);

        ArrayList<Result> results = getResults(args[0], inputs);

        for (Result r : results) {
            System.out.println(gson.toJson(r));
        }

    }
}
