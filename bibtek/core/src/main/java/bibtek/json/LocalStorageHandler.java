package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class responsible for reading and writing users to stored json files.
 */
public final class LocalStorageHandler implements UserMapHandler {

    /**
     * The path where the users file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_PATH = "target/users.json";

    /**
     * The file path where the json data will be stored.
     */
    private Path storagePath;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .setPrettyPrinting().create();

    private UserMap userMap;


    /**
     * @param path the file path where the json data will be stored
     */
    public LocalStorageHandler(final String path) throws IOException {

        setStoragePath(path);

    }

    /**
     * Default constructor of storageHandler, creating a storageHandler with the
     * default storage path.
     *
     * @throws IOException
     */
    public LocalStorageHandler() throws IOException {
        this(LocalStorageHandler.DEFAULT_STORAGE_PATH);
    }

    /**
     * Change the location at which the library entries should be stored. Attempts
     * to create a new file at this location.
     *
     * @param path new storage location
     */
    public void setStoragePath(final String path) throws IOException {

        this.storagePath = Paths.get(path);

        if (!Files.exists(storagePath)) {

            try {
                Files.createFile(storagePath);
            } catch (IOException e) {
                throw new IOException("Exception when creating file");
            }

        }

    }

    @Override
    public UserMap getUserMap() {

        if (userMap == null) {

            try {
                final Reader reader = Files.newBufferedReader(storagePath);
                userMap = gson.fromJson(reader, new TypeToken<UserMap>() {
                }.getType());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();

            }

            if (userMap == null) {

                userMap = new UserMap();

            }

        }

        return userMap;

    }

    private void putUserMap() {

        try {
            final Writer writer = Files.newBufferedWriter(storagePath);
            gson.toJson(userMap, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean hasUser(final String username) {
        return getUserMap().hasUser(username);
    }

    @Override
    public Collection<String> getUsernames() {
        final Collection<String> usernames = new ArrayList<>();
        getUserMap().forEach(user -> usernames.add(user.getUserName()));
        return usernames;
    }

    @Override
    public User getUser(final String username) {

        return getUserMap().getUser(username);

    }

    @Override
    public void putUser(final User user) {

        getUserMap().putUser(user);

        putUserMap();

    }

    @Override
    public void removeUser(final String username) {

        getUserMap().removeUser(getUser(username));

    }

    @Override
    public void notifyUserChanged(final User user) {

        putUser(user);

    }

}
