package bibtek.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void fullConstructorTest() {
        Library lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        lib.addBookEntry(TestConstants.BOOK_ENTRY3);
        lib.addBookEntry(TestConstants.BOOK_ENTRY4);
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
