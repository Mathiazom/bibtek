package bibtek.core;

import java.util.HashSet;
import java.util.Set;

import bibtek.json.StorageHandler;
import bibtek.json.StorageHandler.BookEntryData;
import bibtek.json.StorageHandler.LibraryData;

public class Library {
    private StorageHandler storageHandler = new StorageHandler();
    private Set<BookEntry> BOOK_ENTRIES;

    public Library() {
        this.BOOK_ENTRIES = getLibraryData();
    }

    public Library(LibraryData libraryData) {
        this.BOOK_ENTRIES = convertToLibrary(libraryData.bookEntries);
    }

    public void addBookEntry(BookEntry bookEntry) {
            BOOK_ENTRIES.add(bookEntry);
            storageHandler.saveLibraryData(this);
        }

    public void removeBookEntry(BookEntry bookEntry) {
        BOOK_ENTRIES.remove(bookEntry);
            
        storageHandler.saveLibraryData(this);
    }

    public Set<BookEntry> getBookEntries() {
            return BOOK_ENTRIES;
    }

    public void removeBookEntriesByProperty(String property, String value) {
        // TODO
    }

    private Set<BookEntry> getLibraryData() {
        return storageHandler.fetchLibraryData();

    }

    private Set<BookEntry> convertToLibrary(Set<BookEntryData> bookEntriesData) {
        Set<BookEntry> bookEntries = new HashSet<>();

        bookEntriesData.forEach(bookEntryData -> {
            BookEntry bookEntry = new BookEntry(bookEntryData);
            bookEntries.add(bookEntry);
        });
    
        return bookEntries;
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
