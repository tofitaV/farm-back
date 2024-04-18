package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.SpinWheelItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class SpinWheelController {
    private static final List<String> items = Arrays.asList(
            "Corn 1", "Pepper 2", "Nothing 3", "Coins 200 4", "Nothing 5",
            "Corn 6", "Carrot 7", "Nothing 8", "Pepper 9", "Corn 10"
    );

    @RequestMapping(value = "/spin", method = RequestMethod.GET)
    public @ResponseBody int spinWheel() {
        Random random = new Random();
        int index = random.nextInt(items.size());
        return index;
    }

    @RequestMapping(value = "/getSpinWheelRewards", method = RequestMethod.GET)
    public @ResponseBody List<String> spinRewards() {
        return items;
    }

}
