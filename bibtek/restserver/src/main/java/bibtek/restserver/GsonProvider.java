package bibtek.restserver;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import bibtek.json.LocalDateDeserializer;
import bibtek.json.LocalDateSerializer;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {

    private Gson gson;

    /**
     * @return gson instance with custom adapters
     */
    private Gson getGson() {
        if (gson == null) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            gson = gsonBuilder.disableHtmlEscaping()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();
        }
        return gson;
    }

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType,
                              final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(final Class<Object> type, final Type genericType,
                           final Annotation[] annotations, final MediaType mediaType,
                           final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException {

        try (InputStreamReader streamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            return getGson().fromJson(streamReader, genericType);
        }

    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType,
                               final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(final Object object, final Class<?> type, final Type genericType,
                        final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final Object object, final Class<?> type, final Type genericType,
                        final Annotation[] annotations, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) throws IOException,
            WebApplicationException {

        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8)) {
            getGson().toJson(object, genericType, writer);
        }

    }
}

