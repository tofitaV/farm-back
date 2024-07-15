package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.WheelPrize;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.WheelRepository;
import com.example.happyfarmer.Services.SpinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class SpinWheelController {

    SpinService spinService;
    SpinWheelController(SpinService spinService){
        this.spinService = spinService;
    }

    @GetMapping("/spin")
    public WheelPrize spinWheel(@RequestHeader("id") long id) {
        return spinService.spinWheel(id);
    }

    @GetMapping("/rewards")
    public List<WheelPrize> spinRewards() {
        return spinService.spinRewards();
    }

}
