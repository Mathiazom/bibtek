package bibtek.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a users personal library storing book entries.
 */
public final class Library {

    private final Set<BookEntry> bookEntries;

    /**
     * Initializing an empty library.
     *
     */
    public Library() {
        bookEntries = new HashSet<>();

    }

    /**
     * @param bookEntry describes a readers relation with a Book
     */
    public void addBookEntry(final BookEntry bookEntry) {
        if (!isValidBookEntry(bookEntry)) {
            throw new IllegalArgumentException("The book entry has illegal formatting!");
        }
        bookEntries.add(bookEntry);
    }

    /**
     * @param bookEntry describes a readers relation with a Book
     */
    public void removeBookEntry(final BookEntry bookEntry) {

        bookEntries.remove(bookEntry);

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

        if (getBookEntries().isEmpty()) {
            return "No books in library.";
        }

        sb.append("bookEntries: { \n");
        getBookEntries().forEach(bookEntry -> sb.append(bookEntry.toString()).append(",\n"));
        sb.append("}");

        return sb.toString();

    }

    /**
     * Equals method for Library.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        return this.bookEntries.equals(((Library) obj).getBookEntries());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
