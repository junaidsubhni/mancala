package com.bol.mancala.service;

import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.repository.MancalaGameRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface MancalaGameService {

    MancalaGame createGame(String firstPlayerName, String secondPlayerName);

    MancalaGame loadGame(String gameId);

    MancalaGame saveGame(MancalaGame mancalaGame);
}
