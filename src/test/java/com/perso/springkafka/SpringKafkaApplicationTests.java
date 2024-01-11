package com.perso.springkafka;

import com.perso.springkafka.dto.User;
import com.perso.springkafka.kafka.producer.UserKafkaProducer;
import com.perso.springkafka.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
class UserKafkaTestcontainersTest {

	@Autowired
	private UserKafkaProducer userKafkaProducer;

	@MockBean
	private UserService userService;

	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
		registry.add("spring.datasource.url", () -> "jdbc:h2:mem:test");
		registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
		registry.add("spring.datasource.username", () -> "root");
		registry.add("spring.datasource.password", () -> "secret");
		registry.add("spring.flyway.enabled", () -> "false");
	}

	@Test
	void testProduceAndConsumeKafkaMessage() {
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		User user = new User("11111", "John", "Wick");

		userKafkaProducer.writeToKafka(user);

		verify(userService, timeout(5000)).save(captor.capture());
		assertNotNull(captor.getValue());
		assertEquals("11111", captor.getValue().getUuid());
		assertEquals("John", captor.getValue().getFirstName());
		assertEquals("Wick", captor.getValue().getLastName());
	}
}
