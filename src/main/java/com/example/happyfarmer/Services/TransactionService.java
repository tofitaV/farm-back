package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final DepotRepository depotRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(DepotRepository depotRepository, UserRepository userRepository) {
        this.depotRepository = depotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateCoins(long userId, long coinsToAdd) {
        Account account = depotRepository.findDepotByUserId(userId);
        Users user = userRepository.findByTelegramId(userId);

        account.setCoins(coinsToAdd);
        user.setCoins(coinsToAdd);

        depotRepository.save(account);
        userRepository.save(user);
    }
}