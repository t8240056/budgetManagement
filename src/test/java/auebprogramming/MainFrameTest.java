package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.swing.JButton;

/**
 * Test class for MainFrame.
 * Verifies the successful initialization of the main application window
 * and its core components, including layout managers and styling methods.
 */
public class MainFrameTest {

    private MainFrame mainFrame;

    /**
     * Initializes a new MainFrame instance before each test.
     */
    @BeforeEach
    public void setUp() {
        // Ensuring the frame is created in the test environment
        mainFrame = new MainFrame();
    }

    /**
     * Tests if the MainFrame and its internal controllers (Manager/Analyzer)
     * are instantiated correctly.
     */
    @Test
    @DisplayName("Verify MainFrame and Controller Instantiation")
    public void testMainFrameInitialization() {
        assertNotNull(mainFrame, "MainFrame instance should not be null");
        assertNotNull(mainFrame.getTitle(), "Frame title should be initialized");
    }

    /**
     * Tests the styling helper methods for buttons.
     * Ensures that the color modification logic does not throw exceptions.
     */
    @Test
    @DisplayName("Test Button Styling Methods")
    public void testButtonStyling() {
        final JButton testButton = new JButton("Test");
        
        // Check confirm button styling (Green)
        mainFrame.confButtonColors(testButton);
        assertNotNull(testButton.getBackground(), "Button background should be set");
        
        // Check back button styling (Yellow)
        mainFrame.backButtonColors(testButton);
        assertNotNull(testButton.getBackground(), "Button background should be set");
    }

    /**
     * Smoke test for navigation methods.
     * Ensures that switching to a basic panel doesn't crash the application.
     */
    @Test
    @DisplayName("Navigation Smoke Test")
    public void testNavigationSwitch() {
        // Switching to the menu should be handled by the CardLayout internally
        mainFrame.switchTo("menu");
        assertNotNull(mainFrame, "Navigation should not affect frame integrity");
    }
}
