package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Fair;
import com.example.happyfarmer.Models.Plant;
import com.example.happyfarmer.Services.PlantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class FieldController {

    PlantService plantService;

    FieldController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/plant")
    public List<Plant> getPlant(@RequestHeader("id") long id, @RequestHeader("Time-Zone") String timezoneString) {
        return plantService.getPlants(id, timezoneString);
    }

    @PostMapping("/plant")
    public Plant createPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        return plantService.createPlant(plant, timezone);
    }

    @PostMapping("/plant-stage")
    public List<Plant> growPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone, @RequestHeader("id") long id) {
        return plantService.growPlant(plant, id, timezone);
    }

    @PostMapping("/deletePlant")
    public List<Plant> deletePlant(@RequestBody Plant plant, @RequestHeader("id") long id) {
        return plantService.deletePlant(plant, id);
    }

    @PostMapping("/fair")
    public Account sellPlants(@RequestBody Fair fair, @RequestHeader("id") long id) {
        return plantService.sellPlants(fair, id);
    }


}
