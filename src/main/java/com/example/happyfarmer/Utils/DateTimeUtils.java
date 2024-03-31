package com.example.happyfarmer.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    public static LocalDateTime justifyDateForClient(LocalDateTime time, String timezone){
        ZoneId clientZone = ZoneId.of(timezone);
        ZonedDateTime clientDateTime = time.atZone(ZoneId.of("UTC")).withZoneSameInstant(clientZone);
        return clientDateTime.toLocalDateTime();
    }
}
