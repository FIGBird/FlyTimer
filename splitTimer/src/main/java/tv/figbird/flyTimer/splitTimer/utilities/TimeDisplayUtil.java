package tv.figbird.flyTimer.splitTimer.utilities;

import java.util.concurrent.TimeUnit;

public class TimeDisplayUtil {


    public static String getDisplayTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis = subtractTimeUnits(millis, hours, TimeUnit.HOURS);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis = subtractTimeUnits(millis, minutes, TimeUnit.MINUTES);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis = subtractTimeUnits(millis, seconds, TimeUnit.SECONDS);

        return convertToString(hours, minutes, seconds, millis);
    }

    protected static long subtractTimeUnits(long millis, long units, TimeUnit timeUnit) {
        millis = millis - timeUnit.toMillis(units);
        return millis;
    }

    protected static String convertToString(long hours, long minutes, long seconds, long millis) {
        StringBuilder builder = new StringBuilder();

        builder.append(hours).append(":")
                .append(addLeadingZeros(minutes, 2)).append(":")
                .append(addLeadingZeros(seconds, 2)).append(".")
                .append(addLeadingZeros(millis, 3));

        String output = builder.toString();
        if (output.startsWith("0:")) {
            output = output.substring(2);
        }
        while (output.startsWith("00:")) {
            output = output.substring(3);
        }
        if (output.startsWith("0")) {
            output = output.substring(1);
        }

        return output.substring(0, output.length()-1);
    }

    protected static String addLeadingZeros(long units, int numberOfPlaces) {
        StringBuilder string = new StringBuilder();
        while (numberOfPlaces >0) {
            double divisor = Math.pow(10, numberOfPlaces-1);

            if (units < divisor) {
                string.append("0");
            }
            numberOfPlaces = numberOfPlaces - 1;
        }
        if (units != 0) {
            string.append(units);
        }
        return string.toString();
    }


}
