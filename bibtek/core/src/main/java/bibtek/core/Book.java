package bibtek.core;

import com.google.gson.annotations.Expose;

/**
 * Describes the information about a book.
 */
public final class Book {

    @Expose(serialize = true, deserialize = true)
    private final String title;

    @Expose(serialize = true, deserialize = true)
    private final String author;

    @Expose(serialize = true, deserialize = true)
    private final int yearPublished;

    /**
     * @param title         the book title
     * @param author        the book author
     * @param yearPublished the year the book was published
     */
    public Book(final String title, final String author, final int yearPublished) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    /**
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the book authur
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the year the book was published
     */
    public int getYearPublished() {
        return yearPublished;
    }

    @Override
    public String toString() {
        return title + " (" + getYearPublished() + "), " + getAuthor();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        } else {
            if (this.toString().equals(((Book) obj).toString())) {
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
