package com.bol.mancala.service;

import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.*;
import com.bol.mancala.repository.MancalaGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bol.mancala.constants.MancalaConstants.NO_OF_STONES_PER_PLAYER;

@Service
public class MancalaGameServiceImpl implements MancalaGameService{

    private final MancalaGameRepository mancalaGameRepository;

    @Autowired
    public MancalaGameServiceImpl(MancalaGameRepository mancalaGameRepository){
        this.mancalaGameRepository = mancalaGameRepository;
    }
    @Override
    public MancalaGame createGame(String firstPlayerName, String secondPlayerName) {

        MancalaGame mancalaGame = createMancalaGame(firstPlayerName, secondPlayerName);
        mancalaGameRepository.save(mancalaGame);

        return mancalaGame;
    }

    @Override
    public MancalaGame loadGame (String id) throws ResourceNotFoundException {
        Optional<MancalaGame> gameOptional = mancalaGameRepository.findById(id);

        if (!gameOptional.isPresent())
            throw new ResourceNotFoundException("Game id " + id + " not found!");

        return gameOptional.get();
    }

    @Override
    public MancalaGame saveGame(MancalaGame mancalaGame) {
        mancalaGame = mancalaGameRepository.save(mancalaGame);
        return mancalaGame;
    }

    private MancalaGame createMancalaGame(String firstPlayerName, String secondPlayerName){
        List<MancalaPit> pits = Arrays.asList(
                new MancalaPit(1,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(2,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(3,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(4,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(5,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(6,NO_OF_STONES_PER_PLAYER),
                new MancalaBigPit(7),
                new MancalaPit(8,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(9,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(10,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(11,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(12,NO_OF_STONES_PER_PLAYER),
                new MancalaPit(13,NO_OF_STONES_PER_PLAYER),
                new MancalaBigPit(14));
        List<Player> players = Arrays.asList(
                new Player(firstPlayerName),
                new Player(secondPlayerName));
        MancalaGame game = MancalaGame.builder()
                .pits(pits)
                .players(players)
                .gameStatus(GameStatus.IN_PROGRESS)
                .build();
        return game;
    }
}
