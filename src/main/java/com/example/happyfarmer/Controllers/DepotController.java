package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Plant;
import com.example.happyfarmer.Repositories.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class DepotController {

    private final DepotRepository depotRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public DepotController(PlantRepository plantRepository, DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
        this.plantRepository = plantRepository;
    }
    @RequestMapping(value = "/depot", method = RequestMethod.POST)
    public @ResponseBody Account harvestPlant(@RequestBody Plant plant, @RequestHeader("id") long id) {
        int type = plant.getPlantType();
        Account account = depotRepository.findDepotByUserId(id);
        switch (type) {
            case 0:
                account.setCornCount(account.getCornCount() + 1);
                break;
            case 1:
                account.setCarrotCount(account.getCarrotCount() + 1);
                break;
            case 2:
                account.setPepperCount(account.getPepperCount() + 1);
                break;
        }
        depotRepository.save(account);
        plantRepository.delete(plant);
        return depotRepository.findDepotByUserId(id);
    }

    @RequestMapping(value = "/depot", method = RequestMethod.GET)
    public @ResponseBody Account getDepot(@RequestHeader("id") long id) {
        return depotRepository.findDepotByUserId(id);
    }
}
