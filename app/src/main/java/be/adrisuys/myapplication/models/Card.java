package be.adrisuys.myapplication.models;

import java.io.Serializable;

public class Card implements Serializable {

    private String name;
    private int value;

    public Card(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
