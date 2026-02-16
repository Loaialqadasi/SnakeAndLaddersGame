package com.lqad.snakes.model;

public class Card {
    
    private final int value;

    public Card(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override  //not important but better for run-time compiling and correcteness
    public String toString(){  //className@Hexa (Card@4e342fh)
        return "Card[" + value + "]";
    }
    
}
