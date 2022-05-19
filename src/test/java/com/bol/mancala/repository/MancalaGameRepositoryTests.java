package com.bol.mancala.repository;

import com.bol.mancala.model.MancalaGame;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class MancalaGameRepositoryTests {

    @Autowired
    MancalaGameRepository gameRepository;

    @Test
    public void test_game_saved() {
        MancalaGame saved= this.gameRepository.save(new MancalaGame("GameId",6));

        Assert.assertNotNull(saved.getId());

    }

    @Test
    public void test_get_game() {
        this.gameRepository.deleteAll();

        MancalaGame saved= this.gameRepository.save(new MancalaGame("GameId",6));

        List<MancalaGame> list  = this.gameRepository.findAll();

        Assert.assertEquals(1, list.size());
    }
}