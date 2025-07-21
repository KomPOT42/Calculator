import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Calculator extends JFrame {
    int borderWidth = 400;
    int borderHeight = 700;

    Color grayNSU = new Color(67, 67, 67);
    Color greenNSU = new Color(127, 205, 51);
    Color blueNSU = new Color(29, 191, 234);
    Color lightGrayNSU = new Color(225, 225, 225);
    Color lightGray = new Color(143, 143, 143);

    String[] buttonValues = {
            "AC", "←", "()", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            ".", "0", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "()", "←"};

    JLabel displayLabel = new JLabel();
    JPanel buttonsPanel = new JPanel();
    JLabel historyLabel = new JLabel();
    JPanel topPanel = new JPanel();
    boolean canPlaceDot = true;
    boolean clothingParenthesis = false;

    private final Stack<Double> numberStack = new Stack<>();
    private final Stack<Character> operatorStack = new Stack<>();

    private final Map<Character, Integer> precedence = Map.of(
            '+', 1,
            '-', 1,
            '*', 2,
            '/', 2,
            '~', 3
    );

    Calculator() {
        setVisible(true);
        setTitle("Calculator");
        setSize(borderWidth, borderHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        displayLabel.setBackground(grayNSU);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setVisible(true);

        historyLabel.setBackground(grayNSU);
        historyLabel.setForeground(lightGray);
        historyLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        historyLabel.setHorizontalAlignment(JLabel.RIGHT);
        historyLabel.setText("history");
        historyLabel.setOpaque(true);
        historyLabel.setVisible(true);
        historyLabel.setPreferredSize(new Dimension(borderWidth, 50));

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(grayNSU);
        add(buttonsPanel);

        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.add(historyLabel);
        topPanel.add(displayLabel);
        add(topPanel, BorderLayout.NORTH);

        for (String value : buttonValues) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(value);
            button.setFocusable(false);
            button.setBorder(new LineBorder(grayNSU));
            if (Arrays.asList(topSymbols).contains(value)) {
                button.setBackground(blueNSU);
                button.setForeground(Color.white);
            } else if (Arrays.asList(rightSymbols).contains(value)) {
                button.setBackground(greenNSU);
                button.setForeground(Color.white);
            } else button.setBackground(lightGrayNSU);
            buttonsPanel.add(button);

            button.addActionListener(e -> {

                JButton button1 = (JButton) e.getSource();
                String buttonValue = button1.getText();
                switch (buttonValue) {
                    case "." -> {
                        if (canPlaceDot) {
                            String lastChar = displayLabel.getText().substring(displayLabel.getText().length() - 1);
                            if (!"+-÷×√".contains(lastChar)) {
                                if ("Error".contains(lastChar)) displayLabel.setText("0" + buttonValue);
                                else {
                                    displayLabel.setText(displayLabel.getText() + ".");
                                    canPlaceDot = false;
                                }
                            }
                        }
                    }
                    case "0" -> {
                        String currentText = displayLabel.getText();
                        if (Objects.equals(currentText, "0")) break;
                        char lastChar = currentText.charAt(currentText.length() - 1);
                        if ("+-÷×".indexOf(lastChar) != -1) {
                            displayLabel.setText(currentText + "0");
                            break;
                        }
                        String[] tokens = currentText.split("[-+÷×]");
                        String lastToken = tokens[tokens.length - 1];
                        if (lastToken.equals("0")) break;
                        displayLabel.setText(currentText + "0");
                    }
                    case "AC" -> {
                        if (displayLabel.getText().equals("0")) {
                            historyLabel.setText("history");
                            historyLabel.setForeground(lightGray);
                        }
                        else {
                            displayLabel.setText("0");
                            canPlaceDot = true;
                            clothingParenthesis = false;
                        }
                    }
                    case "+", "-", "÷", "×" -> {
                        String lastChar = displayLabel.getText().substring(displayLabel.getText().length() - 1);
                        if (!"+-.÷×".contains(lastChar)) {
                            if ("Error".contains(lastChar)) displayLabel.setText("0" + buttonValue);
                            else {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                                canPlaceDot = true;
                            }
                        }
                    }
                    case "√" -> {
                        String currentText = displayLabel.getText();
                        if (currentText.endsWith("√")) break;
                        if ("0".equals(currentText)) {
                            displayLabel.setText("√");
                        }
                        else {
                            String lastChar = currentText.substring(currentText.length() - 1);
                            if ("0123456789)".contains(lastChar)) {
                                displayLabel.setText(currentText + "×√");
                            } else {
                                displayLabel.setText(currentText + "√");
                            }
                        }
                    }
                    case "←" -> {
                        String currentText = displayLabel.getText();
                        if (currentText.length() > 1) {
                            String lastChar = displayLabel.getText().substring(displayLabel.getText().length() - 1);
                            displayLabel.setText(currentText.substring(0, currentText.length() - 1));
                            if (lastChar.equals(".")) canPlaceDot = true;
                        } else {
                            displayLabel.setText("0");
                        }
                    }
                    case "()" -> {
                        String lastChar = displayLabel.getText().substring(displayLabel.getText().length() - 1);
                        if ("Error".contains(lastChar)) {
                            displayLabel.setText("(");
                            clothingParenthesis = true;
                        }
                        else {
                            if (Objects.equals(displayLabel.getText(), "0")) {
                                displayLabel.setText("(");
                                clothingParenthesis = true;
                            } else if (!clothingParenthesis) {
                                if ("0123456789)".contains(lastChar))
                                    displayLabel.setText(displayLabel.getText() + "×");
                                displayLabel.setText(displayLabel.getText() + "(");
                                clothingParenthesis = true;
                            } else {
                                displayLabel.setText(displayLabel.getText() + ")");
                                clothingParenthesis = false;
                            }
                        }
                    }
                    case "=" -> {
                        try {
                            String expression = displayLabel.getText();
                            expression = expression.replace("×", "*")
                                    .replace("÷", "/")
                                    .replace("√", "~");

                            double result = evaluateExpression(expression);
                            String resultText;

                            if (result == (int) result) {
                                resultText = String.valueOf((int) result);
                            } else {
                                resultText = String.format("%.10f", result);
                                resultText = resultText.replaceAll("0*$", "").replaceAll("\\.$", "");
                            }
                            displayLabel.setText(resultText);
                            historyLabel.setText(expression.replace("*", "×").replace("/", "÷").replace("~", "√") + "=" + resultText);
                            historyLabel.setForeground(lightGrayNSU);
                        } catch (Exception ex) {
                            displayLabel.setText("Error");
                        }
                        canPlaceDot = true;
                        clothingParenthesis = false;
                    }
                    default -> {
                        String currentText = displayLabel.getText();
                        String lastChar = currentText.substring(currentText.length() - 1);

                        if (Objects.equals(displayLabel.getText(), "0")) {
                            displayLabel.setText(buttonValue);
                            break;
                        }
                        else if ("Error".contains(lastChar)) {
                            displayLabel.setText(buttonValue);
                            break;
                        }

                        String[] tokens = currentText.split("[+\\-÷×]");
                        String lastToken = tokens[tokens.length - 1];

                        if (")".contains(lastChar)) currentText = (displayLabel.getText() + "×");

                        if (lastToken.equals("0") && currentText.length() > 1) {
                            char beforeLast = currentText.charAt(currentText.length() - 2);
                            if ("+-÷×".indexOf(beforeLast) != -1) {
                                displayLabel.setText(currentText.substring(0, currentText.length() - 1) + buttonValue);
                                break;
                            }
                        }
                        displayLabel.setText(currentText + buttonValue);
                    }
                }
            });
        }
    }
    private double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replaceAll("\\(-", "(0-");
        if (expression.startsWith("-")) {
            expression = "0" + expression;
        }
        List<String> tokens = tokenize(expression);

        for (String token : tokens) {
            if (isNumber(token)) {
                numberStack.push(Double.parseDouble(token));
            } else if (token.equals("(")) {
                operatorStack.push('(');
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    processOperator();
                }
                operatorStack.pop();
            } else {
                char op = token.charAt(0);
                while (!operatorStack.isEmpty() &&
                        operatorStack.peek() != '(' &&
                        precedence.getOrDefault(operatorStack.peek(), 0) >= precedence.getOrDefault(op, 0)) {
                    processOperator();
                }
                operatorStack.push(op);
            }
        }
        while (!operatorStack.isEmpty()) {
            processOperator();
        }
        return numberStack.pop();
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                current.append(c);
            } else {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }
        return tokens;
    }
    private boolean isNumber(String token) {
        return token.matches("\\d+(\\.\\d+)?");
    }
    private void processOperator() {
        char op = operatorStack.pop();
        if (op == '~') {
            double operand = numberStack.pop();
            numberStack.push(Math.sqrt(operand));
        } else {
            double b = numberStack.pop();
            double a = numberStack.pop();

            switch (op) {
                case '+' -> numberStack.push(a + b);
                case '-' -> numberStack.push(a - b);
                case '*' -> numberStack.push(a * b);
                case '/' -> {
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    numberStack.push(a / b);
                }
            }
        }
    }
    public static void main(String[] args) {
        new Calculator();
    }
}
