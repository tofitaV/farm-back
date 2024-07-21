package com.example.happyfarmer.Utils;

import lombok.Getter;

@Getter
public enum ItemEnum {

    CORN(0, "Corn"),
    CARROT(1, "Carrot"),
    PEPPER(2, "Pepper"),
    NOTHING(3, "Nothing"),
    SEED(4, "Seed"),
    COIN(5, "Coin");

    private final int type;
    private final String name;

    ItemEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getNameByType(int type) {
        for (ItemEnum item : values()) {
            if (item.type == type) {
                return item.getName();
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
