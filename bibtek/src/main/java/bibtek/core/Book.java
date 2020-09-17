package bibtek.core;

public class Book {

    private final String title;
    private final String author;
    private final int yearPublished;

    public Book(String title, String author, int yearPublished) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    @Override
    public String toString() {
        return title + " (" + getYearPublished() + "), " + getAuthor();
    }

    @Override
    public boolean equals(Object obj) {
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
