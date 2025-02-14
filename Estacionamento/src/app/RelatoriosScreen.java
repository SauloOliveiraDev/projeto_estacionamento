//Equipe: Saulo de Oliveira, Igor Vasconcelos, Leonardo Santos, Paulo Medeiros, Ravi Mariane
package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class RelatoriosScreen {
    private JButton btnRelatorioDiario, btnRelatorioMensal;
    JFrame telaOpcao = new JFrame();
    public RelatoriosScreen() {
    	telaOpcao.setTitle("Relatórios - Wallpark");
    	telaOpcao.setSize(400, 200);
    	telaOpcao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	telaOpcao.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        btnRelatorioDiario = new JButton("Relatório Diário");
        btnRelatorioMensal = new JButton("Relatório Mensal");

        btnRelatorioDiario.addActionListener(e -> emitirRelatorio("DIARIO"));
        btnRelatorioMensal.addActionListener(e -> emitirRelatorio("MENSAL"));

        panel.add(btnRelatorioDiario);
        panel.add(btnRelatorioMensal);

        telaOpcao.add(panel);
    }

    private void emitirRelatorio(String tipo) {
        Connection con = ConexaoDB.conectar();
        try {
            String filtroData;
            
            if (tipo.equals("DIARIO")) {
                filtroData = "2025-02-13%"; // Data fixa para o exemplo
            } else {
                // Solicitar mês e ano ao usuário
                String mesAno = JOptionPane.showInputDialog(
                    telaOpcao, 
                    "Digite o mês e o ano no formato MM-AAAA:", 
                    "Selecionar Mês do Relatório", 
                    JOptionPane.QUESTION_MESSAGE
                );

                // Validar entrada
                if (mesAno == null || !mesAno.matches("\\d{2}-\\d{4}")) {
                    JOptionPane.showMessageDialog(
                        telaOpcao, 
                        "Formato inválido! Use MM-AAAA.", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Formatar para a consulta SQL
                String[] partes = mesAno.split("-");
                filtroData = partes[1] + "-" + partes[0] + "%"; // Formato: YYYY-MM-%
            }

            String query = "SELECT modelo, placa, data_entrada FROM veiculos WHERE data_entrada LIKE ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, filtroData);
            ResultSet rs = stmt.executeQuery();

            // Criar a tabela de resultados
            Vector<String> colunas = new Vector<>();
            colunas.add("Modelo");
            colunas.add("Placa");
            colunas.add("Data Entrada");

            Vector<Vector<String>> dados = new Vector<>();

            while (rs.next()) {
                Vector<String> linha = new Vector<>();
                linha.add(rs.getString("modelo"));
                linha.add(rs.getString("placa"));
                linha.add(rs.getString("data_entrada"));
                dados.add(linha);
            }

            // Criar e exibir a nova tela
            JFrame relatorioFrame = new JFrame("Relatório " + tipo);
            relatorioFrame.setSize(500, 300);
            relatorioFrame.setLocationRelativeTo(null);

            JTable tabela = new JTable(new DefaultTableModel(dados, colunas));
            JScrollPane scrollPane = new JScrollPane(tabela);
            relatorioFrame.add(scrollPane);

            relatorioFrame.setVisible(true);
            telaOpcao.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
