package com.example.happyfarmer;

import com.example.happyfarmer.Models.*;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Repositories.UserRepository;
import com.example.happyfarmer.Services.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class ValidateData {
    private static final String BOT_TOKEN = System.getProperty("botToken");

    private final UserRepository userRepository;
    private final DepotRepository depotRepository;
    private final ReferralService referralService;

    @Autowired
    public ValidateData(UserRepository userRepository, DepotRepository depotRepository, ReferralService referralService) {
        this.userRepository = userRepository;
        this.depotRepository = depotRepository;
        this.referralService = referralService;
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public ResponseEntity<?> validateData(@RequestBody TelegramUserInfo telegramUserInfo) {
        InitDataUnsafe initDataUnsafe = telegramUserInfo.getInitDataUnsafe();
        String receivedHash = initDataUnsafe.getHash();
        String refCode = initDataUnsafe.getStart_param();

        String dataCheckString = createDataCheckString(initDataUnsafe);
        String secretKey = generateSecretKey(BOT_TOKEN);

        String computedHash = computeHmacSHA256(dataCheckString.getBytes(StandardCharsets.UTF_8), secretKey.getBytes(StandardCharsets.UTF_8));
        //if (computedHash.equals(receivedHash)) {
        if (true) {
            try {
                TelegramUser telegramUser = initDataUnsafe.getUser();
                Users user = userRepository.findByTelegramId(telegramUser.getId());
                if (user != null) {
                    if (!depotRepository.existsByUserId(user.getTelegramId())) {
                        depotRepository.save(Account.builder().userId(telegramUser.getId()).coins(user.getCoins()).build());
                    }
                    long parsedRefCode;
                    if (refCode != null) {
                        if (!refCode.isEmpty()) {
                            try {
                                parsedRefCode = Long.parseLong(refCode);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException("Cannot parse");
                            }
                            if (userRepository.existsByTelegramId(parsedRefCode)) {
                                if (parsedRefCode != user.getTelegramId()) {
                                    user.setReferredBy(refCode);
                                    userRepository.save(user);
                                }
                            }
                        }
                    }
                    return ResponseEntity.ok(user.getTelegramId());
                } else {
                    long telegramId = telegramUser.getId();
                    Users newUser = Users.builder()
                            .name(telegramUser.getUsername())
                            .telegramId(telegramId)
                            .referralCode(String.valueOf(telegramId))
                            .build();
                    long parsedRefCode;
                    if (refCode != null) {
                        if (!refCode.isEmpty()) {
                            try {
                                parsedRefCode = Long.parseLong(refCode);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException("Cannot parse");
                            }
                            if (userRepository.existsByTelegramId(parsedRefCode)) {
                                if (parsedRefCode != newUser.getTelegramId()) {
                                    newUser.setReferredBy(refCode);
                                }
                            }
                        }
                    }
                    userRepository.save(newUser);
                    depotRepository.save(Account.builder().userId(newUser.getTelegramId()).build());
                    return ResponseEntity.ok(newUser.getTelegramId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
            }
        } else {
            return ResponseEntity.ok(294367378);
        }
    }

    private String createDataCheckString(InitDataUnsafe initDataUnsafe) {
        TreeMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("auth_date", initDataUnsafe.getAuth_date());
        sortedMap.put("hash", initDataUnsafe.getHash());
        sortedMap.put("query_id", initDataUnsafe.getQuery_id());
        TelegramUser user = initDataUnsafe.getUser();
        if (user != null) {
            sortedMap.put("allows_write_to_pm", String.valueOf(user.getAllows_write_to_pm()));
            sortedMap.put("first_name", user.getFirst_name());
            sortedMap.put("id", String.valueOf(user.getId()));
            sortedMap.put("is_premium", String.valueOf(user.getIs_premium()));
            sortedMap.put("language_code", user.getLanguage_code());
            sortedMap.put("last_name", user.getLast_name());
            sortedMap.put("username", user.getUsername());
        }
        StringBuilder dataCheckString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            dataCheckString.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return dataCheckString.toString();
    }

    private String generateSecretKey(String botToken) {
        return computeHmacSHA256(botToken.getBytes(StandardCharsets.UTF_8), "WebAppData".getBytes(StandardCharsets.UTF_8));
    }

    private String computeHmacSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data);
            return bytesToHex(hmacData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
