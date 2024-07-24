package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long id;
    long coins;
    String name;
    int league;
    long telegramId;
    String referralCode;
    String referredBy;
    int availableSpins = 0;
    @OneToMany(mappedBy = "telegramId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpinAttempt> spinAttempts;
}
