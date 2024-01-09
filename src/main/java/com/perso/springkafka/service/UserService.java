package com.perso.springkafka.service;


import com.perso.springkafka.dto.User;

import java.util.List;

public interface UserService {

    void save(User user);

    List<com.perso.springkafka.entity.User> getUsersByName(String firstName);

    List<User> getAllUsers();
}
