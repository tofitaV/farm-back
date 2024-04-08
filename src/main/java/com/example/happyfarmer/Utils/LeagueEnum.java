package com.example.happyfarmer.Utils;

import lombok.Getter;

@Getter
public enum LeagueEnum {
    BRONZE(0),
    SILVER(1),
    GOLD(2),
    PLATINUM(4);

    LeagueEnum(int league) {
        this.league = league;
    }
    private int league;

}
