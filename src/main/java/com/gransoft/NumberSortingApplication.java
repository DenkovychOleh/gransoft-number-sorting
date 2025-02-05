package com.gransoft;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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

/**
 * The NumberSortingApplication class provides a graphical user interface for generating random numbers,
 * displaying them in a grid, and sorting them in ascending or descending order.
 *
 * @author Oleh Denkovych
 */
public class NumberSortingApplication {

    private static final int NUMBER_THRESHOLD = 30;  // Constant for the number 30

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
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel("How many numbers to display:");
        inputField = new JTextField(5);
        inputField.setHorizontalAlignment(JTextField.CENTER);

        JButton enterButton = setupEnterButton();

        gbc.gridy++;
        introPanel.add(label, gbc);
        gbc.gridy++;
        introPanel.add(inputField, gbc);
        gbc.gridy++;
        introPanel.add(enterButton, gbc);

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
                if (count > 0) {
                    generateNumbers(count);
                    cardLayout.show(mainPanel, "Sort");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(jFrame, "Please enter a valid number.");
            }
        };
        enterButton.addActionListener(enterAction);
        inputField.addActionListener(enterAction);

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        sortButton = new JButton("Sort");
        setupButtonStyle(sortButton, Color.GREEN);
        JButton resetButton = new JButton("Reset");
        setupButtonStyle(resetButton, Color.GREEN);

        sortButton.addActionListener(e -> sortNumbers());
        resetButton.addActionListener(e -> {
            inputField.setText("");
            numbers.clear();
            cardLayout.show(mainPanel, "Intro");
        });

        gbc.gridy++;
        buttonPanel.add(sortButton, gbc);
        gbc.gridy++;
        buttonPanel.add(resetButton, gbc);

        sortPanel.add(scrollPane, BorderLayout.CENTER);
        sortPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(sortPanel, "Sort");
    }

    /**
     * Generates a list of random numbers and updates the number buttons.
     */
    private void generateNumbers(int count) {
        Random rand = new Random();
        boolean hasSmallNumber = false;

        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(1000) + 1;
            numbers.add(num);
            if (num <= NUMBER_THRESHOLD) {
                hasSmallNumber = true;
            }
        }

        if (!hasSmallNumber) {
            numbers.set(rand.nextInt(numbers.size()), rand.nextInt(NUMBER_THRESHOLD) + 1);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 5, 5, 5);

        int column = 0;
        int row = 0;

        for (Integer num : numbers) {
            JButton btn = new JButton(num.toString());
            btn.addActionListener(e -> handleNumberClick(num));
            setupButtonStyle(btn, Color.BLUE);

            gbc.gridx = column;
            gbc.gridy = row;

            numberPanel.add(btn, gbc);

            row++;

            if (row == 10) {
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
        quickSort(numbers, 0, numbers.size() - 1);
        ascending = !ascending;
        sortButton.setText(ascending ? "Sort Descending" : "Sort Ascending");
        updateNumberButtons();
    }


    /**
     Implements the quicksort algorithm.
     */
    private void quickSort(List<Integer> list, int begin, int end) {
        Collections.sort(list);
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
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, end);
        return i + 1;
    }

}