package com.example.happyfarmer.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "plant_types")
@AllArgsConstructor
@NoArgsConstructor
public class PlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "plant_type")
    int plantType;
    @Column(name = "plant_name")
    String plantName;
    @Column(name = "time_to_grow")
    int timeToGrow;

}
