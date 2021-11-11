package com.company.NumJSON;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Object NumJSON. Represents a generic NumJSON object.
 */
public class ObjectNumJSON implements NumJSON{
    JsonObject je;
    NumJSON payload;

    public ObjectNumJSON(JsonObject je, NumJSON payload) {
        this.je = je;
        this.payload = payload;
    }


    @Override
    public int sum() {
        return payload.sum();
    }

    @Override
    public int product() {
        return payload.product();
    }

    @Override
    public Object getData() {
        return je;
    }
}
