package bibtek.core;

import bibtek.json.StorageHandler.BookData;

public class Book {

    private String title;
    private String author;
    private int yearPublished;

    public Book(String title, String author, int yearPublished) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    public Book(BookData bookData) {
        this.title = bookData.title;
        this.author = bookData.author;
        this.yearPublished = bookData.yearPublished;
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

}
