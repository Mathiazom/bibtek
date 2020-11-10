package bibtek.core;

import java.time.LocalDate;

/**
 * Class for constnats used in tests.
 */
public final class TestConstants {

    private TestConstants() {

    }

    /**
     * Constant date.
     */
    public static final LocalDate SOME_DATE = LocalDate.of(2020, 9, 27);
    /**
     * Joyce date.
     */
    public static final int JOYCE_DATE = 1939;
    /**
     * Orwell date.
     */
    public static final int ORWELL_DATE = 1948;
    /**
     * Dosto date.
     */
    public static final int DOSTO_DATE = 1866;
    /**
     * Chri date.
     */
    public static final int CHRI_DATE = 2016;
    /**
     * Joyce book.
     */
    public static final Book BOOK1 = new Book("Finnegan's Wake", "James Joyce", JOYCE_DATE);
    /**
     * Orwell book.
     */
    public static final Book BOOK2 = new Book("1984", "George Orwell", ORWELL_DATE);
    /**
     * Dosto book.
     */
    public static final Book BOOK3 = new Book("Crime and Punishment", "Fjodor Dostojevskij", DOSTO_DATE);
    /**
     * Chri book.
     */
    public static final Book BOOK4 = new Book("Algorithms to Live by", "Brian Christian", CHRI_DATE);
    /**
     * BookEntry1.
     */
    public static final BookEntry BOOK_ENTRY1 = new BookEntry(BOOK1, SOME_DATE, BookReadingState.NOT_STARTED);
    /**
     * BookEntry2.
     */
    public static final BookEntry BOOK_ENTRY2 = new BookEntry(BOOK2, SOME_DATE, BookReadingState.COMPLETED);
    /**
     * BookEntry3.
     */
    public static final BookEntry BOOK_ENTRY3 = new BookEntry(BOOK3, SOME_DATE, BookReadingState.READING);
    /**
     * BookEntry4.
     */
    public static final BookEntry BOOK_ENTRY4 = new BookEntry(BOOK4, SOME_DATE, BookReadingState.ABANDONED);
    /**
     * User age 20.
     */
    public static final int AGE20 = 20;
    /**
     * User age 21.
     */
    public static final int AGE21 = 21;

}
