package com.bol.mancala.service;

import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bol.mancala.constants.MancalaConstants.*;

@Service
public class MancalaSowServiceImpl implements MancalaSowService{

    private final  MancalaGameService mancalaGameService;

    @Autowired
    public MancalaSowServiceImpl(MancalaGameService mancalaGameService){
        this.mancalaGameService = mancalaGameService;
    }

    @Override
    public MancalaGame sow(String gameId, int requestedPitId) {

        MancalaGame game = mancalaGameService.loadGame(gameId);
        if(game.getGameStatus() == GameStatus.GAME_OVER){
            game.setMessage("Game is Over!");
            return game;
        }
        if (isPlayerBigPit(requestedPitId)){
            game.setMessage("Illegal Move");
            return game;
        }

        setPlayerTurn(requestedPitId, game);

        if (isNotLegalMove(requestedPitId, game)) {
            game.setMessage(game.getPlayerTurn().name() +"'s Turn");
            return game;
        }

        MancalaPit selectedPit = getPit(requestedPitId, game);

        int stones = selectedPit.getStones();

        if (stones == EMPTY_STONE) {
            game.setMessage("Illegal Move");
            return game;
        }

        selectedPit.setStones(EMPTY_STONE);

        game.setCurrentPitIndex(requestedPitId);

        // simply sow all stones except the last one
        IntStream.range(0, stones -1)
                .forEach(index-> sowRight(game, false));

        // simply the last stone
        sowRight(game,true);

        int currentPitIndex = game.getCurrentPitIndex();
        gameOverRule(game);
        switchPlayersTurn(game, currentPitIndex);
        mancalaGameService.saveGame(game);
        return game;
    }

    private void switchPlayersTurn(MancalaGame game, int currentPitIndex) {
        if (currentPitIndex != PLAYER_A_BIG_PIT_ID && currentPitIndex != PLAYER_B_BIG_PIT_ID)
            game.setPlayerTurn(nextTurn(game.getPlayerTurn()));
    }

    private boolean isNotLegalMove(int requestedPitId, MancalaGame game) {
        return game.getPlayerTurn() == PlayerTurns.PLAYER_A && requestedPitId > PLAYER_A_BIG_PIT_ID ||
                game.getPlayerTurn() == PlayerTurns.PLAYER_B && requestedPitId < PLAYER_A_BIG_PIT_ID;
    }

    private boolean isPlayerBigPit(int requestedPitId) {
        return requestedPitId == PLAYER_A_BIG_PIT_ID || requestedPitId == PLAYER_B_BIG_PIT_ID;
    }

    private void setPlayerTurn(int requestedPitId, MancalaGame game) {
        if (game.getPlayerTurn() == null) {
            if (requestedPitId < PLAYER_A_BIG_PIT_ID)
                game.setPlayerTurn(PlayerTurns.PLAYER_A);
            else
                game.setPlayerTurn(PlayerTurns.PLAYER_B);
        }
    }

    private void sowRight(MancalaGame game, Boolean lastStone) {
        int currentPitIndex = game.getCurrentPitIndex() % TOTAL_PITS + 1;

        PlayerTurns playerTurn = game.getPlayerTurn();

        currentPitIndex = skipOpponentBigPit(currentPitIndex, playerTurn);

        game.setCurrentPitIndex(currentPitIndex);

        game.setTargetPit(getPit(currentPitIndex, game));
        if (checkIfLastStone(lastStone, currentPitIndex, game.getTargetPit())) return;

        if (stealOpponentStones(game)) return;

        game.getTargetPit().sow();
    }

    private boolean stealOpponentStones(MancalaGame game) {
        MancalaPit oppositePit = getPit(TOTAL_PITS - game.getCurrentPitIndex(), game);
        if (game.getTargetPit().isEmpty() && !oppositePit.isEmpty() && isOwnTargetPit(game)) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer pitHouseIndex = game.getCurrentPitIndex() < PLAYER_A_BIG_PIT_ID ? PLAYER_A_BIG_PIT_ID : PLAYER_B_BIG_PIT_ID;
            MancalaPit pitHouse = getPit(pitHouseIndex, game);
            pitHouse.addStones(oppositeStones + 1);
            return true;
        }
        return false;
    }

    private boolean isOwnTargetPit(MancalaGame game) {
        return game.getPlayerTurn() == PlayerTurns.PLAYER_A && game.getCurrentPitIndex() < PLAYER_A_BIG_PIT_ID ||
                game.getPlayerTurn() == PlayerTurns.PLAYER_B && game.getCurrentPitIndex() > PLAYER_A_BIG_PIT_ID;
    }

    private boolean checkIfLastStone(Boolean lastStone, int currentPitIndex, MancalaPit targetPit) {
        if (!lastStone || currentPitIndex == PLAYER_A_BIG_PIT_ID || currentPitIndex == PLAYER_B_BIG_PIT_ID) {
            targetPit.sow();
            return true;
        }
        return false;
    }

    private int skipOpponentBigPit(int currentPitIndex, PlayerTurns playerTurn) {
        if ((currentPitIndex == PLAYER_A_BIG_PIT_ID && playerTurn == PlayerTurns.PLAYER_B) ||
                (currentPitIndex == PLAYER_B_BIG_PIT_ID && playerTurn == PlayerTurns.PLAYER_A))
            currentPitIndex = currentPitIndex % TOTAL_PITS + 1;
        return currentPitIndex;
    }

    public PlayerTurns nextTurn(PlayerTurns currentTurn) {
        if (currentTurn == PlayerTurns.PLAYER_A)
            return PlayerTurns.PLAYER_B;
        return PlayerTurns.PLAYER_A;
    }

    public MancalaPit getPit(Integer pitIndex, MancalaGame game) throws MancalaException {
        try {
            return game.getPits().get(pitIndex-1);
        }catch (Exception e){
            throw  new MancalaException("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

    private void gameOverRule(MancalaGame game) {
        int currentPlayerStones = game.getCurrentPlayerTotalStonesInPits();

        if (currentPlayerStones != 0)
            return;
        game.setPlayerTurn(nextTurn(game.getPlayerTurn()));
        int otherPlayerStones = game.getCurrentPlayerTotalStonesInPits();
        if(game.getPlayerTurn() == PlayerTurns.PLAYER_A){
            game.getPits().get(PLAYER_A_BIG_PIT_LIST_ID).addStones(otherPlayerStones);
            resetPitsToZero(game,0,5);
        }else{
            game.getPits().get(PLAYER_B_BIG_PIT_LIST_ID).addStones(otherPlayerStones);
            resetPitsToZero(game,7,12);
        }
        if(game.getPits().get(PLAYER_A_BIG_PIT_LIST_ID).getStones() < game.getPits().get(PLAYER_B_BIG_PIT_LIST_ID).getStones()){
            game.setMessage(PlayerTurns.PLAYER_B.name()+" is Winner!");
        } else if(game.getPits().get(PLAYER_A_BIG_PIT_LIST_ID).getStones() > game.getPits().get(PLAYER_B_BIG_PIT_LIST_ID).getStones()){
            game.setMessage(PlayerTurns.PLAYER_A.name()+" is Winner!");
        }else{
            game.setMessage("Game Draw!");
        }

        game.setGameStatus(GameStatus.GAME_OVER);

    }
    public static void resetPitsToZero(MancalaGame game, int startPit, int endPit) {
        IntStream.rangeClosed(startPit, endPit).forEach(p -> game.getPits().get(p).clear());
    }

}
