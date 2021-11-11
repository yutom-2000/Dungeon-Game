package com.company;

import com.company.NumJSON.NumJSON;
import com.google.gson.JsonElement;

public class Result {
    Object object;
    int total;

    public Result(Object og, int value) {
        this.object = og;
        this.total = value;
    }

}
