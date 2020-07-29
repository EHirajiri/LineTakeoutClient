package jp.co.greensys.takeout.util;

import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Tokyo");

    public static long getDateOfToday(int hour, int minute) {
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
        return dateTime.toInstant().toEpochMilli();
    }

    public static String toString(long dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        format.setTimeZone(TIME_ZONE);
        return format.format(dateTime);
    }

    public static String toString(String format, long dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TIME_ZONE);
        return sdf.format(dateTime);
    }

    public static List<String> getDeliveryDate() {
        List<String> deliveryDate = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        for (int i = 0; i < 3; i++) {
            calendar.add(Calendar.DATE, i);
            deliveryDate.add(toString("yyyy/MM/dd", calendar.getTimeInMillis()));
        }
        return deliveryDate;
    }

    public static List<String> getDeliveryTime() {
        return ImmutableList.of("12:00", "12:30", "13:00", "13:30");
    }

    public static Instant toInstant(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            Date formatDate = sdf.parse(dateTime);
            return formatDate.toInstant();
        } catch (ParseException e) {
            return null;
        }
    }
}
