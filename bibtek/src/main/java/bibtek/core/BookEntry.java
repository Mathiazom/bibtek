package bibtek.core;

import java.util.Date;

public class BookEntry {

    private Book book;
    private Date dateAcquired;
    private BookReadingState readingState;


    public BookEntry(Book book, Date dateAcquired, BookReadingState readingState) {
        this.book = book;
        this.dateAcquired = dateAcquired;
        this.readingState = readingState;
    }

    public Book getBook() {
        return book;
    }

    public Date getDateAcquired() {
        return dateAcquired;
    }

    public BookReadingState getReadingState() {
        return readingState;
    }

}
