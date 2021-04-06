package tv.figbird.flyTimer.splitTimer.utilities;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeDisplayUtilTest {

    @Test
    void subtractTimeSeconds() {
        long result = TimeDisplayUtil.subtractTimeUnits(1500, 1, TimeUnit.SECONDS);

        assertEquals(500, result);
    }

    @Test
    void subtractTimeMinutes() {
        long result = TimeDisplayUtil.subtractTimeUnits(60500, 1, TimeUnit.MINUTES);

        assertEquals(500, result);
    }

    @Test
    void subtractTimeHours() {
        long result = TimeDisplayUtil.subtractTimeUnits(3600500, 1, TimeUnit.HOURS);

        assertEquals(500, result);
    }

    @Test
    void addLeadingZerosSingle() {
        String result = TimeDisplayUtil.addLeadingZeros(1, 3);

        assertEquals("001", result);
    }

    @Test
    void addLeadingZerosDouble() {
        String result = TimeDisplayUtil.addLeadingZeros(10, 3);

        assertEquals("010", result);
    }

    @Test
    void addLeadingZerosTriple() {
        String result = TimeDisplayUtil.addLeadingZeros(100, 3);

        assertEquals("100", result);
    }

    @Test
    void addLeadingZerosZeroPlaces() {
        String result = TimeDisplayUtil.addLeadingZeros(100, 0);

        assertEquals("100", result);
    }

    @Test
    void convertToString() {
        String result = TimeDisplayUtil.convertToString(1, 1, 1, 10);

        assertEquals("1:01:01.01", result);
    }

    @Test
    void getDisplayTime() {
        String result = TimeDisplayUtil.getDisplayTime(100000300);

        assertEquals("27:46:40.30", result);
    }
}