package bibtek.restserver;

import bibtek.core.*;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.time.LocalDate;

public final class BibtekConfig extends ResourceConfig {

    private UserMap userMap;


    public BibtekConfig(final UserMap userMap) {
        setUserMap(userMap);
        packages("bibtek.restapi");
        register(GsonMessageBodyHandler.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(userMap);
            }
        });
    }


    public BibtekConfig() {
        this(createDefaultUserMap());
    }

    public UserMap getUserMap() {
        return userMap;
    }

    public void setUserMap(final UserMap userMap) {
        this.userMap = userMap;
    }

    private static UserMap createDefaultUserMap() {
        UserMap userMap = new UserMap();
        final int danteBigBoyAge = 800;
        final Library library = new Library();
        User user1 = new User("dante", danteBigBoyAge, library);

        final int dummyBookYear = 1953;
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

        userMap.putUser(user1);
        return userMap;

    }

}
