/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231082 - Naufal Zaky Nugraha
 * 2 - 5026231035 - Aldani Prasetyo
 * 3 - 5026231183 - Asrid Meilendra
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();

    // Theme colors
    private Color gridLineColor = Color.BLACK;
    private Color lightGridLineColor = Color.LIGHT_GRAY;
    private Color backgroundColor = Color.WHITE;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);

                // Tambahkan border khusus untuk pemisah kotak 3x3
                Border thickBorderVertical = BorderFactory.createMatteBorder(0, 2, 0, 0, gridLineColor);
                Border thickBorderHorizontal = BorderFactory.createMatteBorder(2, 0, 0, 0, gridLineColor);

                if (col > 0 && col % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderVertical,
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                if (row > 0 && row % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderHorizontal,
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                // Untuk sel di pojok kiri atas setiap kotak 3x3
                if ((col > 0 && col % 3 == 0) && (row > 0 && row % 3 == 0)) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(2, 2, 0, 0, gridLineColor),
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                super.add(cells[row][col]);
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(backgroundColor);
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     * @param cellsToGuess number of cells to guess (controls difficulty)
     */
    public void newGame(int cellsToGuess) {
        // Generate a new puzzle
        puzzle.newPuzzle(cellsToGuess);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Generate a new puzzle with default difficulty (Easy)
     */
    public void newGame() {
        newGame(36); // Default to Easy mode
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Show a hint by revealing a random empty cell
     * @return true if a hint was given, false if no empty cells are available
     */
    public boolean showHint() {
        // Create a list of empty cells
        java.util.List<Point> emptyCells = new java.util.ArrayList<>();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS) {
                    emptyCells.add(new Point(row, col));
                }
            }
        }

        // If there are no empty cells, return false
        if (emptyCells.isEmpty()) {
            return false;
        }

        // Select a random empty cell
        int randomIndex = (int)(Math.random() * emptyCells.size());
        Point selectedCell = emptyCells.get(randomIndex);

        // Reveal the correct number in the selected cell
        int row = (int)selectedCell.getX();
        int col = (int)selectedCell.getY();
        cells[row][col].setText(String.valueOf(cells[row][col].number));
        cells[row][col].status = CellStatus.CORRECT_GUESS;
        cells[row][col].paint();

        return true;
    }

    /**
     * Update the theme colors based on dark mode state
     * @param isDarkMode true for dark mode, false for light mode
     */
    public void updateTheme(boolean isDarkMode) {
        // Update colors based on theme
        gridLineColor = isDarkMode ? Color.WHITE : Color.BLACK;
        lightGridLineColor = isDarkMode ? new Color(100, 100, 100) : Color.LIGHT_GRAY;
        backgroundColor = isDarkMode ? new Color(50, 50, 50) : Color.WHITE;

        // Update panel background
        setBackground(backgroundColor);

        // Update all cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                // Update cell borders
                Border thickBorderVertical = BorderFactory.createMatteBorder(0, 2, 0, 0, gridLineColor);
                Border thickBorderHorizontal = BorderFactory.createMatteBorder(2, 0, 0, 0, gridLineColor);

                if (col > 0 && col % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderVertical,
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                if (row > 0 && row % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            thickBorderHorizontal,
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                if ((col > 0 && col % 3 == 0) && (row > 0 && row % 3 == 0)) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(2, 2, 0, 0, gridLineColor),
                            BorderFactory.createLineBorder(lightGridLineColor, 1)
                    ));
                }

                // Update cell colors
                cells[row][col].updateTheme(isDarkMode);
            }
        }

        // Repaint the panel
        repaint();
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();

            // Retrieve the int entered
            int numberIn;
            try {
                numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn < 1 || numberIn > 9) {
                    sourceCell.setText("");
                    return;
                }
            } catch (NumberFormatException ex) {
                sourceCell.setText("");
                return;
            }

            // For debugging
            System.out.println("You entered " + numberIn);

            // Check if the number is correct
            if (numberIn == sourceCell.number) {
                sourceCell.status = CellStatus.CORRECT_GUESS;
            } else {
                sourceCell.status = CellStatus.WRONG_GUESS;
            }
            sourceCell.paint();

            // Check for duplicates and highlight them
            checkAndHighlightDuplicates();

            // Check if solved
            if(isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulation!");
            }
        }
    }

    // Check for duplicates in row, column and 3x3 box
    private void checkAndHighlightDuplicates() {
        // Reset all cells to their original colors first
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].setDuplicate(false);
            }
        }

        // Check for duplicates
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                String currentValue = cells[row][col].getText();
                if (!currentValue.isEmpty()) {
                    // Check row
                    for (int c = 0; c < SudokuConstants.GRID_SIZE; c++) {
                        if (c != col && cells[row][c].getText().equals(currentValue)) {
                            cells[row][col].setDuplicate(true);
                            cells[row][c].setDuplicate(true);
                        }
                    }

                    // Check column
                    for (int r = 0; r < SudokuConstants.GRID_SIZE; r++) {
                        if (r != row && cells[r][col].getText().equals(currentValue)) {
                            cells[row][col].setDuplicate(true);
                            cells[r][col].setDuplicate(true);
                        }
                    }

                    // Check 3x3 box
                    int boxRowStart = (row / 3) * 3;
                    int boxColStart = (col / 3) * 3;
                    for (int r = boxRowStart; r < boxRowStart + 3; r++) {
                        for (int c = boxColStart; c < boxColStart + 3; c++) {
                            if ((r != row || c != col) && cells[r][c].getText().equals(currentValue)) {
                                cells[row][col].setDuplicate(true);
                                cells[r][c].setDuplicate(true);
                            }
                        }
                    }
                }
            }
        }
    }
}