package bibtek.restserver;

import java.time.LocalDate;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.Library;
import bibtek.core.User;
import bibtek.json.BooksAPIHandler;

public final class ServerUtil {

    private ServerUtil() {

    }

    private static User userDante() {

        final Library library = new Library();

        final int danteBigBoyAge = 800;

        final int dummyBookYear = 1953;
        final int dummyBookYear2 = 1948;

        library.addBookEntry(new BookEntry(
                new Book("Fahrenheit 451", "Ray Bradbury", dummyBookYear,
                        "https://s2982.pcdn.co/wp-content/uploads/2017/09/fahrenheit-451-flamingo-edition.jpg"),
                LocalDate.now(), BookReadingState.READING));
        library.addBookEntry(new BookEntry(new Book("1984", "George Orwell", dummyBookYear2), LocalDate.now(),
                BookReadingState.COMPLETED));

        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9780241242643"), LocalDate.now(),
                BookReadingState.ABANDONED));

        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9780765394866"), LocalDate.now(),
                BookReadingState.NOT_STARTED));

        return new User("dante", danteBigBoyAge, library);

    }

    private static User userVergil() {
        final Library library = new Library();

        final int vergilBigBoyAge = 2090;

        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9780192835840"), LocalDate.now(),
                BookReadingState.COMPLETED));
        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9781686172717"), LocalDate.now(),
                BookReadingState.NOT_STARTED));

        final User dummyUser = new User("vergil", vergilBigBoyAge, library);

        return dummyUser;

    }

    private static User userDanteEdited() {
        final Library library = new Library();

        final int danteBigBoyAge = 821;

        final int dummyBookYear2 = 1948;

        library.addBookEntry(new BookEntry(new Book("1984", "George Orwell", dummyBookYear2), LocalDate.now(),
                BookReadingState.COMPLETED));

        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9780241242643"), LocalDate.now(),
                BookReadingState.ABANDONED));

        library.addBookEntry(new BookEntry(new BooksAPIHandler().fetchBook("9780765394866"), LocalDate.now(),
                BookReadingState.READING));

        return new User("dante", danteBigBoyAge, library);

    }

    /**
     * The dummy user Vergil.
     */
    public static final User VERGIL_USER = userVergil();

    /**
     * The dummy user Dante.
     */
    public static final User DANTE_USER = userDante();

    /**
     * Edited Dante user.
     */
    public static final User DANTE_USER_EDITED = userDanteEdited();

}
