package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Models.*;
import com.example.happyfarmer.Repositories.PlantRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class FieldController {
    private final UserRepository userRepository;
    private final DepotRepository depotRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public FieldController(UserRepository userRepository, PlantRepository plantRepository, DepotRepository depotRepository) {
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
        this.depotRepository = depotRepository;
    }

    @GetMapping("/plant")
    public List<Plant> getPlant(@RequestHeader("id") long id) {
        List<Plant> plantList = plantRepository.findAllPlantByUserId(id);
        LocalDateTime currentTime = LocalDateTime.now();
        for (Plant plant : plantList) {
            if (plant.getStageOfGrowing() == 0 && plant.getDateTime().plusMinutes(plant.getTimeToGrow()).isBefore(currentTime)) {
                plant.setStageOfGrowing(1);
            }
        }

        for (Plant plant : plantList) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = ZonedDateTime.of(currentTime, zoneId);
            ZonedDateTime plantZonedDateTime = ZonedDateTime.of(plant.getActualTimeToGrow(), zoneId);
            long currentTimestamp = zonedDateTime.toInstant().toEpochMilli();
            long plantCurrentTimestamp = plantZonedDateTime.toInstant().toEpochMilli();
            if (plantCurrentTimestamp - currentTimestamp <= 0) {
                plant.setIsGrow(true);
            }
        }
        return (List<Plant>) plantRepository.saveAll(plantList);
    }

    @PostMapping("/plant")
    public Plant createPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        plant.setActualTimeToGrow(plant.getDateTime().plusSeconds(plant.getTimeToGrow()));

        try {
            plantRepository.save(plant);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plant;
    }

    @PostMapping("/plant-stage")
    public List<Plant> growPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone, @RequestHeader("id") long id) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        List<Plant> plantList = plantRepository.findAllPlantByUserId(id);
        plantList.stream()
                .filter(e -> e.getPositionCol() == plant.getPositionCol() && e.getPositionRow() == plant.getPositionRow())
                .forEach(f -> {
                    f.setStageOfGrowing(plant.getStageOfGrowing() + 1);
                    f.setActualTimeToGrow(f.getDateTime());
                });
        return (List<Plant>) plantRepository.saveAll(plantList);
    }

    @PostMapping("/deletePlant")
    public List<Plant> deletePlant(@RequestBody Plant plant, @RequestHeader("id") long id) {
        plantRepository.delete(plant);
        return plantRepository.findAllPlantByUserId(id);
    }

    @PostMapping("/fair")
    public Account sellPlants(@RequestBody Fair fair, @RequestHeader("id") long id) {
        int type = fair.getType();
        Account account = depotRepository.findDepotByUserId(id);
        if (account == null) {
            throw new IllegalStateException("Account not found for user id: " + id);
        }
        switch (fair.getType()) {
            case 0:
                sellPlant(account, fair, account.getCornCount(), account::setCornCount);
                break;
            case 1:
                sellPlant(account, fair, account.getCarrotCount(), account::setCarrotCount);
                break;
            case 2:
                sellPlant(account, fair, account.getPepperCount(), account::setPepperCount);
                break;
            default:
                throw new IllegalArgumentException("Invalid plant type: " + fair.getType());
        }

        depotRepository.save(account);
        Users user = userRepository.findByTelegramId(id);
        if (user != null) {
            user.setCoins(account.getCoins());
            userRepository.save(user);
        }
        return depotRepository.findDepotByUserId(id);
    }

    private void sellPlant(Account account, Fair fair, int plantCount, java.util.function.Consumer<Integer> setPlantCount) {
        if (plantCount >= fair.getPlantCount()) {
            setPlantCount.accept(plantCount - fair.getPlantCount());
            account.setCoins(account.getCoins() + fair.getCoin());
            depotRepository.save(account);
        } else {
            throw new IllegalStateException("Not enough plants to sell");
        }
    }

}
