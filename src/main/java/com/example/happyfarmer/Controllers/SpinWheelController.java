package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.SpinStatus;
import com.example.happyfarmer.Models.WheelPrize;
import com.example.happyfarmer.Services.SpinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/purchase")
    public boolean purchaseSpin(@RequestHeader("id") long id) {
        return spinService.purchaseSpin(id);
    }
    @GetMapping("/status")
    public SpinStatus getSpinStatus(@RequestHeader("id") long id) {
        return spinService.getSpinStatus(id);
    }
}
