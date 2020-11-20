package bibtek.json;

import bibtek.core.User;

/**
 * Standard for UserMap request handling.
 */
public interface UserMapHandler {

    /**
     * Response status of handled request.
     */
    class Status {

        private final int code;
        private final String message;
        private final boolean isOk;

        Status(final int code, final String message, final boolean isOk) {

            this.code = code;
            this.message = message;
            this.isOk = isOk;

        }

        /**
         * Request response status code.
         *
         * @return code
         */
        public int getCode() {
            return code;
        }

        /**
         * Request was handled without problems.
         *
         * @return true if no problems occurred, false otherwise
         */
        public boolean isOk() {
            return isOk;
        }

        @Override
        public String toString() {
            return code + " " + message;
        }
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
    Status putUser(User user);

    /**
     * Notifies that the User has changed, e.g. Library entries have been edited,
     * added or removed.
     *
     * @param user the User that has changed
     * @return response status code
     */
    Status notifyUserChanged(User user);

    /**
     * Removes the User with the given username from the UserMap.
     *
     * @param username the username of the User to remove
     * @return response status code
     */
    Status removeUser(String username);

}
