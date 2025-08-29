import javax.swing.*;

/**
 * Entry point for the monthly planner application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame schFrame = new JFrame("月度计划 (Monthly Planner)");
            schFrame.add(new SchedulerPanel("EventsList.csv"));
            schFrame.pack();
            schFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            schFrame.setLocationRelativeTo(null);
            schFrame.setVisible(true);
        });
    }
}

