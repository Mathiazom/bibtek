package bibtek.json;

import bibtek.core.User;

import java.util.Collection;

/**
 *
 * Interface defining the standard for UserMap access.
 *
 */
public interface UserMapHandler {

    /**
     * Checks if there (already) exists a User with the provided username.
     *
     * @param username the (new) username
     * @return true if there exists a User with the provided username, false otherwise
     */
    boolean hasUser(String username);

    /**
     * Gets the usernames of the Users in UserMap.
     *
     * @return the usernames of the Users in UserMap.
     */
    Collection<String> getUsernames();

    /**
     * Gets the User with the given username.
     *
     * @param username the User's username
     * @return the User with the given username
     */
    User getUser(String username);

    /**
     * Adds a User to the UserMap.
     *
     * @param user the User
     */
    void putUser(User user);

    /**
     * Removes the User with the given username from the UserMap.
     *
     * @param username the username of the User to remove
     */
    void removeUser(String username);

    /**
     * Notifies that the User has changed, e.g. Library entries
     * have been edited, added or removed.
     *
     * @param user the User that has changed
     */
    void notifyUserChanged(User user);


}
