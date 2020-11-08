package bibtek.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class LibraryTest {

    /**
     * Testing if the toString method returns excpected string.
     */
    @Test
    public void toStringTest() {
        Library lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        lib.addBookEntry(TestConstants.BOOK_ENTRY3);
        lib.addBookEntry(TestConstants.BOOK_ENTRY4);
        String actualString = lib.toString();
        String expectedString = "bookEntries: { \n" + "bookEntry: { title: Finnegan's Wake, author: James Joyce, "
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
        Library emptyLib = new Library();
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
        Library libk = new Library();
        libk.addBookEntry(TestConstants.BOOK_ENTRY1);
        libk.addBookEntry(TestConstants.BOOK_ENTRY2);
        libk.addBookEntry(TestConstants.BOOK_ENTRY3);
        // create full library
        Library lib2 = new Library();
        lib2.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib2.addBookEntry(TestConstants.BOOK_ENTRY2);
        lib2.addBookEntry(TestConstants.BOOK_ENTRY3);
        lib2.addBookEntry(TestConstants.BOOK_ENTRY4);
        lib2.removeBookEntry(TestConstants.BOOK_ENTRY4);
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
        Book bookNullTitle = new Book(null, "Brian Christian", TestConstants.CHRI_DATE);
        Book bookNullAuthor = new Book("Algorithms to Live by", null, TestConstants.CHRI_DATE);
        Book bookNull = null;
        BookEntry bookEntryNull = null;
        Library lib1 = new Library();
        // testing for a null book Entry
        try {
            lib1.addBookEntry(bookEntryNull);
            fail("Should throw IllegalArgumentException when BookEntry is invalid");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as Book
        try {
            lib1.addBookEntry(new BookEntry(bookNull, TestConstants.SOME_DATE, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when Book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as date
        try {
            lib1.addBookEntry(new BookEntry(TestConstants.BOOK4, null, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when dateAquired is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as readingState
        try {
            lib1.addBookEntry(new BookEntry(TestConstants.BOOK4, TestConstants.SOME_DATE, null));
            fail("Should throw IllegalArgumentException when readingState is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as author
        try {
            lib1.addBookEntry(new BookEntry(bookNullAuthor, TestConstants.SOME_DATE, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when author of the book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        // Testing for a BookEntry with a null as title
        try {
            lib1.addBookEntry(new BookEntry(bookNullTitle, TestConstants.SOME_DATE, BookReadingState.NOT_STARTED));
            fail("Should throw IllegalArgumentException when title of the book is null");
        } catch (IllegalArgumentException e) {
            // Succeeds
        }
        Library lib2 = new Library();
        // Testing if it adds the bookEntry to bookEntries
        lib2.addBookEntry(TestConstants.BOOK_ENTRY1);
        Set<BookEntry> expected = new HashSet<>();
        expected.add(TestConstants.BOOK_ENTRY1);
        assertEquals(expected, lib2.getBookEntries(),
                "The book entries in the library was not as expected after addBookEntry() on lib1");
    }
}
