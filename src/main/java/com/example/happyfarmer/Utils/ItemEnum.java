package com.example.happyfarmer.Utils;

import lombok.Getter;

@Getter
public enum ItemEnum {

    CORN(0),
    CARROT(1),
    PEPPER(2),
    NOTHING(3),
    SEED(4),
    COIN(5);

    ItemEnum(int type) {
        this.type = type;
    }

    public int type;
}
