package bibtek.json;

import bibtek.core.User;
import bibtek.core.UserMap;

import java.io.IOException;
import java.util.Collection;

/**
 * Standard for UserMap access.
 */
public interface UserMapHandler {

    /**
     * Checks if there (already) exists a User with the provided username.
     *
     * @param username the (new) username
     * @return true if there exists a User with the provided username, false
     *         otherwise
     */
    boolean hasUser(String username);

    /**
     * Gets the usernames of the Users in UserMap.
     *
     * @return the usernames of the Users in UserMap.
     */
    Collection<String> getUsernames() throws IOException;

    /**
     * Gets all the Users currently stored.
     *
     * @return the UserMap with stored Users
     */
    UserMap getUserMap() throws IOException;

    /**
     * Gets the User with the given username.
     *
     * @param username the User's username
     * @return the User with the given username
     */
    User getUser(String username) throws IOException;

    /**
     * Adds a User to the UserMap.
     *
     * @param user the User
     */
    void putUser(User user) throws IOException;

    /**
     * Removes the User with the given username from the UserMap.
     *
     * @param username the username of the User to remove
     */
    void removeUser(String username) throws IOException;

    /**
     * Notifies that the User has changed, e.g. Library entries have been edited,
     * added or removed.
     *
     * @param user the User that has changed
     */
    void notifyUserChanged(User user) throws IOException;

}
