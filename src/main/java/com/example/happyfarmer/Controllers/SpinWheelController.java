package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.WheelPrize;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.WheelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class SpinWheelController {

    private final WheelRepository wheelRepository;
    private final DepotRepository depotRepository;

    public SpinWheelController(WheelRepository wheelRepository, DepotRepository depotRepository) {
        this.wheelRepository = wheelRepository;
        this.depotRepository = depotRepository;
    }

    @RequestMapping(value = "/spin", method = RequestMethod.GET)
    public @ResponseBody WheelPrize spinWheel(@RequestHeader("id") long id) {
        List<WheelPrize> wheelPrizes = (List<WheelPrize>) wheelRepository.findAll();
        Account account = depotRepository.findDepotByUserId(id);
        Random random = new Random();
        int i = random.nextInt(wheelPrizes.size());
        WheelPrize wheelPrize = wheelPrizes.get(i);

        switch (wheelPrize.getPrizeType().type) {
            case 0:
                account.setCornCount(account.getCornCount() + wheelPrize.getPrizeCount());
                break;
            case 1:
                account.setCarrotCount(account.getCarrotCount() + wheelPrize.getPrizeCount());
                break;
            case 2:
                account.setPepperCount(account.getPepperCount() + wheelPrize.getPrizeCount());
                break;
            case 3:
                break;
            case 5:
                account.setCoins(account.getCoins() + wheelPrize.getPrizeCount());
                break;
        }
        depotRepository.save(account);
        return wheelPrize;
    }

    @RequestMapping(value = "/getSpinWheelRewards", method = RequestMethod.GET)
    public @ResponseBody List<WheelPrize> spinRewards() {
        return (List<WheelPrize>) wheelRepository.findAll();
    }

}
