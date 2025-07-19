import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Calculator extends JFrame {
    int borderWidth = 400;
    int borderHeight = 600;

    Color grayNSU = new Color(67, 67, 67);
    Color greenNSU = new Color(127,205,51);
    Color blueNSU = new Color(29,191,234);
    Color lightGrayNSU = new Color(225,225,225);

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

    Boolean next = true;

    Calculator() {
        setVisible(true);
        setTitle("Calculator");
        setSize(borderWidth, borderHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setResizable(false);
        setLayout(new BorderLayout());

        displayLabel.setBackground(grayNSU);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80 ));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setVisible(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5,4));
        buttonsPanel.setBackground(grayNSU);
        add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(grayNSU));
            if (Arrays.asList(topSymbols).contains(buttonValue)){
                button.setBackground(blueNSU);
                button.setForeground(Color.white);
            }
            else if (Arrays.asList(rightSymbols).contains(buttonValue)){
                button.setBackground(greenNSU);
                button.setForeground(Color.white);
            }
            else button.setBackground(lightGrayNSU);
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();
                    String text = displayLabel.getText();
                    String last = text.substring(text.length()-1);
                    switch (buttonValue) {
                        case "." -> {
                            if ((next) && (!"+-÷×".contains(last))) {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                                next = false;
                            }
                        }
                        case "AC" -> {
                            displayLabel.setText("0");
                        }
                        case "+", "-", "÷", "×" -> {
                            if (!"+-.÷×".contains(last)) {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                                next = true;
                            }
                        }
                        default -> {
                            if (displayLabel.getText() == "0") displayLabel.setText(buttonValue);
                            else if (!"0".contains(last)){
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }
                    }
                }
            });
        }
    }
}
