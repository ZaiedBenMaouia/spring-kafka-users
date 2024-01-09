package com.perso.springkafka.controller;

import com.github.javafaker.Faker;
import com.perso.springkafka.dto.User;
import com.perso.springkafka.kafka.producer.UserKafkaProducer;
import com.perso.springkafka.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User", description = "User APIs")
public class UserController {

    private final UserKafkaProducer kafkaProducer;

    private final UserService userService;

    private final Faker faker;

    public UserController(UserKafkaProducer kafkaProducer, UserService userService) {
        this.kafkaProducer = kafkaProducer;
        this.userService = userService;
        faker = new Faker();
    }

    @GetMapping("/random")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create a user", description = "Creates a random user and write it to Kafka which is consumed by the listener")
    public void generateRandomUser() {
        kafkaProducer.writeToKafka(new User(UUID.randomUUID().toString(), faker.name().firstName(), faker.name().lastName()));
    }

    @GetMapping("/{firstName}")
    @Operation(summary = "Create a user", description = "Returns a list of users that matchers the given name")
    public ResponseEntity<List<User>> getUsersByName(@PathVariable(name = "firstName") String name) {
        List<com.perso.springkafka.entity.User> users = userService.getUsersByName(name);
        List<User> response = users.stream().map(user -> new User(user.getId(), user.getFirstName(), user.getLastName())).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @Operation(summary = "Create a user", description = "Returns a list of users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
