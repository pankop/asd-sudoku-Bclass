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
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The main Sudoku program
 */
public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("Reset");
    JComboBox<String> levelSelector;
    JPanel buttonPanel = new JPanel();
    private Timer gameTimer;
    private JLabel timerLabel;
    private int secondsElapsed;

    // Constructor
    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Create level selector
        String[] levels = {"Easy", "Medium", "Hard"};
        levelSelector = new JComboBox<>(levels);

        // Add action listener to level selector for automatic game start
        levelSelector.addActionListener(e -> startNewGame());

        // Initialize timer label
        timerLabel = new JLabel("Time: 00:00");
        initializeTimer();

        // Add components to button panel
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(new JLabel("Difficulty: "));
        buttonPanel.add(levelSelector);
        buttonPanel.add(btnNewGame);
        buttonPanel.add(timerLabel);

        // Add button panel to the south
        cp.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener for New Game button
        btnNewGame.addActionListener(e -> startNewGame());

        // Initialize the game board to start the game
        startNewGame();

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    // Start a new game based on selected difficulty
    private void startNewGame() {
        String level = (String) levelSelector.getSelectedItem();
        int cellsToGuess;

        switch (level) {
            case "Easy":
                cellsToGuess = 36;
                break;
            case "Medium":
                cellsToGuess = 45;
                break;
            case "Hard":
                cellsToGuess = 49;
                break;
            default:
                cellsToGuess = 36; // Default to Easy
        }

        board.newGame(cellsToGuess);
        resetTimer();
    }

    // Initialize the timer
    private void initializeTimer() {
        secondsElapsed = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                updateTimerLabel();
            }
        });
    }

    // Reset and start the timer
    private void resetTimer() {
        gameTimer.stop();
        secondsElapsed = 0;
        updateTimerLabel();
        gameTimer.start();
    }

    // Update the timer display
    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    /** The entry main() entry method */
    public static void play() {
        // Run the game in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> new Sudoku());
    }
}