package com.lqad.snakes.model;


import java.util.Random;


public class Deck {

    private final Random random;
    private final int maxMoveValue;

    public Deck(int maxMoveValue) {
        this.random = new Random();
        this.maxMoveValue = maxMoveValue;  // to make the random in a spicified limit

    }

    public Card draw() {
       int value = random.nextInt(maxMoveValue) + 1;

        return new Card(value);

    }

    
}
