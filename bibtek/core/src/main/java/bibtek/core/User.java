package bibtek.core;

public class User {

    /**
     * The minimal age the user can be.
     */
    public static final int MINIMAL_AGE = 13;
    /**
     * User attributes.
     */
    private String userName;
    private Library library;
    private int age;

    /**
     * Full constructor for all parameters, making a user with a library.
     *
     * @param userName
     * @param age
     * @param library
     */
    public User(final String userName, final int age, final Library library) {
        this.userName = userName;
        this.age = age;
        this.library = library;
    }

    /**
     * Constructor that keeps the current library (from json file).
     *
     * @param userName
     * @param age
     */
    public User(final String userName, final int age) {
        this(userName, age, new Library());
    }

    /**
     * @return the user name of this user.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *
     * @return user's age
     */
    public int getAge() {
        return this.age;
    }

    /**
     *
     * @return user's library
     */
    public Library getLibrary() {
        return this.library;
    }

    /**
     * Sets user's username.
     *
     * @param name
     */
    public void setUserName(final String name) {
        this.userName = name;
    }

    /**
     * Change the age of the user.
     *
     * @param userAge
     */
    public void setAge(final int userAge) {
        this.age = userAge;
    }

    /**
     * Change the library of the user.
     *
     * @param lib
     */
    public void setLibrary(final Library lib) {
        this.library = lib;
    }

    /**
     * Saves the user's information i a json file.
     */
    public void saveUser() {
    }

}
