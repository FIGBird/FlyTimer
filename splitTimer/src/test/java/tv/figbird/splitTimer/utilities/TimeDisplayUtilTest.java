package tv.figbird.splitTimer.utilities;


import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeDisplayUtilTest {


    @Test
    void getDisplayTimeProducesExpectedValues() {
        assertEquals("1:00:00.01", TimeDisplayUtil.getDisplayTime(3600010));
        assertEquals("1:00.01", TimeDisplayUtil.getDisplayTime(60010));
        assertEquals("1.01", TimeDisplayUtil.getDisplayTime(1010));
        assertEquals("0.01", TimeDisplayUtil.getDisplayTime(10));
    }

    @Test
    void convertToStringRemovesUnnecessaryLeadingZeroes() {
        assertEquals("0.01", TimeDisplayUtil.convertToString(0, 0, 0, 10));
        assertEquals("1.00", TimeDisplayUtil.convertToString(0, 0, 1, 0));
        assertEquals("1:00.00", TimeDisplayUtil.convertToString(0, 1, 0, 0));
        assertEquals("1:00:00.00", TimeDisplayUtil.convertToString(1, 0, 0, 0));
    }

    @Test
    void addLeadingZeroesAddsExpectedZeroes() {
        assertEquals("002", TimeDisplayUtil.addLeadingZeros(2, 3));
        assertEquals("02", TimeDisplayUtil.addLeadingZeros(2, 2));
        assertEquals("2", TimeDisplayUtil.addLeadingZeros(2, 1));
        assertEquals("00", TimeDisplayUtil.addLeadingZeros(0, 2));
    }

    @Test
    void subtractTimeReturnsExpectedValues() {
        assertEquals(1, TimeDisplayUtil.subtractTimeUnits(1001, 1, TimeUnit.SECONDS));
        assertEquals(1, TimeDisplayUtil.subtractTimeUnits(60001, 1, TimeUnit.MINUTES));
        assertEquals(1, TimeDisplayUtil.subtractTimeUnits(3600001, 1, TimeUnit.HOURS));
    }
}