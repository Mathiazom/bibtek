package bibtek.core;

public enum BookReadingState {
    /**
     * The reader has not started reading the book.
     */
    NOT_STARTED,
    /**
     * The reader is currently reading the book.
     */
    READING,
    /**
     * The reader has completed the book.
     */
    COMPLETED,
    /**
     * The reader did not complete the book.
     */
    ABANDONED

}
