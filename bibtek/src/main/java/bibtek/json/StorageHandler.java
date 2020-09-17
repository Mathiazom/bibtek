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
 *
 * Converts book entries to json format, which is then stored as a file in local
 * storage
 *
 */
public final class StorageHandler {

    private final Path storagePath;

    public StorageHandler(final String path) {

        this.storagePath = Paths.get(path);

        if (!Files.exists(storagePath)) {

            try {
                Files.createFile(storagePath);
            } catch (IOException e) {
                System.err.println("Exception when creating library.json: " + e.getCause());
                e.printStackTrace();
            }

        }

    }

    public void storeBookEntries(final Set<BookEntry> bookEntries) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        final Gson gson = gsonBuilder.setPrettyPrinting().create();

        final Writer writer = Files.newBufferedWriter(storagePath);
        gson.toJson(bookEntries, writer);
        writer.close();

    }

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
