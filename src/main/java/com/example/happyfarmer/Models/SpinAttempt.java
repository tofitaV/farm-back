package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "spin_attempt")
public class SpinAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "telegram_id", nullable = false)
    long telegramId;
    @Builder.Default
    LocalDate date = LocalDate.now(ZoneOffset.UTC);
    @Builder.Default
    LocalDateTime timeOfAttempt = LocalDateTime.now(ZoneOffset.UTC);
    Boolean isFree;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wheel_prize_id")
    WheelPrize wheelPrize;
}
