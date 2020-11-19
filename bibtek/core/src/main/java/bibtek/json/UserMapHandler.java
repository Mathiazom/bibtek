package bibtek.json;

import bibtek.core.User;

/**
 * Standard for UserMap access.
 *
 * @param <T> status type
 */
public interface UserMapHandler<T> {

    enum Status {

        /**
         * Storage request was handled without problems.
         */
        OK,

        /**
         * Storage did not find request-related resource.
         */
        NOT_FOUND,

        /**
         * An error occurred while processing storage request.
         */
        ERROR

    }

    /**
     * Gets the User with the given username.
     *
     * @param username the User's username
     * @return the User with the given username, or null if the user does not exist
     */
    User getUser(String username);

    /**
     * Adds/updates a User to the UserMap.
     *
     * @param user the User
     * @return response status code
     */
    T putUser(User user);

    /**
     * Notifies that the User has changed, e.g. Library entries have been edited,
     * added or removed.
     *
     * @param user the User that has changed
     * @return response status code
     */
    T notifyUserChanged(User user);

    /**
     * Removes the User with the given username from the UserMap.
     *
     * @param username the username of the User to remove
     * @return response status code
     */
    T removeUser(String username);

}
