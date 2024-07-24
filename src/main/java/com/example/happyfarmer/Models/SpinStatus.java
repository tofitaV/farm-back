package com.example.happyfarmer.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpinStatus {
    int availableSpins;
    boolean hasFreeSpin;
}
