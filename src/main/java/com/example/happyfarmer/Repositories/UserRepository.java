package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users, Long> {
    List<Users> findAllByLeague(int league);
    Users findByTelegramId(long id);
    @Query("SELECT u.referralCode FROM Users u WHERE u.telegramId = :id")
    String getReferralCode(long id);
}