import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import javax.script.*;

public class Calculator extends JFrame {
    int borderWidth = 400;
    int borderHeight = 600;

    Color grayNSU = new Color(67, 67, 67);
    Color greenNSU = new Color(127, 205, 51);
    Color blueNSU = new Color(29, 191, 234);
    Color lightGrayNSU = new Color(225, 225, 225);

    String[] buttonValues = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            ".", "0", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    boolean canPlaceDot = true;

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("js");

    Calculator() {
        setVisible(true);
        setTitle("Calculator");
        setSize(borderWidth, borderHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        displayLabel.setBackground(grayNSU);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setVisible(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(grayNSU);
        add(buttonsPanel);


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

                String text = displayLabel.getText();
                String last = text.substring(text.length() - 1);

                JButton button1 = (JButton) e.getSource();
                String buttonValue = button1.getText();
                switch (buttonValue) {
                    case "." -> {
                        if (canPlaceDot) {
                            String lastChar = displayLabel.getText().substring(displayLabel.getText().length() - 1);
                            if (!"+-÷×".contains(lastChar)) {
                                displayLabel.setText(displayLabel.getText() + ".");
                                canPlaceDot = false;
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
                        displayLabel.setText("0");
                        canPlaceDot = true;
                    }
                    case "+", "-", "÷", "×" -> {
                        if (!"+-.÷×".contains(last)) {
                            displayLabel.setText(displayLabel.getText() + buttonValue);
                            canPlaceDot = true;
                        }
                    }
                    case "=" -> {
                        String currentText = displayLabel.getText();
                        String mathExpression = currentText.replace("×", "*").replace("÷", "/");
                        try {
                            Object result = engine.eval(mathExpression);
                            displayLabel.setText(result.toString());
                        } catch (Exception ex) {
                            displayLabel.setText("Error");
                        }
                        //displayLabel.setText(mathExpression);
                    }
                    default -> {
                        String currentText = displayLabel.getText();

                        if (Objects.equals(currentText, "0")) {
                            displayLabel.setText(buttonValue);
                            break;
                        }

                        String[] tokens = currentText.split("[+\\-÷×]");
                        String lastToken = tokens[tokens.length - 1];

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
}
