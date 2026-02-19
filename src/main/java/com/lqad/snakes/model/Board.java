package com.lqad.snakes.model;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private static final int FINAL_POSITION = 100;
    private final Map<Integer, Integer> snakes = new HashMap<>();
    private final Map<Integer, Integer> ladders = new HashMap<>();

    public Board() {
        initializeSnakes();
        initializeLadders();
    }

    private void initializeSnakes() {
        // Adjusted for Zigzag board flow if necessary, but logic remains ID-based
        snakes.put(97, 73); snakes.put(70, 55); snakes.put(52, 42);
        snakes.put(25, 12); snakes.put(85, 74); snakes.put(36, 6);
        snakes.put(65, 59); snakes.put(91, 72);
    }

    private void initializeLadders() {
        ladders.put(6, 25); ladders.put(11, 40); ladders.put(60, 85);
        ladders.put(46, 67); ladders.put(17, 69); ladders.put(2, 23);
        ladders.put(33, 49); ladders.put(77, 93);
    }

    public Map<Integer, Integer> getAllSnakesAndLadders() {
        Map<Integer, Integer> combined = new HashMap<>();
        combined.putAll(snakes);
        combined.putAll(ladders);
        return combined;
    }

    public int checkJump(int position) {
        if (snakes.containsKey(position)) return snakes.get(position);
        if (ladders.containsKey(position)) return ladders.get(position);
        return position;
    }

    // === CHANGE 1: BOUNCE BACK LOGIC ===
    public int resolvePosition(int currentPosition, int move) {
        int tentativePosition = currentPosition + move;
        
        if (tentativePosition > FINAL_POSITION) {
            // Calculate bounce
            int excess = tentativePosition - FINAL_POSITION;
            return FINAL_POSITION - excess;
        }
        
        return checkJump(tentativePosition);
    }

    public int getFinalPosition() { return FINAL_POSITION; }
}