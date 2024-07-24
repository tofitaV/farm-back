package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.*;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.SpinAttemptRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Repositories.WheelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class SpinService {
    private final WheelRepository wheelRepository;
    private final DepotRepository depotRepository;
    private final UserRepository userRepository;
    private final SpinAttemptRepository spinAttemptRepository;

    public SpinService(WheelRepository wheelRepository, DepotRepository depotRepository, UserRepository userRepository, SpinAttemptRepository spinAttemptRepository) {
        this.wheelRepository = wheelRepository;
        this.depotRepository = depotRepository;
        this.userRepository = userRepository;
        this.spinAttemptRepository = spinAttemptRepository;
    }

    @Transactional
    public WheelPrize spinWheel(long id) {
        List<WheelPrize> wheelPrizes = (List<WheelPrize>) wheelRepository.findAll();
        if (wheelPrizes.isEmpty()) {
            throw new IllegalStateException("No prizes available");
        }
        Account account = depotRepository.findDepotByUserId(id);
        if (account == null) {
            throw new IllegalStateException("Account not found for user id: " + id);
        }
        Random random = new Random();
        int i = random.nextInt(wheelPrizes.size());
        WheelPrize wheelPrize = wheelPrizes.get(i);

        switch (wheelPrize.getPrizeType().getType()) {
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
            default:
                throw new IllegalStateException("Unknown prize type: " + wheelPrize.getPrizeType().getType());
        }
        depotRepository.save(account);
        return spin(id, wheelPrize);
    }

    public List<WheelPrize> spinRewards() {
        return (List<WheelPrize>) wheelRepository.findAll();
    }

    @Transactional
    public boolean canSpin(long id) {
        LocalDate today = LocalDate.now(); //TODO get by UTC
        List<SpinAttempt> attemptsToday = spinAttemptRepository.findByTelegramIdAndDate(id, today);
        return attemptsToday.stream().noneMatch(SpinAttempt::getIsFree);
    }

    @Transactional
    public WheelPrize spin(long telegramId, WheelPrize wheelPrize) {
        Users user = userRepository.findByTelegramId(telegramId);

        if (canSpin(telegramId)) {
            SpinAttempt spinAttempt = SpinAttempt.builder()
                    .telegramId(telegramId)
                    .isFree(true)
                    .wheelPrize(wheelPrize)
                    .build();
            spinAttemptRepository.save(spinAttempt);
            return wheelPrize;
        } else if (user.getAvailableSpins() > 0) {
            SpinAttempt spinAttempt = SpinAttempt.builder()
                    .telegramId(telegramId)
                    .isFree(false)
                    .wheelPrize(wheelPrize)
                    .build();
            spinAttemptRepository.save(spinAttempt);

            user.setAvailableSpins(user.getAvailableSpins() - 1);
            userRepository.save(user);
            return wheelPrize;
        } else {
            throw new RuntimeException("No available spins");
        }
    }

    @Transactional
    public boolean purchaseSpin(long id) {
        try {
            Users user = userRepository.findByTelegramId(id);
            user.setAvailableSpins(user.getAvailableSpins() + 1);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SpinStatus getSpinStatus(long telegramId) {
        return userRepository.findAvailableSpinsAndFreeSpinStatus(telegramId);
    }
}
