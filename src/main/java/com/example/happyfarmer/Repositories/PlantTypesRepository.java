package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.PlantType;
import org.springframework.data.repository.CrudRepository;


public interface PlantTypesRepository extends CrudRepository<PlantType, Integer> {
    PlantType findByPlantType(int plantType);
}
