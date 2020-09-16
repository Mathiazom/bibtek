package bibtek.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import bibtek.core.Book;
import bibtek.core.BookEntry;
import bibtek.core.BookReadingState;

public final class StorageHandler {
    private static final String PATH_TO_LOCAL_STORAGE = "bibtek/target/library.json";

    public StorageHandler() {
        Path libraryPath = Paths.get(PATH_TO_LOCAL_STORAGE);
        if (!Files.exists(libraryPath)) {
            try {
                Files.createFile(libraryPath);
            } catch (IOException e) {
                System.err.println("Exception when creating library.json: " + e.getCause());
                e.printStackTrace();
            }
        }
    }
    
    public void saveBookEntries(Set<BookEntry> bookEntries) throws IOException {
        Gson gson = new Gson();
        Writer writer = Files.newBufferedWriter(Paths.get(PATH_TO_LOCAL_STORAGE));
        LibraryData libraryData = new LibraryData(bookEntries);
        gson.toJson(libraryData, writer);
        writer.close();
    }

    public Set<BookEntry> fetchBookEntries() throws IOException {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(PATH_TO_LOCAL_STORAGE));
            LibraryData libraryData = gson.fromJson(reader, LibraryData.class);
            reader.close();
            
            if (libraryData == null)
                return new HashSet<>();

            return libraryData.getBookEntries();
    }

    public class BookData {
        public String title;
        public String author;
        public int yearPublished;

        private BookData(Book book) {
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.yearPublished = book.getYearPublished();
        }

        private Book toBook() {
            return new Book(this.title, this.author, this.yearPublished);
        }
    }
    public class BookEntryData {
        public BookData bookData;
        public Date dateAcquired;
        public BookReadingState readingState;

        private BookEntryData(BookEntry bookEntry) {
            this.bookData = new BookData(bookEntry.getBook());
            this.dateAcquired = bookEntry.getDateAcquired();
            this.readingState = bookEntry.getReadingState();
        }

        private BookEntry toBookEntry() {
            return new BookEntry(this.bookData.toBook(), this.dateAcquired, this.readingState);
        }
    }
    public class LibraryData {
        public Set<BookEntryData> bookEntriesData;

        private LibraryData(Set<BookEntry> bookEntries) {
            this.bookEntriesData = convertBookEntryData(bookEntries);
        }

        private Set<BookEntry> getBookEntries() {
            Set<BookEntry> bookEntries = new HashSet<>();

            this.bookEntriesData.forEach(bookEntryData -> {
                bookEntries.add(bookEntryData.toBookEntry());
            });
            return bookEntries;
        }
    }

    private Set<BookEntryData> convertBookEntryData(Set<BookEntry> bookEntries) {
        Set<BookEntryData> bookEntriesData = new HashSet<>();

        bookEntries.forEach(bookEntry -> {
            BookEntryData bookEntryData = new BookEntryData(bookEntry);
            bookEntriesData.add(bookEntryData);
        });

        return bookEntriesData;
    }
}
