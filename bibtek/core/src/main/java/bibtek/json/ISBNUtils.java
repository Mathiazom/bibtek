package bibtek.json;

import java.util.Arrays;
import java.util.List;

public final class ISBNUtils {

    private ISBNUtils() {
        throw new IllegalStateException();
    }

    private static final List<Integer> VALID_ISBN_LENGTHS = Arrays.asList(10, 13);

    /**
     *
     * Check if a given string can represent a valid ISBN and act as a unique Book id.
     *
     * @param isbn string to validate
     * @return true if given string could be a valid ISBN, false otherwise
     */
    public static boolean isValidISBN(final String isbn) {

        final boolean onlyDigits = isbn.matches("\\d*");
        final boolean validLength = VALID_ISBN_LENGTHS.contains(isbn.length());

        return onlyDigits && validLength;

    }

}
