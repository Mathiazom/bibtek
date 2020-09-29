package bibtek.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bibtek.json.StorageHandler;

public class LibraryTest {
    // constants
    private final LocalDate someDate = LocalDate.of(2020, 9, 27);
    private final int joyceDate = 1939;
    private final int orwellDate = 1948;
    private final int dostoDate = 1866;
    private final int chriDate = 2016;
    private final Book book1 = new Book("Finnegan's Wake", "James Joyce", joyceDate);
    private final Book book2 = new Book("1984", "George Orwell", orwellDate);
    private final Book book3 = new Book("Crime and Punishment", "Fjodor Dostojevskij", dostoDate);
    private final Book book4 = new Book("Algorithms to Live by", "Brian Christian", chriDate);
    private final BookEntry bookEntry1 = new BookEntry(book1, someDate, BookReadingState.NOT_STARTED);
    private final BookEntry bookEntry2 = new BookEntry(book2, someDate, BookReadingState.COMPLETED);
    private final BookEntry bookEntry3 = new BookEntry(book3, someDate, BookReadingState.READING);
    private final BookEntry bookEntry4 = new BookEntry(book4, someDate, BookReadingState.ABANDONED);
    /**
     * Set the storage handler.
     */
    private StorageHandler sh;
    private StorageHandler sh2;

    /**
     * The storgaHandler should be emtied before each test.
     */
    @BeforeEach
    public void clearStorageHanndler() {
        try {
            sh = new StorageHandler("target/testLibrary.json");
            sh2 = new StorageHandler("target/testLibrary2.json");
            sh.storeBookEntries(null);
            sh2.storeBookEntries(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testing if the toString method returns excpected string.
     */
    @Test
    public void toStringTest() {
        Library lib = new Library("target/testLibrary2.json");
        lib.addBookEntry(bookEntry1);
        lib.addBookEntry(bookEntry2);
        lib.addBookEntry(bookEntry3);
        lib.addBookEntry(bookEntry4);
        String actualString = lib.toString();
        String expectedString = "Book entries: { \n" + "bookEntry: { title: Finnegan's Wake, author: James Joyce, "
                + "yearPublished: 1939, dateAcquired: 2020-09-27, readingState: NOT_STARTED },\n"
                + "bookEntry: { title: Algorithms to Live by, author: Brian Christian, yearPublished: 2016, "
                + "dateAcquired: 2020-09-27, readingState: ABANDONED },\n"
                + "bookEntry: { title: Crime and Punishment, author: Fjodor Dostojevskij, yearPublished: 1866, "
                + "dateAcquired: 2020-09-27, readingState: READING },\n"
                + "bookEntry: { title: 1984, author: George Orwell, yearPublished: 1948, "
                + "dateAcquired: 2020-09-27, readingState: COMPLETED },\n}";
        // test that the toString creates string like expected
        assertEquals(expectedString, actualString, "The toString method does not create a String like expected");
        // test if the toString method creates correct String with an empty Library
        Library emptyLib = new Library("target/testLibrary.json");
        String expectedString2 = "No books in library.";
        String actualString2 = emptyLib.toString();
        assertEquals(expectedString2, actualString2, "The toString method created wrong string for a null library");
    }

    /**
     * Testing if removeBookEntry method works as expected.
     */
    @Test
    public void removeBookEntryTest() {
        // create library with one less book than in bookEntries
        Library libk = new Library("target/testLibrary.json");
        libk.addBookEntry(bookEntry1);
        libk.addBookEntry(bookEntry2);
        libk.addBookEntry(bookEntry3);
        // create full library
        Library lib2 = new Library("target/testLibrary2.json");
        lib2.addBookEntry(bookEntry1);
        lib2.addBookEntry(bookEntry2);
        lib2.addBookEntry(bookEntry3);
        lib2.addBookEntry(bookEntry4);
        lib2.removeBookEntry(bookEntry4);
        assertEquals(libk.getBookEntries(), lib2.getBookEntries(), "removeBookEntry() did not work");
    }

    /**
     * Testing the addBookEntryTest method.
     */
    @Test
    public void addBookEntryTest() {
        // Testing if it throws illegalArgumentException when not not valid entry
        // Creating invalid BookEntry
        // Also isValidBookEntry is tested here, as it is a private method
        Book bookNullTitle = new Book(null, "Brian Christian", chriDate);
        Book bookNullAuthor = new Book("Algorithms to Live by", null, chriDate);
        Book bookNull = null;
        BookEntry bookEntryNull = null;
        Library lib1 = new Library("target/testLibrary.json");
        // testing for a null book Entry
        try {
            lib1.addBookEntry(bookEntryNull);
            fail("Should throw IllegalArgumentException when BookEntry is invalid");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as Book
        try {
            lib1.addBookEntry(new BookEntry(bookNull, someDate, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when Book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as date
        try {
            lib1.addBookEntry(new BookEntry(book4, null, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when dateAquired is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as readingState
        try {
            lib1.addBookEntry(new BookEntry(book4, someDate, null));
            fail("Should throw IllegalArgumentException when readingState is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as author
        try {
            lib1.addBookEntry(new BookEntry(bookNullAuthor, someDate, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when author of the book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as title
        try {
            lib1.addBookEntry(new BookEntry(bookNullTitle, someDate, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when title of the book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing if it adds the bookEntry to bookEntries
        lib1.addBookEntry(bookEntry1);
        Set<BookEntry> expected = new HashSet<>();
        expected.add(bookEntry1);
        assertEquals(expected, lib1.getBookEntries(),
                "The book entries in the library was not as expected after addBookEntry() on lib1");
    }
}