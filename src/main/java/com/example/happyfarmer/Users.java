package com.example.happyfarmer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Users {
    int coins;
    String name;
    int league;
}
