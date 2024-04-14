package com.example.happyfarmer;

import com.example.happyfarmer.Models.Plant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantRepository extends CrudRepository<Plant, Integer> {
    List<Plant> findAllPlantByUserId(long id);
}
