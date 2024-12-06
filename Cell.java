/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231082 - Naufal Zaky Nugraha
 * 2 - 5026231035 - Aldani Prasetyo
 * 3 - 5026231183 - Asrid Meilendra
 */

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
/**
 * The Cell class model the cells of the Sudoku puzzle, by customizing (subclass)
 * the javax.swing.JTextField to include row/column, puzzle number and status.
 */
public class Cell extends JTextField {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for JTextField's colors and fonts
    //  to be chosen based on CellStatus
    public static final Color BG_GIVEN = new Color(240, 240, 240); // RGB
    public static final Color BG_GIVEN_DARK = new Color(80, 80, 80);
    public static final Color FG_GIVEN = Color.BLACK;
    public static final Color FG_GIVEN_DARK = Color.WHITE;
    public static final Color FG_NOT_GIVEN = Color.GRAY;
    public static final Color FG_NOT_GIVEN_DARK = new Color(200, 200, 200);
    public static final Color BG_TO_GUESS  = Color.YELLOW;
    public static final Color BG_TO_GUESS_DARK = new Color(102, 102, 0);
    public static final Color BG_CORRECT_GUESS = new Color(0, 216, 0);
    public static final Color BG_CORRECT_GUESS_DARK = new Color(0, 102, 0);
    public static final Color BG_WRONG_GUESS = new Color(216, 0, 0);
    public static final Color BG_WRONG_GUESS_DARK = new Color(102, 0, 0);
    public static final Color BG_DUPLICATE = new Color(255, 165, 0);
    public static final Color BG_DUPLICATE_DARK = new Color(153, 76, 0);
    public static final Font FONT_NUMBERS = new Font("OCR A Extended", Font.PLAIN, 28);
    public static final Font FONT_NOTES = new Font("OCR A Extended", Font.PLAIN, 12);

    // Define properties (package-visible)
    /** The row and column number [0-8] of this cell */
    int row, col;
    /** The puzzle number [1-9] for this cell */
    int number;
    /** The status of this cell defined in enum CellStatus */
    CellStatus status;
    /** Track dark mode state */
    private boolean isDarkMode = false;
    /** Store notes for the cell */
    private boolean[] notes;

    /** Constructor */
    public Cell(int row, int col) {
        super();   // JTextField
        this.row = row;
        this.col = col;
        // Initialize notes array
        this.notes = new boolean[9];
        // Inherited from JTextField: Beautify all the cells once for all
        super.setHorizontalAlignment(JTextField.CENTER);
        super.setFont(FONT_NUMBERS);
    }

    /** Reset this cell for a new game, given the puzzle number and isGiven */
    public void newGame(int number, boolean isGiven) {
        this.number = number;
        status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
        // Clear notes when starting new game
        clearNotes();
        paint();    // paint itself
    }

    /** Clear all notes in the cell */
    public void clearNotes() {
        for (int i = 0; i < 9; i++) {
            notes[i] = false;
        }
        repaint();
    }

    /** Toggle a note number */
    public void toggleNote(int noteNumber) {
        if (noteNumber >= 1 && noteNumber <= 9 && status == CellStatus.TO_GUESS) {
            notes[noteNumber - 1] = !notes[noteNumber - 1];
            repaint();
        }
    }

    /** This Cell (JTextField) paints itself based on its status */
    public void paint() {
        if (status == CellStatus.GIVEN) {
            // Inherited from JTextField: Set display properties
            super.setText(number + "");
            super.setEditable(false);
            super.setBackground(isDarkMode ? BG_GIVEN_DARK : BG_GIVEN);
            super.setForeground(isDarkMode ? FG_GIVEN_DARK : FG_GIVEN);
        } else if (status == CellStatus.TO_GUESS) {
            // Inherited from JTextField: Set display properties
            super.setText("");
            super.setEditable(true);
            super.setBackground(isDarkMode ? BG_TO_GUESS_DARK : BG_TO_GUESS);
            super.setForeground(isDarkMode ? FG_NOT_GIVEN_DARK : FG_NOT_GIVEN);
        } else if (status == CellStatus.CORRECT_GUESS) {  // from TO_GUESS
            super.setBackground(isDarkMode ? BG_CORRECT_GUESS_DARK : BG_CORRECT_GUESS);
        } else if (status == CellStatus.WRONG_GUESS) {    // from TO_GUESS
            super.setBackground(isDarkMode ? BG_WRONG_GUESS_DARK : BG_WRONG_GUESS);
        }
    }

    public void setDuplicate(boolean isDuplicate) {
        if (isDuplicate && !getText().isEmpty()) {
            super.setBackground(isDarkMode ? BG_DUPLICATE_DARK : BG_DUPLICATE);
        } else {
            paint(); // Reset to original color
        }
    }

    /**
     * Update the theme colors based on dark mode state
     * @param darkMode true for dark mode, false for light mode
     */
    public void updateTheme(boolean darkMode) {
        this.isDarkMode = darkMode;
        paint();
    }

    /** Override paintComponent to draw notes */
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (status == CellStatus.TO_GUESS && getText().isEmpty()) {
            g.setFont(FONT_NOTES);
            g.setColor(isDarkMode ? FG_NOT_GIVEN_DARK : FG_NOT_GIVEN);

            int cellWidth = getWidth();
            int cellHeight = getHeight();

            // Calculate sizes for the 3x3 grid of notes
            int noteWidth = cellWidth / 3;
            int noteHeight = cellHeight / 3;

            // Draw each note number if it exists
            for (int i = 0; i < 9; i++) {
                if (notes[i]) {
                    // Calculate position in the 3x3 grid
                    int row = i / 3;  // 0, 1, or 2 for the row
                    int col = i % 3;  // 0, 1, or 2 for the column

                    // Calculate x and y coordinates with proper spacing
                    int x = (col * noteWidth) + (noteWidth / 4);
                    int y = ((row + 1) * noteHeight) - (noteHeight / 4);

                    // Draw the number
                    String noteStr = String.valueOf(i + 1);
                    java.awt.FontMetrics fm = g.getFontMetrics();
                    int noteWidth2 = fm.stringWidth(noteStr);

                    // Center the number within its cell section
                    x = (col * noteWidth) + ((noteWidth - noteWidth2) / 2);
                    y = (row * noteHeight) + ((noteHeight + fm.getAscent()) / 2);

                    g.drawString(noteStr, x, y);
                }
            }
        }
    }
}