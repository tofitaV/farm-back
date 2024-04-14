package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "plants")
@AllArgsConstructor
@NoArgsConstructor
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int plantType;
    String name;
    LocalDateTime dateTime;
    int positionCol;
    int positionRow;
    int timeToGrow;
    int stageOfGrowing;
    LocalDateTime actualTimeToGrow;
    Boolean isGrow;
    long userId;

}
