package com.bol.mancala.controller;

import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.model.MancalaPit;
import com.bol.mancala.repository.MancalaGameRepository;
import com.bol.mancala.service.MancalaGameService;
import com.bol.mancala.service.MancalaSowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Iterables;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/mancala")
public class MancalaGameController {

    private static final Logger log = LoggerFactory.getLogger(MancalaGameController.class);

    private final MancalaGameService mancalaGameService;

    private final MancalaSowService mancalaSowService;

    @Autowired
    public MancalaGameController (MancalaGameService mancalaGameService, MancalaSowService mancalaSowService){
        this.mancalaGameService = mancalaGameService;
        this.mancalaSowService = mancalaSowService;
    }

    @PostMapping("/create")
    public ResponseEntity<MancalaGame> createGame(@RequestParam @NotNull String firstPlayerName,
                                                  @RequestParam @NotNull String secondPlayerName) {
        log.info("createGame called");
        MancalaGame game = mancalaGameService.createGame(firstPlayerName, secondPlayerName);
        Iterator<MancalaPit> pi = Iterables.cycle(game.getPits()).iterator();
        for (int i = 0; i < 100; i++) {
            MancalaPit pit = pi.next();
            System.out.println(pit.getId()+" : "+pit.getStones());
        }
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<MancalaGame> loadGame(@PathVariable String gameId) {
        log.info("loadGame called");
        MancalaGame game = mancalaGameService.loadGame(gameId);
        if(game == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/sow")
    public ResponseEntity<MancalaGame> playGame(@PathVariable String gameId,
                                           @RequestParam @NotNull int pitId) {
        log.info("playGame called. PitId : "+ pitId);
        MancalaGame game = mancalaSowService.sow(gameId, pitId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
