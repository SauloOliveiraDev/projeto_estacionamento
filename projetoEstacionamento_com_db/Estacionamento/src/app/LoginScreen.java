//Equipe: Saulo de Oliveira, Igor Vasconcelos, Leonardo Santos, Paulo Medeiros, Ravi Mariane
package app;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public LoginScreen() {
        setTitle("Login - Wallpark");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(0, 1, 192, 53);
        txtEmail = new JTextField();
        txtEmail.setBounds(142, 13, 192, 30);
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
        lblSenha.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSenha.setBounds(0, 54, 192, 53);
        txtSenha = new JPasswordField();
        txtSenha.setBounds(142, 65, 192, 30);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBounds(127, 118, 142, 42);
        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pega o email e a senha digitados e chama o metodo verificarLogin
                verificarLogin(txtEmail.getText(), new String(txtSenha.getPassword()));
            }
        });
        panel.setLayout(null);

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblSenha);
        panel.add(txtSenha);
        JLabel label = new JLabel();
        label.setBounds(0, 107, 192, 53);
        panel.add(label);
        panel.add(btnLogin);

        getContentPane().add(panel);
    }

    /* Metodo que verifica se o login e senha digitados estao corretos. 
    Se estiverem certos, abre a tela de gerenciamento, senao exibe uma mensagem de erro.*/
    private void verificarLogin(String email, String senha) {
        Connection con = ConexaoDB.conectar(); // Conecta ao banco de dados
        try {
            // Prepara a consulta SQL para verificar o usuario no banco
            String query = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            // Se encontrar um usuario com esse email e senha, abre a proxima tela
            if (rs.next()) {
                new GerenciamentoScreen().setVisible(true); // Abre a tela de gerenciamento
                dispose(); // Fecha a tela de login
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha invalidos."); // Exibe erro
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Mostra erro no console se algo der errado
        }
    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}
