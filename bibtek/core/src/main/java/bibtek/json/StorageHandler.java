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

    private LocalStorageHandler localStorageHandler;
    private RemoteStorageHandler remoteStorageHandler;

    /**
     * Init with appropriate user map handler.
     */
    public StorageHandler() {

        try {
            localStorageHandler = new LocalStorageHandler();
            remoteStorageHandler = new RemoteStorageHandler();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean hasUser(final String username) {
        if (remoteStorageHandler.isAvailable()) {
            return remoteStorageHandler.hasUser(username);
        }

        return localStorageHandler.hasUser(username);
    }

    @Override
    public Collection<String> getUsernames() {
        if (remoteStorageHandler.isAvailable()) {
            return remoteStorageHandler.getUsernames();
        }

        return localStorageHandler.getUsernames();
    }

    @Override
    public UserMap getUserMap() {
        if (remoteStorageHandler.isAvailable()) {

            // Save remote usermap to local storage
            UserMap remoteUserMap = remoteStorageHandler.getUserMap();
            localStorageHandler.putUserMap(remoteUserMap);

            return remoteUserMap;
        }

        return localStorageHandler.getUserMap();
    }

    @Override
    public User getUser(final String username) {
        if (remoteStorageHandler.isAvailable()) {
            return remoteStorageHandler.getUser(username);
        }

        return localStorageHandler.getUser(username);
    }

    @Override
    public void putUser(final User user) {
        if (remoteStorageHandler.isAvailable()) {
            remoteStorageHandler.putUser(user);
        }

        localStorageHandler.putUser(user);
    }

    @Override
    public void removeUser(final String username) {
        if (remoteStorageHandler.isAvailable()) {
            remoteStorageHandler.removeUser(username);
        }

        localStorageHandler.removeUser(username);
    }

    @Override
    public void notifyUserChanged(final User user) {
        if (remoteStorageHandler.isAvailable()) {
            remoteStorageHandler.notifyUserChanged(user);
        }

        localStorageHandler.notifyUserChanged(user);
    }
}
