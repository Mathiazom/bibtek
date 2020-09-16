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
import bibtek.core.Library;

public class StorageHandler {

    public StorageHandler() {
        Path libraryPath = Paths.get("bibtek/target/library.json");
        if (!Files.exists(libraryPath)) {
            System.out.println("No library.json file detected. Creating new file.");
            try {
                Files.createFile(libraryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void saveLibraryData(Library library) {
        try {
            Gson gson = new Gson();
        
            Writer writer = Files.newBufferedWriter(Paths.get("bibtek/target/library.json"));
        
            LibraryData libraryData = new LibraryData(library);
            
            gson.toJson(libraryData, writer);
        
            writer.close();
        
        } catch (Exception ex) {
            System.out.println("saveLibraryException: " + ex.getCause());
            ex.printStackTrace();
        }
    }

    public Set<BookEntry> fetchLibraryData() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("bibtek/target/library.json"));
            LibraryData libraryData = gson.fromJson(reader, LibraryData.class);

            reader.close();
            if (libraryData == null)
                return new HashSet<>();

            Library library = new Library(libraryData);
            return library.getBookEntries();
        
        } catch (Exception ex) {
            System.out.println("fetchLibraryException: " + ex.getCause());
            ex.printStackTrace();
        }
        return null;
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
    }

    public class LibraryData {
        public StorageHandler storageHandler;
        public Set<BookEntryData> bookEntries;

        private LibraryData(Library library) {
            this.bookEntries = convertBookEntryData(library.getBookEntries());
            this.storageHandler = new StorageHandler();
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
