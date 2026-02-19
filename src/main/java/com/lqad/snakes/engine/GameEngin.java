package com.lqad.snakes.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lqad.snakes.model.Ability;
import com.lqad.snakes.model.Board;
import com.lqad.snakes.model.Card;
import com.lqad.snakes.model.Deck;
import com.lqad.snakes.model.Player;

public class GameEngin {

    private final Deck deck;
    private final Board board;
    private final List<Player> players;
    private final Random random = new Random();

    private int currPlayerIndex = 0;

    private boolean gameover = false;


    public GameEngin(List<String> playerNames) {
        this.deck = new Deck(6);
        this.players = new ArrayList<>();
        this.board = new Board();

        for (String name : playerNames) {
            players.add(new Player(name));
        }
    }

    public Player getCurrentPlayer() { return players.get(currPlayerIndex); }
    public Board getBoard() { return board; }
    public List<Player> getPlayers() { return players; }

    public int rollDice() {
        Card card = deck.draw();

        return card.getValue();
    }

    // this to make getting a feature card by 10% each 
    public Ability tryLootDrop() {
        int chance = random.nextInt(100);

        if ( chance  < 10 ) { 
            return Ability.DOUBLE_MOVE;
        } 
        
        else if ( chance < 10 ) { 
            return Ability.BLOCK_LADDER;
        }

        return null; 
    }

    public void switchTurn() {
        if (!gameover) {
            currPlayerIndex = (currPlayerIndex + 1) % players.size();
        }
    }



    public void setGameOver(boolean status) {
         this.gameover = status ;
         }

    public boolean isGameOver() {
         return gameover;
         }

}