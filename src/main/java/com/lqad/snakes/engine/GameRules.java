package com.lqad.snakes.engine;

import java.util.ArrayList;
import java.util.List;

import com.lqad.snakes.model.Ability;
import com.lqad.snakes.model.Board;
import com.lqad.snakes.model.Player;

public class GameRules {

    private static final int WIN_POINT = 100;

   
    public static MoveResult analyzeMove(Player player, int diceRoll, Board board, List<Player> allPlayers)  {

        int currentPos = Math.max(1, player.getPosition());
        
        
        List<Integer> path = calculateBouncePath(currentPos, diceRoll);
      
       int landSpot;

            if ( path.isEmpty())  {
                landSpot = currentPos;

            } 
            
            else {
                
                landSpot = path.get(path.size() - 1);
                
            }

        
        
        int destSpot = board.checkJump(landSpot);
        
        boolean isLadder = destSpot > landSpot;
        boolean isSnake = destSpot < landSpot;
        
       
        Player blocker = null;
        if (isLadder) {
            blocker = checkLadderBlocker(allPlayers, player);
        }

        return new MoveResult(path, landSpot, destSpot, isLadder, isSnake, blocker);
    }

    
    public static List<Integer> calculateBouncePath(int currentPos, int cardValue) {
        List<Integer> path = new ArrayList<>();

        if (currentPos + cardValue <= WIN_POINT) {
            // Normal move
            for (int i = 1; i <= cardValue; i++) {
                path.add(currentPos + i);
            }
        } else {
            // Bounce logic
            int stepsForward = WIN_POINT - currentPos;
            int stepsBackward = cardValue - stepsForward;

           
            for (int i = 1; i <= stepsForward; i++) {
                path.add(currentPos + i);
            }
            // Bounce back
            for (int i = 1; i <= stepsBackward; i++) {
                path.add(WIN_POINT - i);
            }
        }
        return path;
    }

    
    public static Player checkLadderBlocker(List<Player> players, Player currentPlayer) {

    if (players == null || players.isEmpty()) {
        return null;
    }

    int currentIndex = players.indexOf(currentPlayer);

    if (currentIndex == -1) {
        return null; 
    }

    int opponent = (currentIndex + 1) % players.size();
    Player oppo = players.get(opponent);

    if (oppo.hasAbility(Ability.BLOCK_LADDER)) {
        return oppo;
    }

    return null;
}

}