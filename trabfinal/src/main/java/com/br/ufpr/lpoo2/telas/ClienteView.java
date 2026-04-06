package com.br.ufpr.lpoo2.telas;

import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.repository.ClienteRepository;
import com.br.ufpr.lpoo2.tablemodel.ClienteTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteView extends JFrame {
    private JTable tabela;
    private ClienteTableModel model;
    private JButton btnExcluir = new JButton("Excluir");
    private JPanel painelBusca = new JPanel();
    private JTextField txtBusca = new JTextField(15);
    private String[] opcoes = {"nome", "sobrenome", "rg", "cpf"};
    private JComboBox<String> comboFiltro = new JComboBox<>(opcoes);
    private JButton btnBuscar = new JButton("Buscar");



    public ClienteView() {
        try {
            model = new ClienteTableModel(ClienteRepository.listarClientes());
            tabela = new JTable(model);
            painelBusca.add(new JLabel("Filtrar por:"));
            painelBusca.add(comboFiltro);
            painelBusca.add(txtBusca);
            painelBusca.add(btnBuscar);

            setLayout(new BorderLayout());
            add(new JScrollPane(tabela), BorderLayout.CENTER);
            add(btnExcluir, BorderLayout.SOUTH);
            add(painelBusca, BorderLayout.NORTH);
            
            btnExcluir.addActionListener(e -> {
                try {
                    acaoExcluir();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
            btnBuscar.addActionListener(e -> {
                try {
                    String termo = txtBusca.getText();
                    String filtro = (String) comboFiltro.getSelectedItem();

                // 1. Busca no banco
                    List<Cliente> resultado = ClienteRepository.buscarClientes(termo, filtro);

                // 2. Atualiza o TableModel (O "coração" da tabela)
                model.setDados(resultado);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro na busca: " + ex.getMessage());
            }
        });
    }

    private void acaoExcluir() throws SQLException {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            Cliente selecionado = model.getClienteAt(linha);
            ClienteRepository.excluirCliente(selecionado.getId());
            model.setDados(ClienteRepository.listarClientes());
        }
    }
}
