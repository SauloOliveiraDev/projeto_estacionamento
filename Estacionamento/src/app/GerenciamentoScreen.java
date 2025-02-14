package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciamentoScreen extends JFrame {
    private JTable tabelaVeiculos;
    private JButton btnAdicionar, btnAlterar, btnExcluir, btnRelatorio, btnFinalizar;
    private JPanel painelVeiculos;

    public GerenciamentoScreen() {
        setTitle("Gerenciamento de Veículos - Wallpark");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Painel de veículos (tabela)
        painelVeiculos = new JPanel();
        painelVeiculos.setLayout(new BorderLayout());
        atualizarTabelaVeiculos();

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novaTelaVeiculo("Adicionar", null, null, null);
            }
        });

        btnAlterar = new JButton("Alterar");
        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = JOptionPane.showInputDialog("Informe a placa do veículo para alterar:");
                if (placa != null && !placa.isEmpty()) {
                    carregarDadosVeiculo(placa, "Alterar");
                }
            }
        });

        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = JOptionPane.showInputDialog("Informe a placa do veículo para excluir:");
                if (placa != null && !placa.isEmpty()) {
                    int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o veículo com a placa: " + placa + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirmacao == JOptionPane.YES_OPTION) {
                        excluirVeiculo(placa);
                    }
                }
            }
        });

        btnRelatorio = new JButton("Emitir Relatório");
        btnRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RelatoriosScreen().telaOpcao.setVisible(true);
            }
        });
        
        btnFinalizar = new JButton("Finalizar Estadia");
        btnFinalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FinalizarScreen(GerenciamentoScreen.this).setVisible(true);
            }
        });

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnRelatorio);
        painelBotoes.add(btnFinalizar);

        // Adicionar componentes à tela
        add(painelVeiculos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    public void atualizarTabelaVeiculos() {
        // Criando a tabela com os veículos cadastrados no banco
        List<String[]> veiculos = listarVeiculos();
        String[] colunas = {"Modelo", "Placa", "Cor", "Data Entrada"};
        String[][] dados = new String[veiculos.size()][4];

        for (int i = 0; i < veiculos.size(); i++) {
            dados[i] = veiculos.get(i);
        }

        tabelaVeiculos = new JTable(dados, colunas);
        painelVeiculos.removeAll();
        painelVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);
        painelVeiculos.revalidate();
        painelVeiculos.repaint();
    }

    private List<String[]> listarVeiculos() {
        List<String[]> veiculos = new ArrayList<>();
        Connection con = ConexaoDB.conectar();
        try {
            String query = "SELECT modelo, placa, cor, data_entrada FROM veiculos";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                veiculos.add(new String[] {
                        rs.getString("modelo"),
                        rs.getString("placa"),
                        rs.getString("cor"),
                        rs.getString("data_entrada")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veiculos;
    }

    private void carregarDadosVeiculo(String placa, String tipo) {
        // Conectar ao banco de dados e buscar os dados do veículo
        Connection con = ConexaoDB.conectar();
        try {
            String query = "SELECT modelo, placa, cor FROM veiculos WHERE placa = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String modelo = rs.getString("modelo");
                String cor = rs.getString("cor");

                novaTelaVeiculo(tipo, placa, modelo, cor);
            } else {
                JOptionPane.showMessageDialog(this, "Veículo não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void novaTelaVeiculo(String tipo, String placa, String modelo, String cor) {
        // Remover a duplicação das variáveis
        JTextField txtModelo = new JTextField(modelo != null ? modelo : "");
        JTextField txtPlaca = new JTextField(placa != null ? placa : "");
        JTextField txtCor = new JTextField(cor != null ? cor : "");

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2));

        painel.add(new JLabel("Modelo:"));
        painel.add(txtModelo);
        painel.add(new JLabel("Placa:"));
        painel.add(txtPlaca);
        painel.add(new JLabel("Cor:"));
        painel.add(txtCor);

        int opcao = JOptionPane.showConfirmDialog(null, painel, tipo + " Veículo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcao == JOptionPane.OK_OPTION) {
            String modeloVeiculo = txtModelo.getText();
            String placaVeiculo = txtPlaca.getText();
            String cor2 = txtCor.getText();

            if (tipo.equals("Adicionar")) {
                adicionarVeiculo(modeloVeiculo, placaVeiculo, cor2);
            } else if (tipo.equals("Alterar")) {
                alterarVeiculo(modeloVeiculo, placaVeiculo, cor2, placa);
            }
        }
    }

    private void adicionarVeiculo(String modelo, String placa, String cor) {
        Connection con = ConexaoDB.conectar();
        try {
            // Verificar se a placa já existe no banco
            String verificaPlacaQuery = "SELECT COUNT(*) FROM veiculos WHERE placa = ?";
            PreparedStatement verificaStmt = con.prepareStatement(verificaPlacaQuery);
            verificaStmt.setString(1, placa);
            ResultSet rs = verificaStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Erro: Já existe um veículo cadastrado com essa placa!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Inserir o veículo caso a placa não exista
            String query = "INSERT INTO veiculos (modelo, placa, cor, data_entrada) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, modelo);
            stmt.setString(2, placa);
            stmt.setString(3, cor);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Veículo adicionado com sucesso.");
            atualizarTabelaVeiculos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void alterarVeiculo(String modelo, String placa, String cor, String placaOriginal) {
        Connection con = ConexaoDB.conectar();
        try {
            String query = "UPDATE veiculos SET modelo = ?, placa = ?, cor = ? WHERE placa = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, modelo);
            stmt.setString(2, placa);
            stmt.setString(3, cor);
            stmt.setString(4, placaOriginal);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Veículo alterado com sucesso.");
            atualizarTabelaVeiculos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void excluirVeiculo(String placa) {
        Connection con = ConexaoDB.conectar();
        try {
            String query = "DELETE FROM veiculos WHERE placa = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, placa);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso.");
            atualizarTabelaVeiculos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
