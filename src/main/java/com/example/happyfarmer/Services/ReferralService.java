package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.ReferralLink;
import com.example.happyfarmer.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferralService {

    private UserRepository userRepository;

    public ReferralService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ReferralLink getMyReferralLink(long id) {
        String code = userRepository.getReferralCode(id);
        String link = "https://t.me/crypto_plants_bot/plants?start=".concat(code);
        return ReferralLink.builder().link(link).build();
    }
}
