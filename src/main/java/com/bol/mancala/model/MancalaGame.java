package com.bol.mancala.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Document
@AllArgsConstructor
@Data
public class MancalaGame {
    @Id
    private String id;

    private List<MancalaPit> pits;

    private PlayerTurns playerTurn;

    public MancalaGame(String id, Integer pitStones) {
        this.pits = Arrays.asList(
                new MancalaPit(1, pitStones),
                new MancalaPit(2, pitStones),
                new MancalaPit(3, pitStones),
                new MancalaPit(4, pitStones));
        this.id = id;
    }
}
