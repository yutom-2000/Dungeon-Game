package com.company.NumJSON;

import java.util.ArrayList;

/**
 * Represents an array of NumJSON
 */
public class ArrayNumJSON implements NumJSON{
    ArrayList<NumJSON> value;


    public ArrayNumJSON(ArrayList<NumJSON> value) {
        this.value = value;
    }

    @Override
    public int sum() {
        int sum = 0;
        for (NumJSON n: value) {
            sum+= n.sum();
        }
        return sum;
    }

    @Override
    public int product() {
        int product = 1;
        for (NumJSON n: value) {
            product*=n.product();
        }
        return product;
    }

    @Override
    public Object getData() {
        ArrayList<Object> data = new ArrayList<>();
        for (NumJSON n : value) {
            data.add(n.getData());
        }
        return data;
    }
}
