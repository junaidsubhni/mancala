package com.bol.mancala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Document(collection = "games")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MancalaGame {
    @Id
    private String id;

    private List<MancalaPit> pits;

    private PlayerTurns playerTurn;

    private List<Player> players;

    private String message;

    private GameStatus gameStatus;

    private MancalaPit targetPit;

    @JsonIgnore
    private int currentPitIndex;

    public int getCurrentPlayerTotalStonesInPits(){
        if(this.playerTurn == PlayerTurns.PLAYER_A){
            return this.pits.stream()
                    .filter(p -> p.getId() < 7)
                    .mapToInt(MancalaPit::getStones)
                    .sum();
        }else{
            return this.pits.stream()
                    .filter(p -> p.getId() > 7 && p.getId() < 14)
                    .mapToInt(MancalaPit::getStones)
                    .sum();
        }
    }
}
