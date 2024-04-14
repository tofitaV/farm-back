package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "depot")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Builder.Default
    int carrotCount = 0;
    @Builder.Default
    int pepperCount = 0;
    @Builder.Default
    int cornCount = 0;
    @Builder.Default
    long coins = 0;
    long userId;
}
