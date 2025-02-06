package com.gransoft;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * The NumberSortingApplication class provides a graphical user interface for generating random numbers,
 * displaying them in a grid, and sorting them in ascending or descending order.
 *
 * @author Oleh Denkovych
 */
public class NumberSortingApplication {

    private static final int NUMBER_THRESHOLD = 30;  // Constant for the number 30
    private static final int MAX_RANDOM_VALUE = 1000; // Upper bound for random numbers
    private static final int GRID_ROW_LIMIT = 10; // Max number of rows
    private static final int ANIMATION_DELAY = 300; // Delay for visualization in milliseconds
    private static final Random RANDOM = new Random();  // Reusable Random


    private final JFrame jFrame;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private JTextField inputField;
    private JPanel numberPanel;
    private JButton sortButton;
    private final List<Integer> numbers = new ArrayList<>();
    private boolean ascending = true;

    /**
     * Constructor.
     */
    public NumberSortingApplication() {
        jFrame = new JFrame("Sorting App");
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setSize(500, 500);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupIntroScreen();
        setupSortScreen();

        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    /**
     * The main entry point of the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberSortingApplication::new);
    }

    /**
     * Sets up the intro screen.
     */
    private void setupIntroScreen() {
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel("How many numbers to display:");
        inputField = new JTextField(5);
        inputField.setHorizontalAlignment(JTextField.CENTER);

        JButton enterButton = setupEnterButton();

        gridBagConstraints.gridy++;
        introPanel.add(label, gridBagConstraints);
        gridBagConstraints.gridy++;
        introPanel.add(inputField, gridBagConstraints);
        gridBagConstraints.gridy++;
        introPanel.add(enterButton, gridBagConstraints);

        mainPanel.add(introPanel, "Intro");
    }

    /**
     *  Creates and sets up the "Enter" button.
     */
    private JButton setupEnterButton() {
        JButton enterButton = new JButton("Enter");
        setupButtonStyle(enterButton, Color.BLUE);

        ActionListener enterAction = e -> {
            try {
                int count = Integer.parseInt(inputField.getText());
                if (count > 0 && count <= MAX_RANDOM_VALUE) {
                    generateNumbers(count);
                    cardLayout.show(mainPanel, getSortButtonText());
                }
                else {
                    JOptionPane.showMessageDialog(jFrame,
                            "Please enter a number between 1 and " + MAX_RANDOM_VALUE,
                            "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(jFrame,
                        "Please enter a valid whole number.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        };
        enterButton.addActionListener(enterAction);
        return enterButton;
    }

    /**
     * Sets up the sorting screen with number display and sorting options.
     */
    private void setupSortScreen() {
        JPanel sortPanel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        numberPanel = new JPanel(new GridBagLayout());
        scrollPane.setViewportView(numberPanel);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        sortButton = new JButton(getSortButtonText());
        setupButtonStyle(sortButton, Color.GREEN);
        JButton resetButton = new JButton("Reset");
        setupButtonStyle(resetButton, Color.GREEN);

        sortButton.addActionListener(e -> sortNumbers());
        resetButton.addActionListener(e -> {
            inputField.setText("");
            numbers.clear();
            cardLayout.show(mainPanel, "Intro");
        });

        gridBagConstraints.gridy++;
        buttonPanel.add(sortButton, gridBagConstraints);
        gridBagConstraints.gridy++;
        buttonPanel.add(resetButton, gridBagConstraints);

        sortPanel.add(scrollPane, BorderLayout.CENTER);
        sortPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(sortPanel, getSortButtonText());
    }

    /**
     * Generates a list of random numbers and updates the number buttons.
     */
    private void generateNumbers(int count) {
        boolean hasSmallNumber = false;

        for (int i = 0; i < count; i++) {
            int randomNumber = RANDOM.nextInt(MAX_RANDOM_VALUE
            ) + 1;
            numbers.add(randomNumber);
            if (randomNumber <= NUMBER_THRESHOLD) {
                hasSmallNumber = true;
            }
        }

        if (!hasSmallNumber) {
            numbers.set(RANDOM.nextInt(numbers.size()), RANDOM.nextInt(NUMBER_THRESHOLD) + 1);
        }

        updateNumberButtons();
    }

    /**
     * Sets the style for buttons with the specified background color.
     */
    private static void setupButtonStyle(
            JButton button, Color background) {

        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    /**
     * Updates the number buttons displayed on the screen.
     */
    private void updateNumberButtons() {
        numberPanel.removeAll();

        numberPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        int column = 0;
        int row = 0;

        for (Integer num : numbers) {
            JButton numberButton = new JButton(num.toString());
            numberButton.addActionListener(e -> handleNumberClick(num));
            setupButtonStyle(numberButton, Color.BLUE);

            gridBagConstraints.gridx = column;
            gridBagConstraints.gridy = row;

            numberPanel.add(numberButton, gridBagConstraints);
            row++;

            if (row == GRID_ROW_LIMIT) {
                row = 0;
                column++;
            }
        }

        numberPanel.revalidate();
        numberPanel.repaint();
    }

    /**
     * Handles the click event for number buttons and generates more numbers if the clicked number is below the threshold.
     */
    private void handleNumberClick(int num) {
        if (num <= NUMBER_THRESHOLD) {
            numbers.clear();
            generateNumbers(num);
        }
        else {
            JOptionPane.showMessageDialog(jFrame, "Please select a value smaller or equal to " + NUMBER_THRESHOLD + ".");
        }
    }

    /**
     * Sorts the list of numbers in ascending or descending order, and updates the button text accordingly.
     */
    private void sortNumbers() {
        ascending = !ascending;
        sortButton.setEnabled(false);

        SwingWorker<Void, List<Integer>> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                quickSort(numbers, 0, numbers.size() - 1);
                return null;
            }

            @Override
            protected void done() {
                sortButton.setEnabled(true);
                sortButton.setText(getSortButtonText());
            }
        };

        worker.execute();
    }

    /**
     Implements the quicksort algorithm.
     */
    private void quickSort(List<Integer> list, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(list, begin, end);
            quickSort(list, begin, partitionIndex - 1);
            quickSort(list, partitionIndex + 1, end);
        }
    }

    /**
     * Partitions the list for quicksort.
     */
    private int partition(List<Integer> list, int begin, int end) {
        int pivot = list.get(end);
        int i = begin - 1;
        for (int j = begin; j < end; j++) {
            if ((ascending && list.get(j) <= pivot) || (!ascending && list.get(j) >= pivot)) {
                i++;
                highlightSwap(i, j);
                Collections.swap(list, i, j);
                updateNumberButtons();
                sleep();
            }
        }
        highlightSwap(i + 1, end);
        Collections.swap(list, i + 1, end);
        updateNumberButtons();
        sleep();
        return i + 1;
    }

    /**
     * Highlights the buttons being swapped by changing their background color.
     */
    private void highlightSwap(int i, int j) {
        JButton button1 = (JButton) numberPanel.getComponent(i);
        JButton button2 = (JButton) numberPanel.getComponent(j);

        button1.setBackground(Color.RED);
        button2.setBackground(Color.RED);

        numberPanel.revalidate();
        numberPanel.repaint();

        sleep();

        button1.setBackground(Color.BLUE);
        button2.setBackground(Color.BLUE);
    }

    /**
     * Pauses the execution for a given amount of time to create a visualization effect.
     */
    private void sleep() {
        try {
            Thread.sleep(ANIMATION_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
    * Returns the label for the sort button based on the current sorting order.
    */
    private String getSortButtonText() {
        return ascending ? "Sort Descending" : "Sort Ascending";
    }

}