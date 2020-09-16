package bibtek.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bibtek.json.StorageHandler;

public class Library {
    private StorageHandler storageHandler = new StorageHandler();
    private Set<BookEntry> bookEntries;

    public Library() {
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
            storageHandler.saveBookEntries(bookEntries);
        } catch (IOException e) {
            bookEntries.remove(bookEntry);
            e.getMessage();
        }
    }

    public void removeBookEntry(BookEntry bookEntry) {
        bookEntries.remove(bookEntry);
            
        try {
            storageHandler.saveBookEntries(bookEntries);
        } catch (IOException e) {
            bookEntries.add(bookEntry);
            e.getMessage();
        }
    }

    public Set<BookEntry> getBookEntries() {
            return bookEntries;
    }

    public void removeBookEntriesByProperty(String property, String value) {
        // TODO
    }

    private boolean isValidBookEntry(BookEntry bookEntry) {
        return bookEntry != null && bookEntry.getBook() != null && bookEntry.getDateAcquired() != null && bookEntry.getReadingState() != null && bookEntry.getBook().getAuthor() != null && bookEntry.getBook().getTitle() != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getBookEntries() == null || getBookEntries().isEmpty()) {
            return "No books in library.";
        }

        sb.append("Book entries: { \n");
        getBookEntries().forEach(bookEntry -> sb.append(bookEntry.toString()).append(",\n"));
        sb.append("}");

        return sb.toString();
    }

}
