package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.Users;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users, Long> {
    List<Users> findAllByLeague(int league);
    Users findByTelegramId(long id);
}