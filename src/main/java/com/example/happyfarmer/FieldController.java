package com.example.happyfarmer;

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

    List<Plant> plantList = new ArrayList<>();
    Account account = Account.builder().build();
    List<Users> usersList = new ArrayList<>();

    private final UserRepository userRepository;

    @Autowired
    public FieldController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/plant", method = RequestMethod.GET)
    public @ResponseBody List<Plant> getPlant(@RequestHeader("Time-Zone") String timezone) {
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
        return plantList;
    }


    @RequestMapping(value = "/plant", method = RequestMethod.POST)
    public @ResponseBody Plant createPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        plant.setActualTimeToGrow(plant.getDateTime().plusSeconds(plant.getTimeToGrow()));

        plantList.add(plant);
        return plant;
    }

    @RequestMapping(value = "/plant-stage", method = RequestMethod.POST)
    public @ResponseBody List<Plant> growPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        plantList.stream()
                .filter(e -> e.getPositionCol() == plant.getPositionCol() && e.getPositionRow() == plant.getPositionRow())
                .forEach(f -> {
                    f.setStageOfGrowing(plant.getStageOfGrowing() + 1);
                    f.setActualTimeToGrow(f.getDateTime());
                });
        return plantList;
    }

    @RequestMapping(value = "/deletePlant", method = RequestMethod.POST)
    public @ResponseBody List<Plant> deletePlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        plantList.remove(plant);
        return plantList;
    }

    @RequestMapping(value = "/depot", method = RequestMethod.POST)
    public @ResponseBody Account harvestPlant(@RequestBody Plant plant, @RequestHeader("Time-Zone") String timezone) {
        int type = plant.getPlantType();
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
        plantList.remove(plant);
        return account;
    }

    @RequestMapping(value = "/depot", method = RequestMethod.GET)
    public @ResponseBody Account getDepot(@RequestHeader("Time-Zone") String timezone) {
        return account;
    }

    @RequestMapping(value = "/fair", method = RequestMethod.POST)
    public @ResponseBody Account sellPlants(@RequestBody Fair fair, @RequestHeader("Time-Zone") String timezone) {
        int type = fair.getType();
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
        return account;
    }

    @RequestMapping(value = "/myLeague", method = RequestMethod.GET)
    public @ResponseBody int getLeague(@RequestHeader("Time-Zone") String timezone) {
        int coins = account.getCoins();

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
        return (List<Users>) userRepository.findAllByLeague(leagueId.getId());
    }


}
