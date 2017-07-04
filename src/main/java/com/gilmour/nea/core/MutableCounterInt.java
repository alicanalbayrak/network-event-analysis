package com.gilmour.nea.core;

/**
 * Created by gilmour on Jul, 2017.
 */
public class MutableCounterInt {

    private int value = 1;

    public void increment() {
        ++value;
    }

    public int get() {
        return value;
    }
}
