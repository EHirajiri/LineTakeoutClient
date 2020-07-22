package jp.co.greensys.takeout.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Tokyo");

    public static LocalDateTime getDateOfToday(int hour, int minute) {
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
        dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return dateTime.toLocalDateTime();
    }

    public static String parseString(LocalDateTime dateTime) {
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.JAPAN);
        return LocalDateTime.parse(date, dtf);
    }
}
