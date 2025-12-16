package com.modulo;
/*
 * MIT License
 * Copyright (c) 2025 ztype764
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import com.modulo.Registry.FunctionRegistry;
import com.modulo.internal.CalcFunction;
import com.modulo.internal.ConfigLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String title = ConfigLoader.getString("ui.window.title");
        int width = ConfigLoader.getInt("ui.window.width");
        int height = ConfigLoader.getInt("ui.window.height");

        setTitle(title);
        setSize(width, height);

        Color bg = Color.decode(ConfigLoader.getColorHex("ui.colors.background"));
        Color keypadBG = Color.decode(ConfigLoader.getColorHex("ui.colors.keypad"));

        getContentPane().setBackground(bg);

        // --- TOP PANEL ---
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(new Color(250, 250, 252));
        top.setBorder(new EmptyBorder(20, 20, 5, 20));

        // Input field - glass style
        inputField = new JTextField();
        inputField.setFont(new Font("Inter", Font.PLAIN, 32));
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        inputField.setBackground(new Color(255, 255, 255));
        inputField.setBorder(new LineBorder(new Color(220, 220, 220), 2, true));
        inputField.setMargin(new Insets(12, 12, 12, 12));
        inputField.setOpaque(true);

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateLive();
            }
        });
        inputField.addActionListener(e -> calculateFinal());

        // Result label
        String defaultResult = ConfigLoader.getString("ui.labels.defaultResult");
        resultLabel = new JLabel(defaultResult, SwingConstants.RIGHT);

        resultLabel.setFont(new Font("Inter", Font.BOLD, 26));
        resultLabel.setForeground(new Color(60, 60, 60));
        resultLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        resultLabel.setFont(new Font("Inter", Font.BOLD, 22));

        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.setBackground(new Color(255, 255, 255));
        displayPanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        displayPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        displayPanel.add(inputField);
        displayPanel.add(resultLabel);

        top.add(displayPanel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // --- BUTTON PANEL ---
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(keypadBG);

        buttonPanel = new JPanel(new GridLayout(5, 4, 14, 14));
        buttonPanel.setBorder(new EmptyBorder(5, 20, 20, 20));

        buttonPanel.setBackground(keypadBG);

        // Pagination buttons
        prevPageBtn = modernButton("◀", false);
        nextPageBtn = modernButton("▶", false);
        prevPageBtn.setFont(new Font("Inter", Font.BOLD, 18));
        nextPageBtn.setFont(new Font("Inter", Font.BOLD, 18));

        prevPageBtn.addActionListener(e -> {
            currentPage--;
            refreshButtons();
        });
        nextPageBtn.addActionListener(e -> {
            currentPage++;
            refreshButtons();
        });

        JPanel pager = new JPanel(new BorderLayout());
        pager.setBorder(new EmptyBorder(5, 20, 5, 20));
        pager.setBackground(keypadBG);
        pager.add(prevPageBtn, BorderLayout.WEST);

        JButton reloadBtn = modernButton("⟳", false);
        reloadBtn.setFont(new Font("Inter", Font.BOLD, 18));
        reloadBtn.addActionListener(e -> {
            FunctionRegistry.reload();
            functions.clear();
            functions.addAll(FunctionRegistry.getFunctions());
            refreshButtons();
            JOptionPane.showMessageDialog(this, "Plugins Reloaded!", "Hot Reload", JOptionPane.INFORMATION_MESSAGE);
        });

        pager.add(reloadBtn, BorderLayout.CENTER);
        pager.add(nextPageBtn, BorderLayout.EAST);

        bottom.add(pager, BorderLayout.NORTH);
        bottom.add(buttonPanel, BorderLayout.CENTER);

        add(bottom, BorderLayout.CENTER);

        // Load functions
        FunctionRegistry.loadFunctions();
        functions.addAll(FunctionRegistry.getFunctions());

        refreshButtons();

        SwingUtilities.invokeLater(inputField::requestFocusInWindow);
        setVisible(true);
    }

    private JButton modernButton(String label, boolean borders) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Inter", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setForeground(Color.decode(ConfigLoader.getColorHex("ui.colors.buttonText")));
        btn.setBackground(Color.decode(ConfigLoader.getColorHex("ui.colors.buttonBackground")));

        if (borders) {
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(6, 6, 6, 6)));
        }

        btn.setOpaque(true);
        btn.setMargin(new Insets(10, 10, 10, 10));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.decode(ConfigLoader.getColorHex("ui.colors.buttonHover")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(248, 248, 248));
            }
        });

        return btn;
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
            JButton btn = modernButton(b, true);

            btn.addActionListener(e -> handleButton(b));
            allButtons.add(btn);
        }

        // Modular scientific buttons
        for (CalcFunction f : functions) {
            JButton btn = modernButton(f.getName(), true);

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
                resultLabel.setText("= 0");
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
            resultLabel.setText("= 0");
            return;
        }
        try {
            double val = evaluate(expr);
            resultLabel.setText("= " + format(val));
        } catch (Exception e) {
            resultLabel.setText("= …");
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
            resultLabel.setText("= " + format(result));
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
        // Performance: Clean string once
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
                // Handle scientific notation (e.g., 1.2E-3)
                if (c == '-' && ((expr.charAt(i - 1) == 'E' || expr.charAt(i - 1) == 'e'))) {
                    continue;
                }

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

                // Split arguments by comma, respecting parentheses
                List<String> argsStr = splitArgs(inside);
                double[] args = new double[argsStr.size()];

                for (int i = 0; i < argsStr.size(); i++) {
                    args[i] = parseAddSubtract(argsStr.get(i)); // Recursive call to top-level parser logic (minus
                                                                // whitespace cleaning)
                }

                return f.execute(args);
            }
        }
        return parsePrimary(expr);
    }

    private List<String> splitArgs(String inside) {
        List<String> args = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < inside.length(); i++) {
            char c = inside.charAt(i);
            if (c == '(')
                depth++;
            if (c == ')')
                depth--;
            if (c == ',' && depth == 0) {
                args.add(inside.substring(start, i));
                start = i + 1;
            }
        }
        args.add(inside.substring(start));
        return args;
    }

    /**
     * Parses and evaluates primary expressions (numbers and parentheses).
     *
     * @param expr The expression to parse.
     * @return The result of the primary expression.
     */
    private double parsePrimary(String expr) {
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseAddSubtract(expr.substring(1, expr.length() - 1));
        }
        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number: " + expr);
        }
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
