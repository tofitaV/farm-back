package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.Friend;
import com.example.happyfarmer.Models.SpinStatus;
import com.example.happyfarmer.Models.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    List<Users> findAllByLeague(int league);
    Users findByTelegramId(long id);
    boolean existsByTelegramId(long id);
    @Query("SELECT u.referralCode FROM Users u WHERE u.telegramId = :id")
    String getReferralCode(long id);
    @Query("SELECT new com.example.happyfarmer.Models.Friend(u.name, u.coins) FROM Users u WHERE u.referredBy = :id")
    List<Friend> findAllFriendsByReferredBy(long id);

    @Query("SELECT new com.example.happyfarmer.Models.SpinStatus(" +
            "u.availableSpins, " +
            "CASE WHEN EXISTS (SELECT 1 FROM SpinAttempt sa WHERE sa.telegramId = :telegramId AND sa.date = CURRENT_DATE AND sa.isFree = true) THEN true ELSE false END) " +
            "FROM Users u WHERE u.telegramId = :telegramId")
    SpinStatus findAvailableSpinsAndFreeSpinStatus(@Param("telegramId") long telegramId);

}