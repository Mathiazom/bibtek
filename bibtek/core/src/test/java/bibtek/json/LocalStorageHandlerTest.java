package bibtek.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import bibtek.core.Library;
import bibtek.core.TestConstants;
import bibtek.core.User;
import bibtek.core.UserMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocalStorageHandlerTest {
    /**
     * Set the storage handler.
     */
    private LocalStorageHandler localStorageHandler;
    private File tempFile;
    private Library lib;
    private User user;
    private User user2;
    private UserMap userMap;

    /**
     * Initilize he storage hanlder with a temporary file.
     */
    @BeforeEach
    public void intitilizeStorageHandler() {

        try {
            tempFile = File.createTempFile("usersTest", ".json");
            localStorageHandler = new LocalStorageHandler(tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lib = new Library();
        lib.addBookEntry(TestConstants.BOOK_ENTRY1);
        lib.addBookEntry(TestConstants.BOOK_ENTRY2);
        user = new User("Name", TestConstants.AGE20, lib);
        user2 = new User("Another Name", TestConstants.AGE21, lib);
        userMap = new UserMap();
    }

    /**
     * Test the putUser method.
     */
    @Test
    public void putUserTest() {
        // Test if the putUser stores the user as exptected
        localStorageHandler.putUser(user);
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.getAbsolutePath()))) {
            final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();
            UserMap actualUserMap = gson.fromJson(reader, new TypeToken<UserMap>() {
            }.getType());
            userMap.putUser(user);
            assertEquals(userMap, actualUserMap, "The LocalStorageHadlers putUser mathed did not store user correctly");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Test the getUserMap method.
     */
    @Test
    public void getUserMapTest() {
        // Test if it returns an empty UserMap when the file is empty
        UserMap actual1 = localStorageHandler.getUserMap();
        UserMap expected1 = new UserMap();
        assertEquals(expected1, actual1, "getUserMap() did not return an empty userMap when the file is empty");

        // Test if it returns expected UserMap
        UserMap expected2 = new UserMap();
        expected2.putUser(user);
        expected2.putUser(user2);
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        UserMap actual2 = localStorageHandler.getUserMap();
        assertEquals(expected2, actual2, "getUserMap() did not return expected UserMap");

    }

    /**
     * Test if the hasUser() method works as expeted.
     */
    @Test
    public void hasUserTest() {
        // Test if it returns false on an empty file
        boolean actual = localStorageHandler.hasUser("anything");
        assertFalse(actual, "Should return false when the hasUser() is called on an empty file");

        // Test if the hasUser() returns false on a nonEmpty file
        localStorageHandler.putUser(user);
        boolean actual2 = localStorageHandler.hasUser(user2.getUserName());
        assertFalse(actual2, "Should return false when hasUser() us called on a username not in the file");

        // Test if it returns true on the user that is in the file
        localStorageHandler.putUser(user2);
        boolean actual3 = localStorageHandler.hasUser(user.getUserName());
        assertTrue(actual3, "Should return true when hasUser() is called on a username in the file");

    }

    /**
     * Test if the getUsernames() method works as expected.
     */
    @Test
    public void getUsernamesTest() {
        // Test if it returns an empty Collection when the method is called an an empty
        // file
        boolean actual1 = localStorageHandler.getUsernames().isEmpty();
        assertTrue(actual1, "Should return an empty Collection when the file is empty");

        // Test if it returns the expected collection when the file is not empty
        Collection<String> expected2 = new ArrayList<String>();
        expected2.add(user.getUserName());
        expected2.add(user2.getUserName());
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        Collection<String> actual2 = localStorageHandler.getUsernames();
        assertEquals(expected2, actual2, "getUsernames() did not return expected collection of usernames");

    }

    /**
     * Test the getUser(username) method.
     */
    @Test
    public void getUserTest() {
        // Test if it returns a null User if the username is not there
        User actual1 = localStorageHandler.getUser(user.getUserName());
        boolean shouldBeTrue = actual1 == null;
        assertTrue(shouldBeTrue, "getUser(username) should return null when it does not have that username");

        // Test if pusUser returns the expected user.
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        User actual2 = localStorageHandler.getUser(user2.getUserName());
        assertEquals(user2, actual2, "getUser did not return expected user");

    }

    /**
     * Test the removeUser(username) method.
     */
    @Test
    public void removeUserTest() {
        // Test if the removeUser() actaully removes the correct user
        localStorageHandler.putUser(user);
        localStorageHandler.putUser(user2);
        localStorageHandler.removeUser(user.getUserName());
        userMap.putUser(user2);
        UserMap actual2 = localStorageHandler.getUserMap();
        assertEquals(userMap, actual2, "removeUser did not remove the user");

        // Test if it does not remove a user when the username does not exist in file
        try {
            localStorageHandler.removeUser("Non Existent User");
            fail("Should throw exception when removeUser() is called on a non existent username");
        } catch (Exception e) {
            // Succeeds
        }
        assertEquals(userMap, actual2,
                "Something wrong happened when removeUser() was called on a non exixtent username");
    }

    /**
     * Test notifyUserChanged() method.
     */
    @Test
    public void notifyUserChangedTest() {
        // This is just putUserTest.
    }

    /**
     * Delete temp file after reach test.
     */
    @AfterEach
    public void deleteTempFile() {
        tempFile.delete();
    }

}
