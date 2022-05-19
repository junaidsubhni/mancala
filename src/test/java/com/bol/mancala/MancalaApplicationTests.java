package com.bol.mancala;

import com.bol.mancala.repository.MancalaGameRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@RunWith(SpringRunner.class)
class MancalaApplicationTests {

	@Autowired
	MancalaGameRepository gameRepository;

	@Test
	void contextLoads() {
		assertThat(gameRepository).isNotNull();
	}

}
