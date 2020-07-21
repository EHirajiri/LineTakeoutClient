package jp.co.greensys.takeout.util;

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
}
