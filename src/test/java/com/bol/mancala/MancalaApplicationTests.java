package com.bol.mancala;

import com.bol.mancala.controller.MancalaGameController;
import com.bol.mancala.repository.MancalaGameRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MancalaApplicationTests {

	@Autowired
	MancalaGameController gameRepository;

	@Test
	void contextLoads() {
		assertThat(gameRepository).isNotNull();
	}

}
