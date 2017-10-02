import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame {
    private JTextField textfield;
    private boolean number = true;
    private String equalOp = "=";
    private int total;

    public static void main(String[] args) {
        JFrame frame = new Calculator();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Calculator() {
        textfield = new JTextField("0", 12);
        textfield.setHorizontalAlignment(JTextField.RIGHT);

        ActionListener numberListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String digit = event.getActionCommand();
                if (number) {
                    textfield.setText(digit);
                    number = false;
                } else {
                    textfield.setText(textfield.getText() + digit);
                }
            }
        };
        String buttonOrder = "1234567890 ";
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 4, 4));
        for (int i = 0; i < buttonOrder.length(); i++) {
            String key = buttonOrder.substring(i, i + 1);
            if (key.equals(" ")) {
                buttonPanel.add(new JLabel(""));
            } else {
                JButton button = new JButton(key);
                button.addActionListener(numberListener);
                buttonPanel.add(button);
            }
        }
        ActionListener operatorListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (number) {
                    reset();
                    textfield.setText("0");
                } else {
                    number = true;
                    String displayText = textfield.getText();
                    if (equalOp.equals("=")) {
                        total = convertToNumber(displayText);
                    } else if (equalOp.equals("+")) {
                        total += convertToNumber(displayText);
                    } else if (equalOp.equals("-")) {
                        total -= convertToNumber(displayText);
                    } else if (equalOp.equals("*")) {
                        total *= convertToNumber(displayText);
                    } else if (equalOp.equals("/")) {
                        total /= convertToNumber(displayText);
                    }
                    textfield.setText("" + total);
                    equalOp = e.getActionCommand();
                }
            }
        };
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 4, 4));
        String[] opOrder = { "+", "-", "*", "/", "=", "C" };
        for (int i = 0; i < opOrder.length; i++) {
            JButton button = new JButton(opOrder[i]);
            button.addActionListener(operatorListener);
            panel.add(button);
        }
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout(4, 4));
        pan.add(textfield, BorderLayout.NORTH);
        pan.add(buttonPanel, BorderLayout.CENTER);
        pan.add(panel, BorderLayout.EAST);
        this.setContentPane(pan);
        this.pack();
        this.setTitle("Calculator");
        this.setResizable(false);
    }

    private void reset() {
        number = true;
        textfield.setText("0");
        equalOp = "=";
        total = 0;
    }

    private int convertToNumber(String n) {
        return Integer.parseInt(n);
    }
}
