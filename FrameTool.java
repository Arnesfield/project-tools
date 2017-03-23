package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * Do frame-related events with this class tool.
 * @author Arnesfield
 */
public final class FrameTool {
    
    private FrameTool() {}
    
    /**
     * Opens a JFrame.
     * @param open the JFrame to be opened
     */
    public static final void open(JFrame open) {
        open.setLocationRelativeTo(null);
        open.setVisible(true);
    }
    
    /**
     * Closes a JFrame.
     * @param close the JFrame to be closed
     */
    public static final void close(JFrame close) {
        close.dispose();
    }
    
    /**
     * Opens a JFrame and closes the current JFrame.
     * @param open the JFrame to be opened
     * @param close the current JFrame (usually using this keyword)
     */
    public static final void open(JFrame open, JFrame close) {
        open(open);
        close(close);
    }
    
    /**
     * Displays a popup.
     * @param parent the current JFrame or parent frame
     * @param popup the JPanel object that implements the PopupTool
     * @param title the title of the popup JDialog
     */
    public static final void popup(JFrame parent, JPanel popup, String title) {
        final JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        
        // action listener : disposes JDialog
        for (JButton btn : ((PopupTool)popup).getDisposeButtons()) {
            try {
                btn.addActionListener((ActionEvent e) -> {
                    dialog.dispose();
                });
            } catch (Exception e) {}
        }

        // action listener : disposes JDialog and parent frame
        if ( ((PopupTool)popup).getDisposeButtonsRemoveParent() != null ) {
            for (JButton btn : ((PopupTool)popup).getDisposeButtonsRemoveParent()) {
                try {
                    btn.addActionListener((ActionEvent e) -> {
                        dialog.dispose();
                        // remove parent
                        try {
                            parent.dispose();
                        } catch (Exception ex) {}
                    });
                } catch (Exception e) {}
            }
        }
        
        // add popup JPanel to JDialog
        dialog.setContentPane(popup);
        
        // properties of JDialog
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    /**
     * Overrides window closing event.
     * @param close the JFrame to be closed (usually this)
     * @param open the JFrame to be opened
     */
    public static final void onCloseOf(JFrame close, JFrame open) {
        close.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        close.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                open(open);
            }
        });
    }
    
    /**
     * Overrides window closing event.
     * @param close the JFrame to be closed (usually this)
     * @param open the JFrame to be opened
     * @param popup the popup JPanel to be shown after close
     * @param title the title of the popup
     */
    public static final void onCloseOf(JFrame close, JFrame open, JPanel popup, String title) {
        close.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        close.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close.dispose();
                open(open);
                popup(null, popup, title);
            }
        });
    }
    
    /**
     * Displays a JOptionPane error message.
     * @param message message to be appended to the default messaage
     * @param title title of JOptionPane
     */
    public static final void error(String message, String title) {
        JOptionPane.showMessageDialog(null, "An error occured. " + message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Creates a date box instance.
     * @param month combo box of month
     * @param day combo box of day
     * @param year combo box of year
     * @return DateBox object
     */
    public static final DateBox createDateBox(JComboBox month, JComboBox day, JComboBox year) {
        return new DateBox(month, day, year);
    }
    
    /**
     * DateBox class to manipulate combo boxes used for dates.
     */
    public static final class DateBox {
        private String displayMonth = "Month";
        private String displayDay = "Day";
        private String displayYear = "Year";
        private boolean wordedMonth = false;
        private int yearStart = 0;
        private int yearEnd = 0;
        private final JComboBox month;
        private final JComboBox day;
        private final JComboBox year;
        private final DateBoxListener dateBoxListener;
        
        private final class DateBoxListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupAction();
            }
        }

        /**
         * DateBox class to manipulate combo boxes used for dates.
         * Remember to setProperties() of this object.
         * @param month combo box of month
         * @param day combo box of day
         * @param year combo box of year
         */
        private DateBox(JComboBox month, JComboBox day, JComboBox year) {
            this.month = month;
            this.day = day;
            this.year = year;
            this.dateBoxListener = new DateBoxListener();
            
            setBoxDate();
        }
        
        /**
         * Overwrite default properties of the combo box date group.
         * @param wordedMonth is true if months are words; false if digits
         * @param yearStart year now - this
         * @param yearEnd year now + this
         */
        public final void setProperties(boolean wordedMonth, int yearStart, int yearEnd) {
            this.wordedMonth = wordedMonth;
            this.yearStart = yearStart;
            this.yearEnd = yearEnd;
            
            setBoxDate();
        }
        
        /**
         * Overwrite default properties of the combo box date group.
         * @param month first selected item for the month combo box
         * @param day first selected item for the day combo box
         * @param year first selected item for the year combo box
         * @param wordedMonth is true if months are words; false if digits
         * @param yearStart year now - this
         * @param yearEnd year now + this
         */
        public final void setProperties(String month, String day, String year, boolean wordedMonth, int yearStart, int yearEnd) {
            this.displayMonth = month;
            this.displayDay = day;
            this.displayYear = year;
            this.wordedMonth = wordedMonth;
            this.yearStart = yearStart;
            this.yearEnd = yearEnd;
            
            setBoxDate();
        }
        
        /**
         * Generates number for the day combo box.
         * @param size size of numbers to be generated.
         */
        private void generateDays(int size) {
            day.removeAllItems();
            day.addItem(displayDay);
            for (int i = 1; i <= size; i++) day.addItem(i);
        }
        
        /**
         * Sets combo boxes as dates.
         */
        private void setBoxDate() {
            month.removeActionListener(dateBoxListener);
            year.removeActionListener(dateBoxListener);
            
            month.removeAllItems();
            day.removeAllItems();
            year.removeAllItems();
            
            month.addItem(displayMonth);
            day.addItem(displayDay);
            year.addItem(displayYear);

            month.addActionListener(dateBoxListener);
            year.addActionListener(dateBoxListener);
            
            // add months
            if (wordedMonth)
                for (String m : new DateFormatSymbols().getMonths()) {
                    if (!m.isEmpty()) month.addItem(m);
                }
            else
                for (int i = 1; i <= 12; i++)
                    month.addItem( String.valueOf(i) );
            
            // add days
            for (int i = 1; i <= 31; i++)
                day.addItem( String.valueOf(i) );
            
            // add years
            int yr = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = yearStart; i <= yearEnd; i++)
                year.addItem( String.valueOf(yr+i) );
        }
        
        /**
         * Action performed by the date combo box group.
         */
        private void groupAction() {
            // get selected day
            int selectedDay = day.getSelectedIndex();
            
            int yearNow = Calendar.getInstance().get(Calendar.YEAR);
            try {
                yearNow = Integer.parseInt(String.valueOf( year.getSelectedItem() ));
            } catch (NumberFormatException e) {}

            // check if leap year
            boolean leap = yearNow % 4 == 0;

            if (month.getSelectedIndex() == 0)
                generateDays(31);
            else if (month.getSelectedIndex() <= 7) {
                // set feb default
                if (month.getSelectedIndex() == 2)
                    generateDays( (leap) ? 29 : 28 );
                else
                    generateDays( (month.getSelectedIndex() % 2 == 0) ? 30 : 31 );
            }
            else generateDays( (month.getSelectedIndex() % 2 == 0) ? 31 : 30 );

            day.setSelectedIndex( (selectedDay > day.getItemCount()-1) ? day.getItemCount()-1 : selectedDay );
        }
    }
    
}
