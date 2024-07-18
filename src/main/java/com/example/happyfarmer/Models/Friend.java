package com.example.happyfarmer.Models;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    String name;
    long coins;
}
