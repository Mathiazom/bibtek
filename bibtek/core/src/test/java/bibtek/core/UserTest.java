package bibtek.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class UserTest {
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

    @Test
    public void fullConstructorTest() {
        Library lib = new Library();
        lib.addBookEntry(bookEntry1);
        lib.addBookEntry(bookEntry2);
        lib.addBookEntry(bookEntry3);
        lib.addBookEntry(bookEntry4);
        final int age = 12;
        User user1 = new User("Name", age, lib);
        String expected = "user: { \n" + "userName: Name\n" + "age: 12\n" + "library: bookEntries: { \n"
                + "bookEntry: { title: Finnegan's Wake, author: James Joyce, yearPublished: 1939, dateAcquired: 2020-09-27, readingState: NOT_STARTED },\n"
                + "bookEntry: { title: Algorithms to Live by, author: Brian Christian, yearPublished: 2016, dateAcquired: 2020-09-27, readingState: ABANDONED },\n"
                + "bookEntry: { title: Crime and Punishment, author: Fjodor Dostojevskij, yearPublished: 1866, dateAcquired: 2020-09-27, readingState: READING },\n"
                + "bookEntry: { title: 1984, author: George Orwell, yearPublished: 1948, dateAcquired: 2020-09-27, readingState: COMPLETED },\n"
                + "}\n" + "}";
        assertEquals(expected, user1.toString(),
                "The constructor and/or the toString() method does not work as expected");

    }

    @Test
    public void smallConstructorTest() {
        final int age2 = 44;
        User user2 = new User("Michael", age2);
        String expected2 = "user: { \n" + "userName: Michael\n" + "age: 44\n" + "library: No books in library.\n" + "}";
        assertEquals(expected2, user2.toString(),
                "The constructor and/or the toString() method does not work as expected");
    }

}
