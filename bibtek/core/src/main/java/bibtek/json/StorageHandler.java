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
    public Collection<String> getUsernames() throws IOException {
        try {
            return remoteStorageHandler.getUsernames();
        } catch (Exception e) {
            return localStorageHandler.getUsernames();
        }
    }

    @Override
    public UserMap getUserMap() throws IOException {
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
    public User getUser(final String username) throws IOException {
        try {

            // Save remote user to local storage
            User remoteUser = remoteStorageHandler.getUser(username);
            localStorageHandler.putUser(remoteUser);
            return remoteUser;
        } catch (Exception e) {
            return localStorageHandler.getUser(username);
        }
    }

    @Override
    public void putUser(final User user) throws IOException {
        try {
            remoteStorageHandler.putUser(user);
            localStorageHandler.putUser(user);
        } catch (Exception e) {
            localStorageHandler.putUser(user);
        }
    }

    @Override
    public void removeUser(final String username) throws IOException {
        try {
            remoteStorageHandler.removeUser(username);
            localStorageHandler.removeUser(username);
        } catch (Exception e) {
            localStorageHandler.removeUser(username);
        }
    }

    @Override
    public void notifyUserChanged(final User user) throws IOException {
        this.putUser(user);
    }
}
