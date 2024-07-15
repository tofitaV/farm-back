package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Models.League;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Services.LeagueService;
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

    LeagueService leagueService;
    LeagueController(LeagueService leagueService){
        this.leagueService = leagueService;
    }

    @GetMapping("/myLeague")
    public int getLeague(@RequestHeader("id") long id) {
        return leagueService.getLeague(id);
    }

    @PostMapping("/leagueUsers")
    public List<Users> getLeagueUsers(@RequestBody League leagueId) {
        return leagueService.getLeagueUsers(leagueId);
    }
}
