package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Plant;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.PlantRepository;
import com.example.happyfarmer.Services.DepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class DepotController {

    DepotService depotService;

    public DepotController(DepotService depotService) {
        this.depotService = depotService;
    }

    @PostMapping("/depot")
    public Account harvestPlant(@RequestBody Plant plant, @RequestHeader("id") long id) {
        return depotService.harvest(plant, id);
    }

    @GetMapping("/depot")
    public Account getDepot(@RequestHeader("id") long id) {
        return depotService.getDepot(id);
    }
}
