package com.modulo;

import com.modulo.Registry.FunctionRegistry;
import com.modulo.internal.CalcFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>MIT License</p>

<p>Copyright (c) 2025 ztype764</p>

<p>Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:</p>

<p>The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.</p>

 <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.</p>

 **/
/**
 * The main entry point for the Live Calculator application.
 * <p>
 * This class extends {@link JFrame} and provides the graphical user interface
 * for the calculator.
 * It handles user input, button interactions, and displays the calculation
 * results in real-time.
 * </p>
 */
public class LiveCalculator extends JFrame {

    private final JTextField inputField;
    private final JLabel resultLabel;

    private final JPanel buttonPanel;
    private final JButton prevPageBtn;
    private final JButton nextPageBtn;

    private int currentPage = 0;

    private final List<String> baseButtons = Arrays.asList(
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "(", ")",
            "C", "=", "+");

    private final List<CalcFunction> functions = new ArrayList<>();

    /**
     * Constructs a new {@code LiveCalculator} instance.
     * <p>
     * Initializes the UI components, sets up the layout, and loads the calculator
     * functions.
     * </p>
     */
    public LiveCalculator() {
        setTitle("Live Calculator");
        setSize(420, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputField = new JTextField();
        inputField.setFont(new Font("Monaco", Font.PLAIN, 32));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateLive();
            }
        });

        inputField.addActionListener(e -> calculateFinal());

        resultLabel = new JLabel("Result: 0", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultLabel.setForeground(new Color(0, 120, 0));

        top.add(inputField, BorderLayout.CENTER);
        top.add(resultLabel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        prevPageBtn = new JButton("◀");
        nextPageBtn = new JButton("▶");

        prevPageBtn.addActionListener(e -> {
            currentPage--;
            refreshButtons();
        });
        nextPageBtn.addActionListener(e -> {
            currentPage++;
            refreshButtons();
        });

        bottom.add(prevPageBtn, BorderLayout.WEST);
        bottom.add(nextPageBtn, BorderLayout.EAST);
        bottom.add(buttonPanel, BorderLayout.CENTER);

        add(bottom, BorderLayout.CENTER);

        FunctionRegistry.loadFunctions();
        functions.addAll(FunctionRegistry.getFunctions());

        refreshButtons();

        SwingUtilities.invokeLater(inputField::requestFocusInWindow);
        setVisible(true);
    }

    /**
     * Refreshes the buttons displayed in the button panel based on the current
     * page.
     * <p>
     * This method calculates which buttons to show and updates the UI accordingly.
     * </p>
     */
    private void refreshButtons() {

        List<JButton> allButtons = getJButtons();

        int BUTTONS_PER_PAGE = 20;
        int totalPages = (int) Math.ceil(allButtons.size() / (double) BUTTONS_PER_PAGE);
        if (currentPage < 0)
            currentPage = 0;
        if (currentPage >= totalPages)
            currentPage = totalPages - 1;

        buttonPanel.removeAll();
        int start = currentPage * BUTTONS_PER_PAGE;
        int end = Math.min(start + BUTTONS_PER_PAGE, allButtons.size());

        for (int i = start; i < end; i++) {
            buttonPanel.add(allButtons.get(i));
        }

        prevPageBtn.setEnabled(currentPage > 0);
        nextPageBtn.setEnabled(currentPage < totalPages - 1);

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    /**
     * Generates a list of all available calculator buttons.
     *
     * @return A list of {@link JButton} objects representing base operations and
     *         loaded functions.
     */
    private List<JButton> getJButtons() {
        List<JButton> allButtons = new ArrayList<>();

        // Base buttons first
        for (String b : baseButtons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("Arial", Font.BOLD, 22));
            btn.addActionListener(e -> handleButton(b));
            allButtons.add(btn);
        }

        // Modular scientific buttons
        for (CalcFunction f : functions) {
            JButton btn = new JButton(f.getName());
            btn.setFont(new Font("Arial", Font.BOLD, 22));
            btn.addActionListener(e -> appendFunction(f));
            allButtons.add(btn);
        }
        return allButtons;
    }

    /**
     * Appends a function's insert text to the input field and triggers a live
     * calculation.
     *
     * @param func The {@link CalcFunction} to append.
     */
    private void appendFunction(CalcFunction func) {
        inputField.setText(inputField.getText() + func.getInsertText());
        calculateLive();
    }

    /**
     * Handles button clicks for base calculator operations.
     *
     * @param text The text of the button clicked.
     */
    private void handleButton(String text) {
        switch (text) {
            case "C":
                inputField.setText("");
                resultLabel.setText("Result: 0");
                break;

            case "=":
                calculateFinal();
                break;

            default:
                inputField.setText(inputField.getText() + text);
                calculateLive();
        }
    }

    /**
     * Performs a live calculation based on the current text in the input field.
     * <p>
     * Updates the result label with the calculated value or an ellipsis if the
     * expression is incomplete.
     * </p>
     */
    private void calculateLive() {
        String expr = inputField.getText().trim();
        if (expr.isEmpty()) {
            resultLabel.setText("Result: 0");
            return;
        }
        try {
            double val = evaluate(expr);
            resultLabel.setText("Result: " + format(val));
        } catch (Exception e) {
            resultLabel.setText("Result: …");
        }
    }

    /**
     * Performs the final calculation and updates the input field with the result.
     * <p>
     * This is typically triggered by the equals button or pressing Enter.
     * </p>
     */
    private void calculateFinal() {
        String expr = inputField.getText().trim();
        if (expr.isEmpty())
            return;
        try {
            double result = evaluate(expr);
            inputField.setText(format(result));
            resultLabel.setText("Result: " + format(result));
        } catch (Exception e) {
            resultLabel.setText("Error");
        }
    }

    /**
     * Evaluates a mathematical expression string.
     *
     * @param expr The expression to evaluate.
     * @return The result of the evaluation.
     */
    private double evaluate(String expr) {
        return parseAddSubtract(expr.replaceAll("\\s+", ""));
    }

    /**
     * Parses and evaluates addition and subtraction operations.
     *
     * @param expr The expression to parse.
     * @return The result of the addition/subtraction.
     */
    private double parseAddSubtract(String expr) {
        int depth = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                depth++;
            if (c == '(')
                depth--;
            if (depth == 0 && i > 0 && (c == '+' || c == '-')) {
                return c == '+'
                        ? parseAddSubtract(expr.substring(0, i)) + parseMultiply(expr.substring(i + 1))
                        : parseAddSubtract(expr.substring(0, i)) - parseMultiply(expr.substring(i + 1));
            }
        }
        return parseMultiply(expr);
    }

    /**
     * Parses and evaluates multiplication and division operations.
     *
     * @param expr The expression to parse.
     * @return The result of the multiplication/division.
     */
    private double parseMultiply(String expr) {
        int depth = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                depth++;
            if (c == '(')
                depth--;
            if (depth == 0 && i > 0 && (c == '*' || c == '/')) {
                return c == '*'
                        ? parseMultiply(expr.substring(0, i)) * parseUnary(expr.substring(i + 1))
                        : parseMultiply(expr.substring(0, i)) / parseUnary(expr.substring(i + 1));
            }
        }
        return parseUnary(expr);
    }

    /**
     * Parses and evaluates unary operations (positive/negative signs).
     *
     * @param expr The expression to parse.
     * @return The result of the unary operation.
     */
    private double parseUnary(String expr) {
        if (expr.startsWith("+"))
            return parseUnary(expr.substring(1));
        if (expr.startsWith("-"))
            return -parseUnary(expr.substring(1));
        return parseFunction(expr);
    }

    /**
     * Parses and evaluates custom functions.
     *
     * @param expr The expression to parse.
     * @return The result of the function evaluation.
     */
    private double parseFunction(String expr) {
        for (CalcFunction f : functions) {
            String name = f.getName();
            String fnPattern = name + "(";

            if (expr.startsWith(fnPattern) && expr.endsWith(")")) {
                String inside = expr.substring(fnPattern.length(), expr.length() - 1);
                double val = evaluate(inside);
                return f.evaluate(val);
            }
        }
        return parsePrimary(expr);
    }

    /**
     * Parses and evaluates primary expressions (numbers and parentheses).
     *
     * @param expr The expression to parse.
     * @return The result of the primary expression.
     */
    private double parsePrimary(String expr) {
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return evaluate(expr.substring(1, expr.length() - 1));
        }
        return Double.parseDouble(expr);
    }

    /**
     * Formats a double value into a string, removing unnecessary decimal places.
     *
     * @param n The number to format.
     * @return The formatted string.
     */
    private String format(double n) {
        if (n == (long) n)
            return String.valueOf((long) n);
        return ("" + n).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LiveCalculator::new);
    }
}
