package    auebprogramming;

import javax.swing.JOptionPane;

/**
 * Custom application exception class.
 * Provides a static helper method for displaying Swing error dialogs.
 */
public class AppException extends Exception {
    /**
     * Creates a new AppException with the specified detail message.
     *
     * @param message the detail message to be associated with this exception
     */
    public AppException(final String message) {
        super(message);
    }

    /**
     * Displays an error dialog using JOptionPane.
     * Intended for use when catching application-level exceptions.
     *
     * @param message the error message to display
     */
    public static void showError(final String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Σφάλμα",
            JOptionPane.ERROR_MESSAGE
        );
    }

}
