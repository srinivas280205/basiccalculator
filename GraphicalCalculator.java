
package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicalCalculator extends JFrame implements ActionListener {
    private JTextField inputField;

    public GraphicalCalculator() {
        setTitle("Graphical Expression Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.BOLD, 28));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setEditable(false);

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "^", "+",
            "(", ")", "C", "="
        };

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        for (String b : buttons) {
            JButton button = new JButton(b);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(this);
            panel.add(button);
        }

        add(inputField, BorderLayout.NORTH);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphicalCalculator::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "=" -> {
                try {
                    double result = evaluate(inputField.getText());
                    inputField.setText(String.valueOf(result));
                } catch (Exception ex) {
                    inputField.setText("Error");
                }
            }
            case "C" -> inputField.setText("");
            default -> inputField.setText(inputField.getText() + cmd);
        }
    }

    public double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() { ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected character: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected character: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }.parse();
    }
}
