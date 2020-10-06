package bibtek.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bibtek.core.BookEntry;
import com.google.gson.reflect.TypeToken;

/**
 * Class responsible for reading and writing book entries to locally stored json
 * files.
 */
public final class StorageHandler {

    /**
     * The file path where the json data will be stored.
     */
    private Path storagePath;

    /**
     * @param path the file path where the json data will be stored
     */
    public StorageHandler(final String path) throws IOException {

        setStoragePath(path);

    }

    /**
     * Change the location at which the library entries should be stored. Attempts
     * to create a new file at this location.
     *
     * @param path new storage location
     */
    public void setStoragePath(final String path) throws IOException {

        this.storagePath = Paths.get(path);

        if (!Files.exists(storagePath)) {

            try {
                Files.createFile(storagePath);
            } catch (IOException e) {
                throw new IOException("Exception when creating file");
            }

        }

    }

    /**
     * @param bookEntries the collection of all the BookEntries the reader has
     * @throws IOException if the StorageHandler fails to store the book entries
     */
    public void storeBookEntries(final Set<BookEntry> bookEntries) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        final Gson gson = gsonBuilder.setPrettyPrinting().create();

        final Writer writer = Files.newBufferedWriter(storagePath);
        gson.toJson(bookEntries, writer);
        writer.close();

    }

    /**
     * @return the collection of books stored locally
     * @throws IOException if the StorageHandler fails to read the book entries
     */
    public Set<BookEntry> fetchBookEntries() throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        final Gson gson = gsonBuilder.setPrettyPrinting().create();
        final Reader reader = Files.newBufferedReader(storagePath);
        final Set<BookEntry> bookEntries = gson.fromJson(reader, new TypeToken<Set<BookEntry>>() {
        }.getType());
        reader.close();

        return bookEntries == null ? new HashSet<>() : bookEntries;

    }

}
