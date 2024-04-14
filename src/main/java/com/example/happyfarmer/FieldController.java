package com.example.happyfarmer;

import com.example.happyfarmer.Models.*;
import com.example.happyfarmer.Utils.DateTimeUtils;
import com.example.happyfarmer.Utils.LeagueEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @RequestMapping(value = "/plant", method = RequestMethod.GET)
    public @ResponseBody List<Plant> getPlant(@RequestHeader("id") long id) {
        List<Plant> plantList = plantRepository.findAllPlantByUserId(id);
        LocalDateTime currentTime = LocalDateTime.now();
        for (Plant plant : plantList) {
            if (plant.getStageOfGrowing() == 0) {
                if (plant.getDateTime().plusMinutes(plant.getTimeToGrow()).isBefore(currentTime)) {
                    plant.setStageOfGrowing(1);
                }
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


    @RequestMapping(value = "/plant", method = RequestMethod.POST)
    public @ResponseBody Plant createPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        plant.setActualTimeToGrow(plant.getDateTime().plusSeconds(plant.getTimeToGrow()));

        try {
            plantRepository.save(plant);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plant;
    }

    @RequestMapping(value = "/plant-stage", method = RequestMethod.POST)
    public @ResponseBody List<Plant> growPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone, @RequestHeader("id") long id) {
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

    @RequestMapping(value = "/deletePlant", method = RequestMethod.POST)
    public @ResponseBody List<Plant> deletePlant(@RequestBody Plant plant, @RequestHeader("id") long id) {
        plantRepository.delete(plant);
        return plantRepository.findAllPlantByUserId(id);
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

    @RequestMapping(value = "/fair", method = RequestMethod.POST)
    public @ResponseBody Account sellPlants(@RequestBody Fair fair, @RequestHeader("id") long id) {
        int type = fair.getType();
        Account account = depotRepository.findDepotByUserId(id);
        switch (type) {
            case 0:
                if (account.getCornCount() >= fair.getPlantCount()) {
                    account.setCornCount(account.getCornCount() - fair.getPlantCount());
                    account.setCoins(account.getCoins() + fair.getCoin());
                }
                break;
            case 1:
                if (account.getCarrotCount() >= fair.getPlantCount()) {
                    account.setCarrotCount(account.getCarrotCount() - fair.getPlantCount());
                    account.setCoins(account.getCoins() + fair.getCoin());
                }
                break;
            case 2:
                if (account.getPepperCount() >= fair.getPlantCount()) {
                    account.setPepperCount(account.getPepperCount() - fair.getPlantCount());
                    account.setCoins(account.getCoins() + fair.getCoin());
                }
                break;
        }
        depotRepository.save(account);
        return depotRepository.findDepotByUserId(id);
    }

    @RequestMapping(value = "/myLeague", method = RequestMethod.GET)
    public @ResponseBody int getLeague(@RequestHeader("id") long id) {
        long coins = depotRepository.findDepotByUserId(id).getCoins();

        if (coins >= 1000000) {
            return LeagueEnum.PLATINUM.getLeague();
        } else if (coins >= 10000) {
            return LeagueEnum.GOLD.getLeague();
        } else if (coins >= 5000) {
            return LeagueEnum.SILVER.getLeague();
        } else {
            return LeagueEnum.BRONZE.getLeague();
        }
    }

    @RequestMapping(value = "/leagueUsers", method = RequestMethod.POST)
    public @ResponseBody List<Users> getLeagueUsers(@RequestBody League leagueId) {
        List<Users> usersList = new ArrayList<>();
        try {
            usersList = userRepository.findAllByLeague(leagueId.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }


}
