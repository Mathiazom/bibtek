package bibtek.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bibtek.json.StorageHandler;

/**
 * Class representing a users personal library storing book entries.
 * <p>
 * Persists data continuously in local storage by using a {@link StorageHandler}
 */
public final class Library {

    /**
     * The path where the library.json file should be stored by default.
     */
    private static final String DEFAULT_STORAGE_PATH = "target/library.json";

    private StorageHandler storageHandler;

    /**
     * The collection of all the BookEntries the reader has.
     */
    private Set<BookEntry> bookEntries;

    /**
     * Default constructor for Library. Passes the default storage path to the
     * specific constructor.
     */
    public Library() {

        this(DEFAULT_STORAGE_PATH);

    }

    /**
     * Specific constructor for Library. Sets the path for where the library.json
     * file should be stored.
     *
     * @param storagePath the path where the library.json file should be stored
     */
    public Library(final String storagePath) {

        this.storageHandler = new StorageHandler(storagePath);

        try {
            setBookEntries(storageHandler.fetchBookEntries());
        } catch (IOException e) {
            setBookEntries(new HashSet<>());
            System.out.println(e.getMessage());
        }

    }

    /**
     * @param newBookEntries the collection of all the BookEntries the reader has
     */
    private void setBookEntries(final Set<BookEntry> newBookEntries) {
        this.bookEntries = newBookEntries;
    }

    /**
     * @param bookEntry describes a readers relation with a Book
     */
    public void addBookEntry(final BookEntry bookEntry) {

        if (!isValidBookEntry(bookEntry)) {
            throw new IllegalArgumentException("The book entry has illegal formatting!");
        }

        bookEntries.add(bookEntry);

        try {
            storageHandler.storeBookEntries(bookEntries);
        } catch (IOException e) {
            // Undo add book
            bookEntries.remove(bookEntry);
            System.out.println(e.getMessage());
        }

    }

    /**
     * @param bookEntry describes a readers relation with a Book
     */
    public void removeBookEntry(final BookEntry bookEntry) {

        bookEntries.remove(bookEntry);

        try {
            storageHandler.storeBookEntries(bookEntries);
        } catch (IOException e) {
            bookEntries.add(bookEntry);
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return the BookEntries in the Library
     */
    public Set<BookEntry> getBookEntries() {
        return bookEntries;
    }

    // TODO
    /*
     * public void removeBookEntriesByProperty(String property, String value) {
     *
     * }
     */

    /**
     * @param bookEntry describes a readers relation with a Book
     * @return whether or not the BookEntry is valid
     */
    private boolean isValidBookEntry(final BookEntry bookEntry) {

        return bookEntry != null && bookEntry.getBook() != null && bookEntry.getDateAcquired() != null
                && bookEntry.getReadingState() != null && bookEntry.getBook().getAuthor() != null
                && bookEntry.getBook().getTitle() != null;

    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        if (getBookEntries() == null || getBookEntries().isEmpty()) {
            return "No books in library.";
        }

        sb.append("Book entries: { \n");
        getBookEntries().forEach(bookEntry -> sb.append(bookEntry.toString()).append(",\n"));
        sb.append("}");

        return sb.toString();

    }

    /**
     * Change the location at which the library entries should be stored by the {@link StorageHandler}.
     *
     * @param path new storage location
     */
    public void setStoragePath(final String path) {

        storageHandler.setStoragePath(path);

        // Remove entries from old json path
        bookEntries.clear();

    }

}
