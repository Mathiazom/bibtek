package bibtek.json;

import bibtek.core.User;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Class responsible for reading and writing user to locally stored json files.
 */
public final class StorageHandler implements UserMapHandler {

    /**
     * Handler for locally stored data.
     */
    private LocalStorageHandler localStorageHandler;

    /**
     * Handler for remotely stored data (i.e. on server).
     */
    private RemoteStorageHandler remoteStorageHandler;

    /**
     * Initialize with appropriate user map handler(s).
     */
    public StorageHandler() {

        try {
            remoteStorageHandler = new RemoteStorageHandler();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            localStorageHandler = new LocalStorageHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Get user from server, or from local storage if server is not available.
     * If server is available, the retrieved data will overwrite local storage.
     *
     * @param username the User's username
     * @return user
     */
    @Override
    public User getUser(final String username) {
        try {
            final User remoteUser = remoteStorageHandler.getUser(username);
            if (remoteUser != null) {
                // Save remote user to local storage
                localStorageHandler.putUser(remoteUser);
                return remoteUser;
            }
            return null;
        } catch (Exception e) {
            return localStorageHandler.getUser(username);
        }
    }

    /**
     * Store user on server (if available) and locally.
     *
     * @param user the User
     * @return status
     */
    @Override
    public Status putUser(final User user) {
        try {
            final Status remoteStatus = remoteStorageHandler.putUser(user);
            if (!remoteStatus.isOk()) {
                return remoteStatus;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localStorageHandler.putUser(user);
    }

    /**
     * @see #putUser(User)
     */
    @Override
    public Status notifyUserChanged(final User user) {
        return this.putUser(user);
    }

    /**
     * Delete user from server (if available) and locally.
     *
     * @param username of user to be deleted
     * @return status
     */
    @Override
    public Status removeUser(final String username) {
        try {
            final Status remoteStatus = remoteStorageHandler.removeUser(username);
            if (remoteStatus == RemoteStorageHandler.NOT_FOUND) {
                return remoteStatus;
            }
        } catch (Exception ignored) {
            // Catch exception for server not running
        }

        return localStorageHandler.removeUser(username);
    }

}
