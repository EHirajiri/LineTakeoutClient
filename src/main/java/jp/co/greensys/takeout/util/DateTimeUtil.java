package jp.co.greensys.takeout.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd(E) HH:mm");

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Tokyo");

    public static ZonedDateTime getDateOfToday(int hour, int minute) {
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        ZonedDateTime dateTime = ZonedDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE),
            hour,
            minute,
            0,
            0,
            ZoneId.of("Asia/Tokyo")
        );
        return dateTime;
    }

    public static String parseString(ZonedDateTime dateTime) {
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public static ZonedDateTime parseZonedDateTime(String date) {
        return ZonedDateTime.parse(date, DATE_TIME_FORMATTER);
    }
}
