package bibtek.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import bibtek.core.Library;
import bibtek.core.TestConstants;
import bibtek.core.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private LocalStorageHandler sh;
    private File tempFile;
    private Library lib;
    private User user;

    /**
     * Initilize he storage hanlder with a temporary file.
     */
    @BeforeEach
    public void intitilizeStorageHandler() {

        try {
            tempFile = File.createTempFile("testLibrary", ".json");
            sh = new StorageHandler(tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        user = new User("Name", TestConstants.AGE20, lib);
    }

    /**
     * Test the fetchBookEntries method.
     */
    // @Test
    public void fetchUserTest() {
        /*
         * // Testing if it thows IOException if path does not exist try { <<<<<<< HEAD
         * sh = new DirectStorageHandler("target/testLibrary.json"); } catch
         * (IOException e1) { e1.printStackTrace(); return; } try {
         * 
         * ======= >>>>>>> c3ace10... Update StorageHandlerTest
         * sh.setStoragePath("nonExistent/nonExistentLibrary.json"); User nonExistent =
         * sh.getLocalUser(); System.out.println("This should be empty: " +
         * nonExistent.toString());
         * fail("Should throw IOException when trying to get data from a non-excistent file"
         * ); } catch (IOException e) { // Succeeds }
         */
    }

    /**
     * Tests the storeUserLocally() method.
     */
    @Test
    public void storeUserLocallyTest() {
        // Test if it creates the correct json file.

        try {
            sh.storeUserLocally(user);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.getAbsolutePath()))) {
            String actual = reader.lines().reduce("", (a, b) -> a + b);
            String expected = "{  \"userName\": \"Name\",  \"age\": 20,"
                    + "  \"library\": {    \"bookEntries\": [      {        \"book\": "
                    + "{          \"title\": \"Finnegan\\u0027s Wake\",          "
                    + "\"author\": \"James Joyce\",          \"yearPublished\": 1939, "
                    + "         \"imgPath\": \"\"        },        \"dateAcquired\": \"2020-09-27\","
                    + "        \"readingState\": \"NOT_STARTED\"      },      {        \"book\": {"
                    + "          \"title\": \"1984\",          \"author\": \"George Orwell\","
                    + "          \"yearPublished\": 1948,          \"imgPath\": \"\"        },        "
                    + "\"dateAcquired\": \"2020-09-27\",        \"readingState\": \"COMPLETED\"      " + "}    ]  }}";
            assertEquals(expected, actual, "The file was not created correctly");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * Tests the method updateUser().
     */
    @Test
    public void updateUserTest() {
        // Test that it thorws exception if you try to update the userName
        try {
            sh.storeUserLocally(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
        User user2 = new User("Another Name", TestConstants.AGE20, lib);
        try {
            sh.updateUser(user2);
            fail("Shoould throw IOException when it tries to update the local user with a user with another username");
        } catch (Exception e) {
            // Succeeds
        }

        // Test if it actually saves the new changes.
        User user3 = new User("Name", TestConstants.AGE21, lib);
        try {
            sh.updateUser(user3);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.getAbsolutePath()))) {
            String actual = reader.lines().reduce("", (a, b) -> a + b);
            String expected = "{  \"userName\": \"Name\",  \"age\": 21,"
                    + "  \"library\": {    \"bookEntries\": [      {        \"book\": "
                    + "{          \"title\": \"Finnegan\\u0027s Wake\",          "
                    + "\"author\": \"James Joyce\",          \"yearPublished\": 1939, "
                    + "         \"imgPath\": \"\"        },        \"dateAcquired\": \"2020-09-27\","
                    + "        \"readingState\": \"NOT_STARTED\"      },      {        \"book\": {"
                    + "          \"title\": \"1984\",          \"author\": \"George Orwell\","
                    + "          \"yearPublished\": 1948,          \"imgPath\": \"\"        },        "
                    + "\"dateAcquired\": \"2020-09-27\",        \"readingState\": \"COMPLETED\"      " + "}    ]  }}";
            assertEquals(expected, actual, "The file was not created correctly");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

}
