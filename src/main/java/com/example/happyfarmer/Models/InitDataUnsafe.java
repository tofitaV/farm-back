package com.example.happyfarmer.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InitDataUnsafe {
    String auth_date;
    String hash;
    String query_id;
    TelegramUser user;
    String start_param;
}
