package bibtek.core;


/**
 * Describes the information about a book.
 */
public final class Book {

    /** Constant to mark published year as missing from data. */
    public static final int YEAR_PUBLISHED_MISSING = -1;

    private final String title;

    private final String author;

    private final int yearPublished;
    private final String imgPath;

    /**
     * @param title         the book title
     * @param author        the book author
     * @param yearPublished the year the book was published
     * @param imgPath       the book cover image path
     */
    public Book(final String title, final String author, final int yearPublished, final String imgPath) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.imgPath = imgPath;
    }

    public Book(final String title, final String author, final int yearPublished) {
        this(title, author, yearPublished, "");
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
        if (obj.getClass().equals(this.getClass())) {
            return this.toString().equals(((Book) obj).toString());
        }
        return false;

    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public String getImgPath() {
        return imgPath;
    }
}
