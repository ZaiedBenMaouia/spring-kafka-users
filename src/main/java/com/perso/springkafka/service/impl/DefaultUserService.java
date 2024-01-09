package com.perso.springkafka.service.impl;

import com.perso.springkafka.dto.User;
import com.perso.springkafka.repository.UserRepository;
import com.perso.springkafka.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserService.class);

    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        logger.info("Saving user with id = {}", user.getUuid());
        userRepository.save(new com.perso.springkafka.entity.User(user.getUuid(), user.getFirstName(), user.getLastName()));
    }

    @Override
    public List<com.perso.springkafka.entity.User> getUsersByName(String firstName) {
        return userRepository.getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc(firstName);
    }

    @Override
    public List<com.perso.springkafka.entity.User> getAllUsers() {
        return userRepository.findAll();
    }
}
