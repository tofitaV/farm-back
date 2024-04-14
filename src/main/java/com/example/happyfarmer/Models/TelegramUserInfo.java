package com.example.happyfarmer.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramUserInfo {
    String initData;
    InitDataUnsafe initDataUnsafe;
}
