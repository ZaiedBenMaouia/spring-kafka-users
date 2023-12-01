package com.perso.springkafka.repository;

import com.perso.springkafka.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc(String firstName);

}