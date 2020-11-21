package bibtek.ui;

import bibtek.core.*;
import bibtek.json.BooksAPIHandler;
import bibtek.json.LocalDateDeserializer;
import bibtek.json.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class TestConstants {

        /**
         * Sleep time between each typed character.
         */
        static final int WRITE_ROBOT_PAUSE_MILLIS = 1;

        /**
         * Sleep time between operations to simulate user behaviour.
         */
        static final int ROBOT_PAUSE_MS = 300;

        private static final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();

        private TestConstants() {

        }

        public static String userMapDanteJson() {

                final UserMap userMap = new UserMap();

                userMap.putUser(userDante());

                return gson.toJson(userMap);

        }

        public static String userDanteJson() {

                return gson.toJson(userDante());

        }

        public static User userDante() {

                final Library library = new Library();

                final int danteBigBoyAge = 800;

                final int dummyBookYear2 = 1948;

                library.addBookEntry(fahrenheit());
                library.addBookEntry(new BookEntry(new Book("1984", "George Orwell", dummyBookYear2), LocalDate.now(),
                                BookReadingState.COMPLETED));

                library.addBookEntry(new BookEntry(new BooksAPIHandler().searchForBooks("9780241242643").get(0),
                                LocalDate.now(), BookReadingState.ABANDONED));

                library.addBookEntry(new BookEntry(new BooksAPIHandler().searchForBooks("9780765394866").get(0),
                                LocalDate.now(), BookReadingState.NOT_STARTED));

                return new User("dante", danteBigBoyAge, library);

        }

        public static BookEntry fahrenheit(){

                final int fahrenheitYear = 1953;

                return new BookEntry(new Book("Fahrenheit 451", "Ray Bradbury", fahrenheitYear,
                        "https://s2982.pcdn.co/wp-content/uploads/2017/09/fahrenheit-451-flamingo-edition.jpg"),
                        LocalDate.now(), BookReadingState.NOT_STARTED);

        }

        public static String userHeinrichJson(){

                return gson.toJson(new User("heinrich", 17));

        }

}
