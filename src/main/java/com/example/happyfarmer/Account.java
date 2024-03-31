package com.example.happyfarmer;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Account {

    @Builder.Default
    int carrotCount = 0;
    @Builder.Default
    int pepperCount = 0;
    @Builder.Default
    int cornCount = 0;
    @Builder.Default
    int coins = 0;
}
