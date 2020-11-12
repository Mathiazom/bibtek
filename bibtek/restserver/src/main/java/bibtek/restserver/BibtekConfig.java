package bibtek.restserver;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;
import bibtek.core.Library;
import bibtek.core.User;
import bibtek.core.UserMap;
import bibtek.json.BooksAPIHandler;
import bibtek.restapi.UserMapService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.time.LocalDate;

public final class BibtekConfig extends ResourceConfig {

    /**
     * Setup server configs.
     * @param userMap map of Users to start server with
     */
    public BibtekConfig(final UserMap userMap) {
        register(UserMapService.class);
        register(GsonProvider.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(userMap);
            }
        });
    }

    /**
     * Setup server with default data source.
     */
    public BibtekConfig() {
        this(createDefaultUserMap());
    }


    /**
     *
     * Populate user map with dummy user and book entries.
     *
     * @return UserMap with dummy user
     */
    private static UserMap createDefaultUserMap() {

        final UserMap userMap = new UserMap();

        final Library library = new Library();

        final int danteBigBoyAge = 800;
        final User dummyUser = new User("dante", danteBigBoyAge, library);

        final int dummyBookYear = 1953;
        final int dummyBookYear2 = 1948;

        library.addBookEntry(
                new BookEntry(
                        new Book(
                                "Fahrenheit 451",
                                "Ray Bradbury",
                                dummyBookYear,
                                "https://s2982.pcdn.co/wp-content/uploads/2017/09/fahrenheit-451-flamingo-edition.jpg"
                        ),
                        LocalDate.now(),
                        BookReadingState.READING
                )
        );
        library.addBookEntry(
                new BookEntry(
                        new Book(
                                "1984",
                                "George Orwell",
                                dummyBookYear2
                        ),
                        LocalDate.now(),
                        BookReadingState.COMPLETED
                )
        );

        library.addBookEntry(
                new BookEntry(
                        new BooksAPIHandler().fetchBook("9780241242643"),
                        LocalDate.now(),
                        BookReadingState.ABANDONED
                )
        );

        library.addBookEntry(
                new BookEntry(
                        new BooksAPIHandler().fetchBook("9783944283111"),
                        LocalDate.now(),
                        BookReadingState.NOT_STARTED
                )
        );

        userMap.putUser(dummyUser);
        return userMap;

    }

}
