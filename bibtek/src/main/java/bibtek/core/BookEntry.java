package bibtek.core;

import java.util.Date;

import bibtek.json.StorageHandler.BookEntryData;

public class BookEntry {

    private Book book;
    private Date dateAcquired;
    private BookReadingState readingState;

    public BookEntry(Book book, Date dateAcquired, BookReadingState readingState) {
        this.book = book;
        this.dateAcquired = dateAcquired;
        this.readingState = readingState;
    }

    public BookEntry(BookEntryData bookEntryData) {
        this.book = new Book(bookEntryData.bookData);
        this.dateAcquired = bookEntryData.dateAcquired;
        this.readingState = bookEntryData.readingState;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("bookEntry: { ");
        sb.append("title: ").append(this.getBook().getTitle()).append(", ");
        sb.append("author: ").append(this.getBook().getAuthor()).append(", ");
        sb.append("yearPublished: ").append(this.getBook().getYearPublished()).append(", ");
        sb.append("dateAcquired: ").append(this.getDateAcquired()).append(", ");
        sb.append("readingState: ").append(this.getReadingState().name());
        sb.append(" }");
        return sb.toString();
    }

}
