package bibtek.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bibtek.core.Book;

public class BooksAPIHandlerTest {

    private BooksAPIHandler booksApiHandler = new BooksAPIHandler();

    /**
     * Testing the fetchBook(String) method.
     */
    @Test
    public void fetchBookTest() {
        // Testing if it returns expected for known isbn
        final String isbn = "9780262033848";
        try {
            final int yearPublished = 2009;
            Book expected = new Book("Introduction to Algorithms", "Thomas H. Cormen", yearPublished);
            Book actual = booksApiHandler.searchForBooks(isbn).get(0);
            assertEquals(expected, actual, "fetchBook() did not return correct book");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Testing if it return null if there is no search result
        final String illegalISBN = "978026203384---------8";
        try {
            Book expected2 = null;
            Book actual2 = booksApiHandler.searchForBooks(illegalISBN).get(0);
            assertEquals(expected2, actual2, "fetchBook() did not return correct book");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
