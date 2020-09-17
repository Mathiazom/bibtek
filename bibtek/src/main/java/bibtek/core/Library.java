package bibtek.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bibtek.json.StorageHandler;

/**
 *
 * Class representing a users personal library storing book entries
 *
 * Persists data continuously in local storage by using a {@link StorageHandler}
 *
 * Implements the singleton pattern, meaning at most one instance exists at any moment
 *
 */
public class Library {

    private static final String DEFAULT_STORAGE_PATH = "target/library.json";

    private final StorageHandler storageHandler;

    private Set<BookEntry> bookEntries;

    public Library(){

        this(DEFAULT_STORAGE_PATH);

    }

    public Library(final String storagePath) {

        this.storageHandler = new StorageHandler(storagePath);

        try {
            setBookEntries(storageHandler.fetchBookEntries());
        } catch (IOException e) {
            setBookEntries(new HashSet<>());
            e.getMessage();
        }

    }

    private void setBookEntries(Set<BookEntry> bookEntries) {
        this.bookEntries = bookEntries;
    }

    public void addBookEntry(BookEntry bookEntry) {

        if (!isValidBookEntry(bookEntry)) {
            throw new IllegalArgumentException("The book entry has illegal formatting!");
        }

        bookEntries.add(bookEntry);

        try {
            storageHandler.storeBookEntries(bookEntries);
        } catch (IOException e) {
            // Undo add book
            bookEntries.remove(bookEntry);
            e.getMessage();
        }

    }

    public void removeBookEntry(BookEntry bookEntry) {

        bookEntries.remove(bookEntry);

        try {
            storageHandler.storeBookEntries(bookEntries);
        } catch (IOException e) {
            bookEntries.add(bookEntry);
            e.getMessage();
        }

    }

    public Set<BookEntry> getBookEntries() {
        return bookEntries;
    }

    // TODO
    /*public void removeBookEntriesByProperty(String property, String value) {

    }*/

    private boolean isValidBookEntry(BookEntry bookEntry) {

        return
                bookEntry != null &&
                bookEntry.getBook() != null &&
                bookEntry.getDateAcquired() != null &&
                bookEntry.getReadingState() != null &&
                bookEntry.getBook().getAuthor() != null &&
                bookEntry.getBook().getTitle() != null;

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

}
