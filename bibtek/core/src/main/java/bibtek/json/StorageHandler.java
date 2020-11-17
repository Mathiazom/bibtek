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
        try {
            return remoteStorageHandler.hasUser(username);
        } catch (Exception e) {
            return localStorageHandler.hasUser(username);
        }
    }

    @Override
    public Collection<String> getUsernames() {
        try {
            return remoteStorageHandler.getUsernames();
        } catch (Exception e) {
            return localStorageHandler.getUsernames();
        }
    }

    @Override
    public UserMap getUserMap() {
        try {

            // Save remote usermap to local storage
            UserMap remoteUserMap = remoteStorageHandler.getUserMap();
            localStorageHandler.putUserMap(remoteUserMap);

            return remoteUserMap;
        } catch (Exception e) {
            return localStorageHandler.getUserMap();
        }
    }

    @Override
    public User getUser(final String username) {
        try {
            return remoteStorageHandler.getUser(username);
        } catch (Exception e) {
            return localStorageHandler.getUser(username);
        }
    }

    @Override
    public void putUser(final User user) {
        try {
            remoteStorageHandler.putUser(user);
        } catch (Exception e) {
            localStorageHandler.putUser(user);
        }
    }

    @Override
    public void removeUser(final String username) {
        try {
            remoteStorageHandler.removeUser(username);
        } catch (Exception e) {
            localStorageHandler.removeUser(username);
        }
    }

    @Override
    public void notifyUserChanged(final User user) {
        try {
            remoteStorageHandler.notifyUserChanged(user);
        } catch (Exception e) {
            localStorageHandler.notifyUserChanged(user);
        }
    }
}
