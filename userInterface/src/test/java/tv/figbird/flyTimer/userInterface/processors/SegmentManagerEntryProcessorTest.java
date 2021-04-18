package tv.figbird.flyTimer.userInterface.processors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SegmentManagerEntryProcessorTest {

    @Test
    void processStringNoLetters() {
        String result = SegmentManagerEntryProcessor.processStringInputForTimes("12345");

        assertEquals("12345", result);
    }

    @Test
    void processStringNoNumbers() {
        String result = SegmentManagerEntryProcessor.processStringInputForTimes("asdfg");

        assertEquals("", result);
    }

    @Test
    void processStringMixedCharacters() {
        String result = SegmentManagerEntryProcessor.processStringInputForTimes("1l23k4s5");

        assertEquals("12345", result);
    }
}