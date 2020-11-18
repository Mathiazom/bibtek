package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class responsible for reading and writing users to stored json files.
 */
public final class LocalStorageHandler implements UserMapHandler {

    /**
     * The path of bibtek.
     */
    private static final File TARGET_PATH = Paths.get("target").toAbsolutePath().toFile();
    /**
     * The path where the users file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_DIRECTRY = new File(TARGET_PATH, "users").toString();

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
     *
     * @throws IOException
     */
    public LocalStorageHandler() throws IOException {
        this(LocalStorageHandler.DEFAULT_STORAGE_DIRECTRY);
    }

    /**
     * Change the location at which the library entries should be stored. Attempts
     * to create a new file at this location.
     *
     * @param directory new storage directory
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
     * Method that finds all users in the given directory and returns a UserMap with
     * them.
     *
     * @return A UserMap with all users in directory
     */
    // @Override
    public UserMap getUserMap() throws IOException {

        UserMap userMap = new UserMap();
        // Setup directory and its files
        File[] directoryListing = this.storageDirectory.listFiles();
        if (directoryListing == null) {
            throw new IOException("No such directory");
        }

        // Loop through all files in directory
        for (File file : directoryListing) {
            try (Reader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

                User user = gson.fromJson(reader, new TypeToken<User>() {
                }.getType());

                userMap.putUser(user);

            } catch (IOException e) {
                throw new IOException(e);
            }
        }

        return userMap;

    }

    /**
     * Saves a usermap to local storage.
     *
     * @param userMap holding all users
     */
    public void putUserMap(final UserMap userMap) throws IOException {
        for (User user : userMap) {
            File file = new File(this.storageDirectory, user.getUserName() + ".json");
            try (Writer writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

                gson.toJson(user, writer);

            } catch (IOException e) {
                throw new IOException(e);
            }

        }
    }

    @Override
    public boolean hasUser(final String username) {
        try {
            return getUserMap().hasUser(username);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<String> getUsernames() throws IOException {
        final Collection<String> usernames = new HashSet<String>();
        getUserMap().forEach(user -> usernames.add(user.getUserName()));
        return usernames;
    }

    @Override
    public User getUser(final String username) throws IOException {
        File file = new File(this.storageDirectory, username + ".json");
        try (Reader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, new TypeToken<User>() {
            }.getType());

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new IOException(e);
        }

    }

    @Override
    public void putUser(final User user) throws IOException {
        File file = new File(this.storageDirectory, user.getUserName() + ".json");
        try (Writer writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            gson.toJson(user, writer);

        } catch (IOException e) {
            throw new IOException(e);
        }

    }

    @Override
    public void removeUser(final String username) throws IOException {
        File userFile = new File(this.storageDirectory, username + ".json");

        if (!userFile.delete()) {
            throw new IOException("Unable to delete user");
        }

    }

    @Override
    public void notifyUserChanged(final User user) throws IOException {

        putUser(user);

    }

}
