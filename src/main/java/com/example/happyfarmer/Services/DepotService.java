package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Plant;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.PlantRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepotService {

    private final DepotRepository depotRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    @Autowired
    public DepotService(PlantRepository plantRepository, DepotRepository depotRepository, UserRepository userRepository) {
        this.depotRepository = depotRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }
    public Account harvest(Plant plant, long id){
        int type = plant.getPlantType();
        Account account = depotRepository.findDepotByUserId(id);
        if (account == null) {
            account = Account.builder().id(id).coins(0).carrotCount(0).pepperCount(0).cornCount(0).build();
        }
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

    public Account getDepot(long id){
        if (!depotRepository.existsByUserId(id)) {
            Users user = userRepository.findByTelegramId(id);
            depotRepository.save(Account.builder().userId(id).coins(user.getCoins()).build());
        }
        return depotRepository.findDepotByUserId(id);
    }
}
