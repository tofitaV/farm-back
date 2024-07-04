package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Models.League;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Utils.LeagueEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class LeagueController {

    private final DepotRepository depotRepository;
    private final UserRepository userRepository;

    public LeagueController(DepotRepository depotRepository, UserRepository userRepository) {
        this.depotRepository = depotRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/myLeague")
    public int getLeague(@RequestHeader("id") long id) {
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

    @PostMapping("/leagueUsers")
    public List<Users> getLeagueUsers(@RequestBody League leagueId) {
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
