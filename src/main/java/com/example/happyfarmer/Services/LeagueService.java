package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.League;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Utils.LeagueEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    private final DepotRepository depotRepository;
    private final UserRepository userRepository;

    public LeagueService(DepotRepository depotRepository, UserRepository userRepository) {
        this.depotRepository = depotRepository;

        this.userRepository = userRepository;
    }

    public int getLeague(long id){
        Account account = depotRepository.findDepotByUserId(id);
        long coins = account.getCoins();
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

    public List<Users> getLeagueUsers(League leagueId){
        List<Users> usersList = new ArrayList<>();
        try {
            usersList = userRepository.findAllByLeague(leagueId.getId())
                    .stream()
                    .sorted(Comparator.comparingLong(Users::getCoins).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        return usersList;
    }

}
