package bibtek.core;

public enum BookReadingState {

    /**
     * The reader has not started reading the book.
     */
    NOT_STARTED("Not started"),
    /**
     * The reader is currently reading the book.
     */
    READING("Reading"),
    /**
     * The reader has completed the book.
     */
    COMPLETED("Completed"),
    /**
     * The reader did not complete the book.
     */
    ABANDONED("Abandoned");

    private final String s;

    BookReadingState(final String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return this.s;
    }

    /**
     * Convert string to a valid BookReadingState.
     * @param s string representing a reading state
     * @return BookReadingState, or null if invalid string
     */
    public static BookReadingState fromString(final String s) {

        for (BookReadingState readingState : values()) {

            if (readingState.toString().equals(s)) {
                return readingState;
            }

        }

        return null;

    }


}
