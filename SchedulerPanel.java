

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Monthly planner panel (Mon–Sun).
 * - First row: weekday headers
 * - 6x7 grid of JTextAreas for days
 * - loadEvents() appends event text under the day number
 */
public class SchedulerPanel extends JPanel {
    private final JTextArea[][] cells;
    private final String[] weekdayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private final String weekdayNamePrefix = "星期"; 
    private final Calendar monthCal;
    private final Map<Integer, Point> dayToCell;
    private final SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");

    public SchedulerPanel(String csvPath) {
        this.setLayout(new GridLayout(7, 7));
        this.cells = new JTextArea[6][7];
        this.dayToCell = new HashMap<>();

        // Fix calendar to current month
        monthCal = new GregorianCalendar();
        monthCal.set(Calendar.DAY_OF_MONTH, 1);

        // Add weekday labels
        Border blackline = BorderFactory.createLineBorder(Color.black);
        for (String name : weekdayNames) {
            JLabel weekdayLabel = new JLabel(weekdayNamePrefix + name, JLabel.CENTER);
            weekdayLabel.setOpaque(true);
            weekdayLabel.setBackground(new Color(51, 227, 255));
            weekdayLabel.setForeground(Color.RED);
            weekdayLabel.setBorder(blackline);
            this.add(weekdayLabel);
        }

        // Prepare empty cells
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JTextArea aCell = new JTextArea(4, 16);
                aCell.setEditable(false);
                aCell.setLineWrap(true);
                aCell.setWrapStyleWord(true);
                aCell.setBorder(blackline);
                aCell.setText("");
                cells[r][c] = aCell;
                this.add(aCell);
            }
        }

        // Fill in day numbers
        fillDaysForMonth();

        // Load events
        loadEvents(csvPath);
    }

    private void fillDaysForMonth() {
        Calendar cal = (Calendar) monthCal.clone();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int firstDowJava = cal.get(Calendar.DAY_OF_WEEK); // 1=Sunday..7=Saturday
        int firstCol = (firstDowJava + 5) % 7; // shift so Monday=0, Sunday=6

        int row = 0, col = firstCol;

        for (int day = 1; day <= daysInMonth; day++) {
            JTextArea cell = cells[row][col];
            cell.setText(day + "");
            dayToCell.put(day, new Point(row, col));

            col++;
            if (col >= 7) {
                col = 0;
                row++;
            }
        }
    }

    private void loadEvents(String csvPath) {
        try {
            EventFileAccessor accessor = new EventFileAccessor(csvPath);
            Map<Date, String> events = accessor.getEventList();

            int panelYear = monthCal.get(Calendar.YEAR);
            int panelMonth = monthCal.get(Calendar.MONTH);

            for (Map.Entry<Date, String> entry : events.entrySet()) {
                Date d = entry.getKey();
                String text = entry.getValue();

                Calendar c = Calendar.getInstance();
                c.setTime(d);

                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                if (y == panelYear && m == panelMonth) {
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    Point p = dayToCell.get(day);
                    if (p != null) {
                        JTextArea cell = cells[p.x][p.y];
                        String current = cell.getText();
                        cell.setText(current + "\n" + text);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to load events from: " + csvPath);
            ex.printStackTrace();
        }
    }
}
