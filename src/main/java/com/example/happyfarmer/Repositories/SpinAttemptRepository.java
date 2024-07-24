package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.SpinAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpinAttemptRepository extends JpaRepository<SpinAttempt, Long> {
    List<SpinAttempt> findByTelegramIdAndDate(long telegramId, LocalDate date);
}