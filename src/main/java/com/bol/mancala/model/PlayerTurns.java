package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum PlayerTurns {
    PLAYER_A (0),
    PLAYER_B (1);

    private int turn;

}
