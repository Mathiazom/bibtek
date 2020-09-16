package bibtek.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookEntry {

    private final Book book;
    private final Date dateAcquired;
    private final BookReadingState readingState;

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

    public String toPrintString(){

        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        return book.toString() + ", " +
                "acquired " + formatter.format(dateAcquired) + ", " +
                readingState.toString();

    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();
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
