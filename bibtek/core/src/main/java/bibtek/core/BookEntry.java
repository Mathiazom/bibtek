package bibtek.core;

import java.time.LocalDate;

/**
 * Describes a readers relation with a Book.
 */
public final class BookEntry {

    private Book book;

    private LocalDate dateAcquired;

    private BookReadingState readingState;

    /**
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
     * Create shallow copy of given book entry.
     *
     * @param bookEntry to copy
     */
    public BookEntry(final BookEntry bookEntry) {
        this(bookEntry.book, bookEntry.getDateAcquired(), bookEntry.getReadingState());
    }

    /**
     * @return the Book associated with the BookEntry
     */
    public Book getBook() {
        return book;
    }

    /**
     * set the Book to be associated with the BookEntry.
     *
     * @param book
     */
    public void setBook(final Book book) {
        this.book = book;
    }

    /**
     * @return the date the book was acquired
     */
    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    /**
     * set the date the book was acquired.
     *
     * @param dateAcquired
     */
    public void setDateAcquired(final LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    /**
     * @return the reading status of the book
     */
    public BookReadingState getReadingState() {
        return readingState;
    }

    /**
     * set the reading state.
     *
     * @param readingState
     */
    public void setReadingState(final BookReadingState readingState) {
        this.readingState = readingState;
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
        if (obj.getClass().equals(this.getClass())) {
            return this.toString().equals(((BookEntry) obj).toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
