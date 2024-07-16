package com.example.happyfarmer.Services;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ReferralService {

    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom;
    private String key = System.getProperty("secretKey");

    public ReferralService() throws Exception {
        this.secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
        this.secureRandom = new SecureRandom();
    }

    public String encrypt(String inviterId) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] encryptedText = cipher.doFinal(inviterId.getBytes(StandardCharsets.UTF_8));

        byte[] encryptedIvAndText = new byte[GCM_IV_LENGTH + encryptedText.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedText, 0, encryptedIvAndText, GCM_IV_LENGTH, encryptedText.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndText);
    }

    public String decrypt(String encryptedInviterId) throws Exception {
        try {
            byte[] encryptedIvAndText = Base64.getDecoder().decode(encryptedInviterId);
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encryptedIvAndText, 0, iv, 0, iv.length);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedText = new byte[encryptedIvAndText.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedIvAndText, GCM_IV_LENGTH, encryptedText, 0, encryptedText.length);

            byte[] decryptedText = cipher.doFinal(encryptedText);

            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoded string", e);
        } catch (javax.crypto.AEADBadTagException e) {
            throw e;
        }
    }
}
