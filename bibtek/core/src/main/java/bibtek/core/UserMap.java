package bibtek.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Container for all User instances in storage.
 *
 */
public final class UserMap implements Iterable<User> {

    private final Map<String, User> users = new LinkedHashMap<>();

    /**
     * Checks if this UserMap already has a User with the provided username.
     *
     * @param username the username to check
     * @return true if a User with the provided name exists, false otherwise
     */
    public boolean hasUser(final String username) {
        return users.containsKey(username);
    }

    /**
     * Removes the User from this UserMap.
     *
     * @param user the User
     * @throws IllegalArgumentException if the user's name is invalid
     */
    public void removeUser(final User user) {
        users.remove(user.getUserName());
    }

    /**
     * Gets the User with the provided username.
     *
     * @param username the username
     * @return the User with the provided username
     */
    public User getUser(final String username) {
        return users.get(username);
    }

    /**
     * Replaces an existing User with the same username, or adds it.
     *
     * @param user the User
     * @return the replaced User, or null
     */
    public User putUser(final User user) {
        return users.put(user.getUserName(), user);
    }

    @Override
    public Iterator<User> iterator() {
        return users.values().iterator();
    }

    /**
     * Equals method of UserMap.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        for (User user : this) {
            if (!((UserMap) obj).hasUser(user.getUserName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
