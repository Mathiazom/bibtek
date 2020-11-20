package bibtek.json;

import bibtek.core.Book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler responsible for communicating with the Google Books API.
 */
public final class BooksAPIHandler {
    private static final String BOOKS_API_URI_PREFIX = "https://www.googleapis.com/books/v1/volumes?q=";

    private JsonObject fetchBooks(final String searchTerm) {
        final Client client = Client.create();

        final WebResource webResource = client.resource(URI.create(getFetchURI(searchTerm)));

        final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        final String responseString = response.getEntity(String.class);

        final JsonObject responseObject = JsonParser.parseString(responseString).getAsJsonObject();

        return responseObject;
    }

    private String getBookTitle(final JsonObject bookInfo) {
        String bookTitle = "";

        final JsonElement bookTitleElement = bookInfo.get("title");
        if (bookTitleElement != null) {
            bookTitle = bookTitleElement.getAsString();
        }

        return bookTitle;
    }

    private String getBookAuthor(final JsonObject bookInfo) {
        String bookAuthor = "";

        final JsonElement bookAuthorElement = bookInfo.get("authors");
        if (bookAuthorElement != null) {
            bookAuthor = jsonArrayToSimpleString(bookAuthorElement.getAsJsonArray());
        }

        return bookAuthor;
    }

    private String getBookImgPath(final JsonObject bookInfo) {
        String bookImgPath = "";

        try {
            bookImgPath = bookInfo.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
        } catch (NullPointerException ignored) {
            // No thumbnail available for this book
        }
        return bookImgPath;
    }

    private int getBookYearPublished(final JsonObject bookInfo) {
        int bookYearPublished = Book.YEAR_PUBLISHED_MISSING;

        // Check if publishing date is provided in the fetched data
        final JsonElement publishedDateField = bookInfo.get("publishedDate");
        if (publishedDateField != null) {

            final String bookPublishedString = publishedDateField.getAsString();

            // Check if published date one a single integer
            if (bookPublishedString.matches("\\d+")) {
                bookYearPublished = Integer.parseInt(bookPublishedString);
            } else {
                // Attempt to parse date with LocalDate
                try {
                    bookYearPublished = LocalDate.parse(bookPublishedString).getYear();
                } catch (DateTimeParseException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        return bookYearPublished;
    }

    /**
     * Search for book data from Google Books API.
     *
     * @param searchTerm The search term
     * @return A list of books matching the search term, null if no books are found
     */
    public List<Book> searchForBooks(final String searchTerm) {

        JsonObject responseObject = fetchBooks(searchTerm);

        // Make sure there is at least one search result
        if (responseObject.get("totalItems").getAsInt() == 0 || responseObject.get("items") == null) {
            return null;
        }

        List<Book> bookList = new ArrayList<>();

        responseObject.get("items").getAsJsonArray().forEach(bookElement -> {
            final JsonObject bookInfo = (JsonObject) bookElement.getAsJsonObject().get("volumeInfo");
            if (bookInfo == null) {
                // No volume info found, skip
                return;
            }
            String bookTitle = getBookTitle(bookInfo);
            String bookAuthor = getBookAuthor(bookInfo);
            String bookImgPath = getBookImgPath(bookInfo);
            int bookYearPublished = getBookYearPublished(bookInfo);

            bookList.add(new Book(bookTitle, bookAuthor, bookYearPublished, bookImgPath));
        });

        return bookList;

    }

    /**
     * Builds valid URI for fetching book data from Google Books API.
     *
     * @param searchTerm The search term (eg. ISBN, Algorithms to live by)
     * @return URI as string
     */
    public String getFetchURI(final String searchTerm) {
        System.out.println("fetching: " + BOOKS_API_URI_PREFIX + searchTerm.replaceAll(" ", "%20"));
        return BOOKS_API_URI_PREFIX + searchTerm.replaceAll(" ", "%20");

    }

    /**
     * Parses a {@link JsonArray} and creates a comma separated string of elements.
     *
     * @param array Array with elements of type {@link JsonElement}
     * @return string concatenation of {@link JsonElement} strings separated by
     *         comma.
     */
    public String jsonArrayToSimpleString(final JsonArray array) {

        final StringBuilder builder = new StringBuilder();

        builder.append(array.remove(0).getAsString());

        for (final JsonElement author : array) {
            builder.append(", ").append(author.getAsString());
        }

        return builder.toString();

    }

}
