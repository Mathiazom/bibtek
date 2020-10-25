package bibtek.core;

import java.time.LocalDate;

/**
 * Describes a readers relation with a Book.
 */
public final class BookEntry {

    private final Book book;

    private final LocalDate dateAcquired;

    private final BookReadingState readingState;

    /**
     *
     * @param book         the Book associated with the BookEntry
     * @param dateAcquired the date the book was acquired
     * @param readingState the reading status of the book
     */
    public BookEntry(final Book book, final LocalDate dateAcquired, final BookReadingState readingState) {
        this.book = book;
        this.dateAcquired = dateAcquired;
        this.readingState = readingState;
    }

    /**
     * @return the Book associated with the BookEntry
     */
    public Book getBook() {
        return book;
    }

    /**
     * @return the date the book was acquired
     */
    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    /**
     * @return the reading status of the book
     */
    public BookReadingState getReadingState() {
        return readingState;
    }

    /**
     * @return readable output for BookEntry and Book combined
     */
    public String toPrintString() {

        return book.toString() + ", " + "acquired " + dateAcquired.toString() + ", " + readingState.toString();

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
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        } else {
            if (this.toString().equals(((BookEntry) obj).toString())) {
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
