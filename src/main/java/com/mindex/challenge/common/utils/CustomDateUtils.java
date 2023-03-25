package com.mindex.challenge.common.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom Date utils
 */
@Component
public class CustomDateUtils {

    public static final String UTC = "UTC";

    public static LocalDateTime convertFromUtc(LocalDateTime dateTimeUtc, ZoneId timezone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTimeUtc, ZoneId.of(UTC));
        ZonedDateTime convertedDateTime = zonedDateTime.withZoneSameInstant(timezone);
        return convertedDateTime.toLocalDateTime();
    }

    public static LocalDateTime convertToUtc(LocalDateTime dateTime, ZoneId timezone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, timezone);
        ZonedDateTime convertedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(UTC));
        return convertedDateTime.toLocalDateTime();
    }


    public static LocalDate setEffectiveDateToUtcFromHeaders(LocalDate effectiveDate, String timezone, HttpServletRequest request) {

        ZoneId zoneId ;
        if (timezone != null) {
            zoneId = ZoneId.of(timezone);
        }else{
            zoneId = ZoneId.of(getTimezoneFromRequest(request));
        }
        return convertEffectiveDateToUtcForDb(effectiveDate, zoneId);
    }

    public static String getTimezoneFromClient(String timezone, HttpServletRequest request){
        String zoneId;
        if (timezone != null) {
            zoneId = timezone;
        }else{
            zoneId = getTimezoneFromRequest(request);
        }
        return zoneId;
    }

    public static String getTimezoneFromRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String timezone = "UTC";
        if (userAgent != null) {
            Matcher matcher = Pattern.compile("\\b\\w+/\\w+\\s*\\((.*)\\)").matcher(userAgent);
            if (matcher.find()) {
                timezone = matcher.group(1);
            }
        }
        return timezone;
    }
    public static LocalDate convertEffectiveDateToUtcForDb(LocalDate date, ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.MIN);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return utcZonedDateTime.toLocalDate();
    }

    public static LocalDate convertFromUtcToLocalEffectiveDate(LocalDate utcDate, ZoneId zoneId) {
        LocalDateTime utcDateTime = LocalDateTime.of(utcDate, LocalTime.MIN);
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime zonedDateTime = utcZonedDateTime.withZoneSameInstant(zoneId);
        return zonedDateTime.toLocalDate();
    }






}