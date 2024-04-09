package com.example.happyfarmer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users, Integer> {
    List<Users> findAllByLeague(int league);

}