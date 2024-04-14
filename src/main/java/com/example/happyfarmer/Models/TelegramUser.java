package com.example.happyfarmer.Models;

import lombok.Data;

@Data
public class TelegramUser {
    Boolean allows_write_to_pm;
    String first_name;
    Long id;
    Boolean is_premium;
    String language_code;
    String last_name;
    String username;
}
