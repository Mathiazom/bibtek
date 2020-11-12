package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Class responsible for reading and writing user to locally stored json files.
 */
public final class StorageHandler implements UserMapHandler {


    private UserMapHandler userMapHandler;

    /**
     * Init with appropriate user map handler.
     */
    public StorageHandler() {

        // Attempt to use remote handler
        try {
            userMapHandler = new RemoteStorageHandler();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // If remote fails, try local storage
        if (userMapHandler == null) {

            try {
                userMapHandler = new LocalStorageHandler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }


    @Override
    public boolean hasUser(final String username) {
        return userMapHandler.hasUser(username);
    }

    @Override
    public Collection<String> getUsernames() {
        return userMapHandler.getUsernames();
    }

    @Override
    public UserMap getUserMap() {
        return userMapHandler.getUserMap();
    }

    @Override
    public User getUser(final String username) {
        return userMapHandler.getUser(username);
    }

    @Override
    public void putUser(final User user) {
        userMapHandler.putUser(user);
    }

    @Override
    public void removeUser(final String username) {
        userMapHandler.removeUser(username);
    }

    @Override
    public void notifyUserChanged(final User user) {
        userMapHandler.notifyUserChanged(user);
    }
}
