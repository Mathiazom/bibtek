package bibtek.json;

import bibtek.core.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public final class BooksAPIHandler {

    private static final String BOOKS_API_URI_PREFIX = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public Book fetchBook(final String isbn) throws IOException, InterruptedException {

        final HttpClient client = HttpClient.newHttpClient();

        final HttpRequest request = HttpRequest.newBuilder(URI.create(getURIFromISBN(isbn)))
                .GET()
                .header("accept", "application/json")
                .build();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        final JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        final JsonObject bookItem = responseObject.get("items").getAsJsonArray().get(0).getAsJsonObject();

        final JsonObject volumeInfo = (JsonObject) bookItem.get("volumeInfo");

        final String bookTitle = volumeInfo.get("title").getAsString();
        final String bookAuthor = getAuthorsString(volumeInfo.get("authors").getAsJsonArray());

        int bookPublished = -1;

        if(volumeInfo.get("publishedDate") != null){
            final String bookPublishedString = volumeInfo.get("publishedDate").getAsString();
            bookPublished = LocalDate.parse(bookPublishedString).getYear();
        }

        final String bookImgPath = volumeInfo.get("imageLinks").getAsJsonObject().get("smallThumbnail").getAsString();

        return new Book(bookTitle, bookAuthor, bookPublished, bookImgPath);

    }

    public String getURIFromISBN(final String isbn) {

        return BOOKS_API_URI_PREFIX + isbn;

    }

    public String getAuthorsString(final JsonArray authors){

        final StringBuilder builder = new StringBuilder();

        builder.append(authors.remove(0).getAsString());

        for (final JsonElement author : authors) {
            builder.append(", ").append(author.getAsString());
        }

        return builder.toString();

    }


}
