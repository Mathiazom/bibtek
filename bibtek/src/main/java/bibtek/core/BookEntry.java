package bibtek.core;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class BookEntry {

    private final Book book;
    private final LocalDate dateAcquired;
    private final BookReadingState readingState;

    public BookEntry(Book book, LocalDate dateAcquired, BookReadingState readingState) {
        this.book = book;
        this.dateAcquired = dateAcquired;
        this.readingState = readingState;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public BookReadingState getReadingState() {
        return readingState;
    }

    public String toPrintString(){

        return book.toString() + ", " +
                "acquired " + dateAcquired.toString() + ", " +
                readingState.toString();

    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();
        sb.append("bookEntry: { ");
        sb.append("title: ").append(this.getBook().getTitle()).append(", ");
        sb.append("author: ").append(this.getBook().getAuthor()).append(", ");
        sb.append("yearPublished: ").append(this.getBook().getYearPublished()).append(", ");
        sb.append("dateAcquired: ").append(this.getDateAcquired().toString()).append(", ");
        sb.append("readingState: ").append(this.getReadingState().name());
        sb.append(" }");
        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj.getClass().equals(this.getClass()))){
            return false;
        }
        else{
            if(this.toString().equals(((BookEntry) obj).toString())){
                return true;
            }
            return false;

        }
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }


    

}
