package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Fair;
import com.example.happyfarmer.Models.Plant;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.PlantRepository;
import com.example.happyfarmer.Utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PlantService {

    private final DepotRepository depotRepository;
    private final PlantRepository plantRepository;
    private final TransactionService transactionService;

    public PlantService(DepotRepository depotRepository, PlantRepository plantRepository, TransactionService transactionService) {
        this.depotRepository = depotRepository;
        this.plantRepository = plantRepository;
        this.transactionService = transactionService;
    }

    public List<Plant> getPlants(long id, String timezone) {
        List<Plant> plantList = plantRepository.findAllPlantByUserId(id);
        ZoneId userZoneId = ZoneId.of(timezone);
        ZonedDateTime currentTime = ZonedDateTime.now(userZoneId);

        for (Plant plant : plantList) {
            ZonedDateTime plantDateTimeInUserZone = plant.getDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(userZoneId);
            if (plant.getStageOfGrowing() == 0 && plantDateTimeInUserZone.plusMinutes(plant.getTimeToGrow()).isBefore(currentTime)) {
                plant.setStageOfGrowing(1);
            }
        }

        for (Plant plant : plantList) {
            ZonedDateTime plantZonedDateTime = plant.getActualTimeToGrow().atZone(userZoneId);
            long currentTimestamp = currentTime.toInstant().toEpochMilli();
            long plantCurrentTimestamp = plantZonedDateTime.toInstant().toEpochMilli();
            if (plantCurrentTimestamp - currentTimestamp <= 0) {
                plant.setIsGrow(true);
            }
        }
        return (List<Plant>) plantRepository.saveAll(plantList);
    }

    public List<Plant> growPlant(Plant plant, long id, String timezone) {
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

    public Plant createPlant(Plant plant, String timezone) {
        plant.setDateTime(DateTimeUtils.justifyDateForClient(plant.getDateTime(), timezone));
        plant.setActualTimeToGrow(plant.getDateTime().plusSeconds(plant.getTimeToGrow()));

        try {
            plantRepository.save(plant);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plant;
    }

    public List<Plant> deletePlant(Plant plant, long id) {
        plantRepository.delete(plant);
        return plantRepository.findAllPlantByUserId(id);
    }

    public Account sellPlants(Fair fair, long id) {
        int type = fair.getType();
        Account account = depotRepository.findDepotByUserId(id);
        if (account == null) {
            throw new IllegalStateException("Account not found for user id: " + id);
        }
        try {
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
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plant type: " + fair.getType());
        }

        return depotRepository.findDepotByUserId(id);
    }

    private void sellPlant(Account account, Fair fair, int plantCount, java.util.function.Consumer<Integer> setPlantCount) {
        try {
            if (plantCount >= fair.getPlantCount()) {
                setPlantCount.accept(plantCount - fair.getPlantCount());
                transactionService.updateCoins(account.getUserId(), account.getCoins() + fair.getCoin());
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Not enough plants to sell");
        }
    }
}
