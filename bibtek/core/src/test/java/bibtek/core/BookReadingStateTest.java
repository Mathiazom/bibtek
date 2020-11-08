package bibtek.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BookReadingStateTest {

    /**
     * Testing if the fromString() static method returns correctly.
     */
    @Test
    public void fromStringTest() {

        // Testing if it returns correctly for every enum
        BookReadingState notStart = BookReadingState.fromString("Not started");
        BookReadingState reading = BookReadingState.fromString("Reading");
        BookReadingState completed = BookReadingState.fromString("Completed");
        BookReadingState abandoned = BookReadingState.fromString("Abandoned");
        BookReadingState nullstate = BookReadingState.fromString("bwsedwasv");
        assertEquals(BookReadingState.NOT_STARTED.toString(), notStart.toString(), "fromString() returns wrong enum");
        assertEquals(BookReadingState.READING.toString(), reading.toString(), "fromString() returns wrong enum");
        assertEquals(BookReadingState.COMPLETED.toString(), completed.toString(), "fromString() returns wrong enum");
        assertEquals(BookReadingState.ABANDONED.toString(), abandoned.toString(), "fromString() returns wrong enum");
        assertEquals(null, nullstate, "fromString() returns wrong enum");

    }

}
