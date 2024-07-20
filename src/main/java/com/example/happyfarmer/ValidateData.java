package com.example.happyfarmer;

import com.example.happyfarmer.Models.*;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@CrossOrigin(maxAge = 3600)
@RestController
public class ValidateData {

    private static final String BOT_TOKEN = System.getProperty("botToken");
    private final UserRepository userRepository;
    private final DepotRepository depotRepository;

    @Autowired
    public ValidateData(UserRepository userRepository, DepotRepository depotRepository) {
        this.userRepository = userRepository;
        this.depotRepository = depotRepository;
    }

    @PostMapping("/authorize")
    public ResponseEntity<?> validateData(@RequestBody TelegramUserInfo telegramUserInfo) {
        InitDataUnsafe initDataUnsafe = telegramUserInfo.getInitDataUnsafe();
        String refCode = initDataUnsafe.getStart_param();

        TelegramAuth telegramAuth = new TelegramAuth();
        try {
            if (telegramAuth.isValid(telegramUserInfo.getInitData(), BOT_TOKEN)) {
                TelegramUser telegramUser = initDataUnsafe.getUser();
                Users user = userRepository.findByTelegramId(telegramUser.getId());

                if (user != null) {
                    handleExistingUser(user, refCode);
                    return ResponseEntity.ok(user.getTelegramId());
                } else {
                    Users newUser = createNewUser(telegramUser, refCode);
                    return ResponseEntity.ok(newUser.getTelegramId());
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid hash");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    private void handleExistingUser(Users user, String refCode) {
        if (!depotRepository.existsByUserId(user.getTelegramId())) {
            depotRepository.save(Account.builder().userId(user.getTelegramId()).coins(user.getCoins()).build());
        }

        if (isValidRefCode(refCode) && !refCode.equals(String.valueOf(user.getTelegramId()))) {
            user.setReferredBy(refCode);
            userRepository.save(user);
        }
    }

    private Users createNewUser(TelegramUser telegramUser, String refCode) {
        long telegramId = telegramUser.getId();
        Users newUser = Users.builder()
                .name(telegramUser.getUsername())
                .telegramId(telegramId)
                .referralCode(String.valueOf(telegramId))
                .build();

        if (isValidRefCode(refCode) && !refCode.equals(String.valueOf(newUser.getTelegramId()))) {
            newUser.setReferredBy(refCode);
        }

        userRepository.save(newUser);
        if (!depotRepository.existsByUserId(newUser.getTelegramId())) {
            depotRepository.save(Account.builder().userId(newUser.getTelegramId()).build());
        }
        return newUser;
    }

    private boolean isValidRefCode(String refCode) {
        if (refCode == null || refCode.isEmpty()) {
            return false;
        }
        try {
            long parsedRefCode = Long.parseLong(refCode);
            return userRepository.existsByTelegramId(parsedRefCode);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse referral code");
        }
    }
}
