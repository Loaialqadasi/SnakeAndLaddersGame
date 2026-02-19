package com.lqad.snakes.engine;

import java.util.List;

import com.lqad.snakes.model.Player;

public class MoveResult {
    private final List<Integer> walkPath;
    private final int preJumpPosition;  // Where they land before snake/ladder
    private final int finalPosition;    // Where they go after snake/ladder
    private final boolean isLadder;
    private final boolean isSnake;
    private final Player potentialBlocker;

    public MoveResult(List<Integer> walkPath, int preJumpPosition, int finalPosition, 
                      boolean isLadder, boolean isSnake, Player potentialBlocker) {
        this.walkPath = walkPath;
        this.preJumpPosition = preJumpPosition;
        this.finalPosition = finalPosition;
        this.isLadder = isLadder;
        this.isSnake = isSnake;
        this.potentialBlocker = potentialBlocker;
    }

    public List<Integer> getWalkPath() { return walkPath; }
    public int getPreJumpPosition() { return preJumpPosition; }
    public int getFinalPosition() { return finalPosition; }
    public boolean isLadder() { return isLadder; }
    public boolean isSnake() { return isSnake; }
    public Player getPotentialBlocker() { return potentialBlocker; }
}