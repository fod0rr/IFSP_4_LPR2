package Calculadora;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Feito por Maria Eduarda Fodor CB3025063, Pedro Xavier Oliveira CB3027376 

public class Calculadora extends JFrame implements ActionListener {

    private JTextField textField;
    private double resultado;
    private String operador;
    private boolean novoNumero;

    public Calculadora() {
        setTitle("Calculadora");
        setSize(250, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textField = new JTextField("0");
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        textField.setMargin(new Insets(10, 10, 10, 10));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        add(textField, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        resultado = 0;
        operador = "=";
        novoNumero = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        try {
            if ("0123456789.".contains(comando)) {
                if (novoNumero) {
                    textField.setText(comando);
                    novoNumero = false;
                } else {
                    textField.setText(textField.getText() + comando);
                }
            } else if (comando.equals("C")) {
                textField.setText("0");
                resultado = 0;
                operador = "=";
                novoNumero = true;
            } else {
                if (!novoNumero) {
                    calculate(Double.parseDouble(textField.getText()));
                    novoNumero = true;
                }
                operador = comando;
            }
        } catch (NumberFormatException ex) {
            textField.setText("Error");
        } finally {
            if (comando.equals("=")) {
                textField.setText("" + resultado);
            }
        }
    }

    private void calculate(double numero) {
        switch (operador) {
            case "+":
                resultado += numero;
                break;
            case "-":
                resultado -= numero;
                break;
            case "*":
                resultado *= numero;
                break;
            case "/":
                if (numero != 0) {
                    resultado /= numero;
                } else {
                    textField.setText("Error");
                }
                break;
            case "=":
                resultado = numero;
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculadora calc = new Calculadora();
            calc.setVisible(true);
        });
    }
}
