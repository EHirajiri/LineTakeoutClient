package jp.co.greensys.takeout.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarUtil {
    private static final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

    public static Date getDateOfToday(int hour, int minute, int second, int millisecond, int amount) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        calendar.add(Calendar.DATE, amount);

        return calendar.getTime();
    }

    public static ZonedDateTime getDateOfToday(int hour, int minute) {
        Calendar calendar = Calendar.getInstance(timeZone);
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
}
