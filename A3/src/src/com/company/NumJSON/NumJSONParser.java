package com.company.NumJSON;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class NumJSONParser {

    /**
     * Parse all information from a reader into an ArrayList of NumJSON's
     * top level parser
     * @param reader Reader input
     * @return ArrayList of NumJSON's parsed from the reader
     */
    public static ArrayList<NumJSON> parseAll(Reader reader) {
        ArrayList<NumJSON> inputs = new ArrayList<NumJSON>();
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);
        try {
            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();
                if (token.equals(JsonToken.END_DOCUMENT)) break;

                inputs.add(NumJSONParser.parse(jsonReader, token));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputs;
    }


    /**
     * Method to read json object
     *
     * @param reader reader to get numJson from
     * @return
     */
    private static ObjectNumJSON parseObject(JsonReader reader) {
        Gson gson = new Gson();
        NumJSON payload = new StringNumJSON("Init");
        JsonObject result = new JsonObject();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NAME)) {
                    String name = reader.nextName();
                    if (name.equals("payload")) { //case is payload
                        payload = parse(reader, reader.peek());
                        result.add(name, gson.toJsonTree(gson.toJson(payload.getData())));
                    } else {
                        result.add(name, JsonParser.parseReader(reader));
                    }
                }

            }
            reader.endObject();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return new ObjectNumJSON(result, payload);
    }

    /**
     * Parse one NumJson using given token
     *
     * @param read  reader
     * @param token token denoting type
     * @return NumJSON result of parse
     */
    public static NumJSON parse(JsonReader read, JsonToken token) {
        try {
            switch (token) {
                case NUMBER:
                    return new NumberNumJSON(read.nextInt());
                case STRING:
                    return new StringNumJSON(read.nextString());
                case BEGIN_ARRAY:
                    return new ArrayNumJSON(ReadJson(read));
                case BEGIN_OBJECT:
                    return parseObject(read);
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return new StringNumJSON("Error");
    }

    /**
     * Parses string into corresponding NumJSON object
     * Mainly used for arrays
     * @param reader Reader to get NumJSON from
     */
    public static ArrayList<NumJSON> ReadJson(JsonReader reader) {
        ArrayList<NumJSON> result = new ArrayList<NumJSON>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NUMBER)) {
                    result.add(new NumberNumJSON(reader.nextInt()));
                } else if (token.equals(JsonToken.STRING)) {
                    result.add(new StringNumJSON(reader.nextString()));
                } else if (token.equals(JsonToken.END_ARRAY)) {
                    reader.endArray();
                    break;
                }
            }
            reader.endArray();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return result;
    }

}