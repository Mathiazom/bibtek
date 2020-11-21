package bibtek.ui;

import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.testfx.api.FxRobot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility class to help test the {@link Toast} control
 */
public class FxTestUtil {

    /**
     * Assert that a {@link Toast} is visible and the state and message equals the given the expected state and message
     * @param expectedMessage to compare the actual message with
     * @param expectedState to compare the actual state with
     * @param parent to search for Toast
     */
    public static void assertToast(final Toast.ToastState expectedState, final String expectedMessage, final Parent parent) {
        assertToastVisible(parent);
        assertToastStateEquals(expectedState, parent);
        assertToastTextEquals(expectedMessage, parent);
    }

    /**
     * Assert that a {@link Toast} is visible as a child of the given parent
     * @param parent to search for Toast
     */
    public static void assertToastVisible(final Parent parent) {
        final Toast toast = (Toast) parent.lookup("Toast");
        assertNotNull(toast);
        assertTrue(toast.isVisible());
    }

    /**
     * Assert that a {@link Toast}'s message equals the given the expected message
     * @param expectedMessage to compare the actual message with
     * @param parent to search for Toast
     */
    public static void assertToastTextEquals(final String expectedMessage, final Parent parent) {
        assertEquals(expectedMessage, ((Toast) parent.lookup("Toast")).toastText.getText());
    }

    /**
     * Assert that a {@link Toast}'s {@link bibtek.ui.Toast.ToastState} equals the given the expected state
     * @param expectedState to compare the actual state with
     * @param parent to search for Toast
     */
    public static void assertToastStateEquals(final Toast.ToastState expectedState, final Parent parent) {
        assertEquals(expectedState, ((Toast) parent.lookup("Toast")).getState());
    }

}
