package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Class responsible for reading and writing users to stored json files.
 * File names are of the form '{username}.json'
 * example:
 * - dante.json
 * - heinrich.json
 * - sigmund.json
 */
public final class LocalStorageHandler implements UserMapHandler {

    /**
     * Local storage request has been handled without problems.
     */
    public static final Status OK = new Status(1, "OK", true);

    /**
     * Could not find requested resource in local storage.
     */
    public static final Status NOT_FOUND = new Status(2, "Not Found", false);

    /**
     * An error occurred while handling local storage request.
     */
    public static final Status ERROR = new Status(0, "Error", false);


    /**
     * The path of bibtek.
     */
    private static final File TARGET_PATH = Paths.get("target").toAbsolutePath().toFile();

    /**
     * The path where the users file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_DIRECTORY = new File(TARGET_PATH, "users").toString();

    /**
     * The file path where the json data will be stored.
     */
    private File storageDirectory;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).setPrettyPrinting().create();

    /**
     * @param directory the file path where the json data will be stored
     */
    public LocalStorageHandler(final String directory) throws IOException {

        setStorageDirectory(directory);

    }

    /**
     * Default constructor of storageHandler, creating a storageHandler with the
     * default storage directory.
     */
    public LocalStorageHandler() throws IOException {
        this(DEFAULT_STORAGE_DIRECTORY);
    }

    /**
     * Change the location at which the users should be stored.
     * Attempts to create a new directory if does not exist.
     *
     * @param directory new storage directory
     * @throws IOException if path is not a directory,
     *                     or directory does not exist and cannot be created.
     */
    public void setStorageDirectory(final String directory) throws IOException {

        this.storageDirectory = new File(directory);
        if (this.storageDirectory.isFile()) {
            throw new IOException("Not a directory");
        }
        if (!this.storageDirectory.exists()) {
            if (!this.storageDirectory.mkdirs()) {
                throw new IOException("Unable to make directory");
            }
        }

    }

    /**
     * Get File of user with given username.
     *
     * @param username of user
     * @return user file
     */
    private File getUserFile(final String username) {

        return new File(this.storageDirectory, username + ".json");

    }

    /**
     * Method that finds all users in the given directory and returns them in a UserMap.
     *
     * @return A UserMap with all users in directory, or null if failed.
     */
    public UserMap getUserMap() {

        final UserMap userMap = new UserMap();
        // Setup directory and its files
        final File[] directoryListing = this.storageDirectory.listFiles();
        if (directoryListing == null) {
            return null;
        }

        // Get all users in directory
        for (File file : directoryListing) {

            final User user = readUserFromFile(file);

            userMap.putUser(user);

        }

        return userMap;

    }

    /**
     * Stores a usermap to local storage.
     *
     * @param userMap to store
     */
    public void putUserMap(final UserMap userMap) {
        for (final User user : userMap) {
            putUser(user);
        }
    }

    /**
     * Read user from file.
     *
     * @param file to read
     * @return user instance, or null if fails.
     */
    private User readUserFromFile(final File file) {

        try (Reader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, new TypeToken<User>() {
            }.getType());
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * Retrieve user with given username from files.
     *
     * @param username the User's username
     * @return user
     */
    @Override
    public User getUser(final String username) {

        return readUserFromFile(getUserFile(username));

    }

    /**
     * Store user to file '{username}.json'.
     *
     * @param user the User
     * @return status
     */
    @Override
    public Status putUser(final User user) {

        final File file = getUserFile(user.getUserName());
        try (Writer writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            gson.toJson(user, writer);

        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
        return OK;

    }

    /**
     * Delete file related to user of given username.
     *
     * @param username of the User to remove
     * @return response status
     */
    @Override
    public Status removeUser(final String username) {

        final File userFile = getUserFile(username);

        if (!userFile.exists()) {
            return NOT_FOUND;
        }

        return userFile.delete() ? OK : ERROR;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status notifyUserChanged(final User user) {
        return putUser(user);
    }

}
