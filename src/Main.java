import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * BIT1053 - Computer Programming
 * Workout Planning and Tracking System
 * City University Malaysia | MAY 2026
 *
 * Entry point of the application.
 * Launches the GUI on the Event Dispatch Thread (EDT) — required for Swing.
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel for cleaner UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Falls back to default Swing look if system LnF fails
            System.err.println("Look and feel not applied: " + e.getMessage());
        }

        // Launch GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            WorkoutGUI gui = new WorkoutGUI();
            gui.setVisible(true);
        });
    }
}
