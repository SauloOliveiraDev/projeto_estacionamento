//Equipe: Saulo de Oliveira, Igor Vasconcelos, Leonardo Santos, Paulo Medeiros, Ravi Mariane
package app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FinalizarScreen extends JFrame {
    private String placa;
    private JLabel lblEntrada, lblSaida, lblValor;
    private JButton btnPagar;
    private LocalDateTime dataEntrada, dataSaida;
    private double valorPagar;
    
    private GerenciamentoScreen GerenciamentoScreen;

    public FinalizarScreen(GerenciamentoScreen GerenciamentoScreen) {
    	this.GerenciamentoScreen = GerenciamentoScreen;
        setTitle("Finalizar Estadia - Wallpark");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        // Pedir a placa do veículo
        placa = JOptionPane.showInputDialog(this, "Digite a placa do veículo:", "Finalizar Estadia", JOptionPane.QUESTION_MESSAGE);
        if (placa == null || placa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Placa inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Buscar a data de entrada do veículo no banco
        if (!carregarDados()) {
            JOptionPane.showMessageDialog(this, "Veículo não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Criar labels com informações
        lblEntrada = new JLabel("Entrada: " + formatarData(dataEntrada));
        lblSaida = new JLabel("Saída: " + formatarData(dataSaida));
        lblValor = new JLabel("Valor a pagar: R$ " + String.format("%.2f", valorPagar));

        btnPagar = new JButton("Pagar");
        btnPagar.addActionListener(e -> processarPagamento());
        

        // Adicionar componentes na tela
        add(lblEntrada);
        add(lblSaida);
        add(lblValor);
        add(btnPagar);

        setVisible(true);
    }

    private boolean carregarDados() {
        Connection con = ConexaoDB.conectar();
        try {
            String query = "SELECT data_entrada FROM veiculos WHERE placa = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dataEntrada = LocalDateTime.parse(rs.getString("data_entrada"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                dataSaida = LocalDateTime.now();
                calcularValor();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void calcularValor() {
        long minutos = Duration.between(dataEntrada, dataSaida).toMinutes();
        double precoPorHora = 5.0;
        valorPagar = (minutos / 60.0) * precoPorHora;
    }

    private String formatarData(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }

    private void processarPagamento() {
    	String op = JOptionPane.showInputDialog(this, "1 = À vista\n2 = Débito\n3 = Cartão de Crédito\nDigite o método de pagamento:", "Continua", JOptionPane.QUESTION_MESSAGE);

    	if (!op.equals("1") && !op.equals("2") && !op.equals("3")) {
    	    JOptionPane.showMessageDialog(this, "Método de pagamento digitado inválido!");
    	} else {
    	    String metodo;
    	    if (op.equals("1")) {
    	        metodo = "à vista";
    	    } else if (op.equals("2")) {
    	        metodo = "no débito";
    	    } else {
    	        metodo = "no cartão de crédito";
    	    }

    	    int resposta = JOptionPane.showConfirmDialog(this, "Deseja pagar R$ " + String.format("%.2f", valorPagar) + " " + metodo + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
    	    
    	    if (resposta == JOptionPane.YES_OPTION) {
    	        excluirVeiculo();
    	    }
    	}
    }

    private void excluirVeiculo() {
        Connection con = ConexaoDB.conectar();
        try {
            String query = "DELETE FROM veiculos WHERE placa = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, placa);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pagamento realizado! Veículo removido do sistema.");
            dispose();
            GerenciamentoScreen.atualizarTabelaVeiculos();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao processar pagamento!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

