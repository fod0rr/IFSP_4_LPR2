import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cadastro de Aluno de Academia");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = createMainPanel();
            frame.add(panel);
            frame.setVisible(true);
        });
    }
//Feito por Maria Eduarda Fodor (CB3025063) e Pedro Xavier Oliveira (CB3027376)
    private static JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return panel;
    }

    private static JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblIdade = new JLabel("Idade:");
        JLabel lblPeso = new JLabel("Peso (kg):");
        JLabel lblAltura = new JLabel("Altura (m):");
        JLabel lblObjetivo = new JLabel("Objetivo:");

        JTextField txtNome = new JTextField();
        JTextField txtIdade = new JTextField();
        JTextField txtPeso = new JTextField();
        JTextField txtAltura = new JTextField();
        JTextField txtObjetivo = new JTextField();

        formPanel.add(lblNome);
        formPanel.add(txtNome);
        formPanel.add(lblIdade);
        formPanel.add(txtIdade);
        formPanel.add(lblPeso);
        formPanel.add(txtPeso);
        formPanel.add(lblAltura);
        formPanel.add(txtAltura);
        formPanel.add(lblObjetivo);
        formPanel.add(txtObjetivo);

        return formPanel;
    }

    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton btnIncluir = createStyledButton("Incluir", new Color(34, 139, 34));
        JButton btnLimpar = createStyledButton("Limpar", new Color(255, 165, 0));
        JButton btnApresentar = createStyledButton("Apresentar Dados", new Color(30, 144, 255));
        JButton btnSair = createStyledButton("Sair", new Color(255, 69, 0));

        buttonPanel.add(btnIncluir);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnApresentar);
        buttonPanel.add(btnSair);

        btnIncluir.addActionListener(createIncluirListener());
        btnApresentar.addActionListener(createApresentarListener());
        btnLimpar.addActionListener(createLimparListener());
        btnSair.addActionListener(createSairListener());

        return buttonPanel;
    }

    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private static ActionListener createIncluirListener() {
        return e -> {
            try {
                String nome = JOptionPane.showInputDialog("Nome:");
                String idadeStr = JOptionPane.showInputDialog("Idade:");
                String pesoStr = JOptionPane.showInputDialog("Peso (kg):");
                String alturaStr = JOptionPane.showInputDialog("Altura (m):");
                String objetivo = JOptionPane.showInputDialog("Objetivo:");

                if (nome == null || idadeStr == null || pesoStr == null || alturaStr == null || objetivo == null) {
                    return;
                }

                int idade = Integer.parseInt(idadeStr);
                float peso = Float.parseFloat(pesoStr);
                float altura = Float.parseFloat(alturaStr);

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/academia",
                        "root",
                        "senhadosql"
                );

                String query = "INSERT INTO alunos (nome, idade, peso, altura, objetivo) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, idade);
                preparedStatement.setFloat(3, peso);
                preparedStatement.setFloat(4, altura);
                preparedStatement.setString(5, objetivo);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Dados incluídos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private static ActionListener createApresentarListener() {
        return e -> {
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/academia",
                        "root",
                        "senhadosql"
                );

                String query = "SELECT * FROM alunos";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    int idade = resultSet.getInt("idade");
                    float peso = resultSet.getFloat("peso");
                    float altura = resultSet.getFloat("altura");
                    String objetivo = resultSet.getString("objetivo");

                    String alunoJson = String.format(
                            "{\n  \"nome\": \"%s\",\n  \"idade\": %d,\n  \"peso\": %.2f,\n  \"altura\": %.2f,\n  \"objetivo\": \"%s\"\n}",
                            nome, idade, peso, altura, objetivo
                    );

                    result.append(alunoJson).append(",\n");
                }

                if (result.length() > 0) {
                    result.setLength(result.length() - 2);
                }

                JTextArea textArea = new JTextArea(result.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Dados dos Alunos", JOptionPane.INFORMATION_MESSAGE);
                connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private static ActionListener createLimparListener() {
        return e -> JOptionPane.showMessageDialog(null, "Campos Limpos", "Limpar", JOptionPane.INFORMATION_MESSAGE);
    }

    private static ActionListener createSairListener() {
        return e -> System.exit(0);
    }
}
