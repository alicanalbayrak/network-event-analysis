package com.gilmour.nea.core;

/**
 * Created by gilmour on Jul, 2017.
 */
public class MutableAggregatorInt {
    private int value = 0;

    public MutableAggregatorInt(int initial) {
        value = initial;
    }

    public void increment(int number) {
        value = value + number;
    }

    public int get() {
        return value;
    }
}
