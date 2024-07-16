package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.*;

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
}
