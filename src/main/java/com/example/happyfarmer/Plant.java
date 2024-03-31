package com.example.happyfarmer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Plant {
    int plantType;
    String name;
    LocalDateTime dateTime;
    int positionCol;
    int positionRow;
    int timeToGrow;
    int stageOfGrowing;
    LocalDateTime actualTimeToGrow;
    Boolean isGrow;


}
