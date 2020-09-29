# Persistance layer

The persistance layer utilizes the [gson](https://github.com/google/gson) libary for serialization and deserialization of objects to JSON.
These operations are delegated to the [StorageHandler](StorageHandler.java).

Most of the classes in [*bibtek.core*](../core/) are converted directly, but the help classes [LocalDateSerializer](LocalDateSerializer.java) and [LocalDateDeserializer](LocalDateDeserializer) have been created to convert LocalDate objects.