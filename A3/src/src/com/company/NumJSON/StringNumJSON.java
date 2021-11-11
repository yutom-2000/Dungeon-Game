package com.company.NumJSON;

/**
 * Represents a String NumJSON, represents a string
 */
public class StringNumJSON implements NumJSON{
    String value;

    public StringNumJSON(String value) {
        this.value = value;
    }

    @Override
    public int sum() {
        return 0;
    }

    @Override
    public int product() {
        return 1;
    }

    @Override
    public Object getData() {
        return this.value;
    }
}
