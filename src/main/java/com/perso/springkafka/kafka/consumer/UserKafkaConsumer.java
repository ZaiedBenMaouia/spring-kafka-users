package com.perso.springkafka.kafka.consumer;

import com.perso.springkafka.dto.User;
import com.perso.springkafka.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserKafkaConsumer.class);

    private final UserService userService;

    public UserKafkaConsumer(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    public void logKafkaMessages(@Payload User user,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                 @Header(KafkaHeaders.OFFSET) Long offset,
                                 Acknowledgment acknowledgment) {
        logger.info("Received a message contains a user information with firstName {}, from {} topic, " +
                "{} partition, and {} offset", user.getFirstName(), topic, partition, offset);
        userService.save(user);
        acknowledgment.acknowledge();
    }
}
