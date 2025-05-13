package group70.quackstagram.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeStampFormatter {

    public static String getElapsedTimestamp(Timestamp timestamp) {
        LocalDateTime timeOfNotification = timestamp.toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long hoursBetween = ChronoUnit.HOURS.between(timeOfNotification, currentTime) % 24;
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        if (daysBetween == 0 && hoursBetween == 0 && minutesBetween == 0) {
            return "Just now";
        }

        StringBuilder timeElapsed = new StringBuilder();

        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }

        if (hoursBetween > 0) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(", ");
            }
            timeElapsed.append(hoursBetween).append(" hour").append(hoursBetween > 1 ? "s" : "");
        }

        if (minutesBetween > 0) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }

        return timeElapsed + " ago";
    }
}
