package com.company.NumJSON;

/**
 * Represents a Numeric NumJSON, representing a single int
 */
public class NumberNumJSON implements NumJSON{
    int value;

    public NumberNumJSON(int value) {
        this.value = value;
    }

    @Override
    public int sum() {
        return this.value;
    }

    @Override
    public int product() {
        return this.value;
    }

    @Override
    public Object getData() {
        return value;
    }
}
