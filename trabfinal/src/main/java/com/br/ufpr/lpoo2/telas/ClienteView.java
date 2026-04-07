package com.br.ufpr.lpoo2.telas;

import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.repository.ClienteRepository;
import com.br.ufpr.lpoo2.tablemodel.ClienteTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteView extends JFrame {
    private JTable tabela;
    private ClienteTableModel model;
    private JButton btnExcluir = new JButton("Excluir");
    private JButton btnAdicionar = new JButton("Adicionar");
    private JButton btnAtualizar = new JButton("Atualizar");
    private JButton btnAbrirConta = new JButton("Abrir Conta");
    private JButton btnOperacoesConta = new JButton("Operações de Conta");
    private JPanel painelBusca = new JPanel();
    private JTextField txtBusca = new JTextField(15);
    private String[] opcoesFiltro = {"nome", "sobrenome", "rg", "cpf"};
    private JComboBox<String> comboFiltro = new JComboBox<>(opcoesFiltro);
    private JButton btnBuscar = new JButton("Buscar");

    // New components for sorting
    private JComboBox<String> comboOrdenacao;
    private String[] opcoesOrdenacao = {
            "Nome (A-Z)", "Nome (Z-A)",
            "Sobrenome (A-Z)", "Sobrenome (Z-A)",
            "Salário (Maior-Menor)", "Salário (Menor-Maior)"
    };
    private String currentOrderByColumn = "nome";
    private String currentOrderDirection = "ASC";


    public ClienteView() {
        super("Clientes");
        try {
            // Initialize sorting combo box
            comboOrdenacao = new JComboBox<>(opcoesOrdenacao);
            comboOrdenacao.setSelectedIndex(0); // Default to Nome (A-Z)

            model = new ClienteTableModel(ClienteRepository.listarClientes(currentOrderByColumn, currentOrderDirection));
            tabela = new JTable(model);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Panel for search and add/open account buttons
            JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
            painelSuperior.add(new JLabel("Filtrar por:"));
            painelSuperior.add(comboFiltro);
            painelSuperior.add(txtBusca);
            painelSuperior.add(btnBuscar);
            painelSuperior.add(btnAdicionar);
            painelSuperior.add(btnAbrirConta);
            painelSuperior.add(btnOperacoesConta);

            // Panel for sorting
            JPanel painelOrdenacao = new JPanel(new FlowLayout(FlowLayout.LEFT));
            painelOrdenacao.add(new JLabel("Ordenar por:"));
            painelOrdenacao.add(comboOrdenacao);

            // Combine search/add and sort panels
            JPanel painelNorte = new JPanel(new BorderLayout());
            painelNorte.add(painelSuperior, BorderLayout.CENTER);
            painelNorte.add(painelOrdenacao, BorderLayout.SOUTH);


            JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            painelBotoesInferior.add(btnAtualizar);
            painelBotoesInferior.add(btnExcluir);

            setLayout(new BorderLayout());
            add(new JScrollPane(tabela), BorderLayout.CENTER);
            add(painelBotoesInferior, BorderLayout.SOUTH);
            add(painelNorte, BorderLayout.NORTH); // Add the combined panel to NORTH

            btnAtualizar.setEnabled(false);

            // Action Listeners
            btnExcluir.addActionListener(e -> {
                try {
                    acaoExcluir();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnBuscar.addActionListener(e -> {
                try {
                    String termo = txtBusca.getText();
                    String filtro = (String) comboFiltro.getSelectedItem();
                    List<Cliente> resultado = ClienteRepository.buscarClientes(termo, filtro);
                    model.setDados(resultado);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro na busca: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnAdicionar.addActionListener(e -> {
                acaoAdicionar();
            });

            btnAtualizar.addActionListener(e -> {
                try {
                    acaoAtualizar();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao carregar cliente para atualização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnAbrirConta.addActionListener(e -> {
                acaoAbrirConta();
            });

            btnOperacoesConta.addActionListener(e -> {
                acaoOperacoesConta();
            });

            comboOrdenacao.addActionListener(e -> {
                acaoOrdenarClientes();
            });

            tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        btnAtualizar.setEnabled(tabela.getSelectedRow() != -1);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao iniciar a aplicação: " + e.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void acaoExcluir() throws SQLException {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            Cliente selecionado = model.getClienteAt(linha);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente " + selecionado.getNome() + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ClienteRepository.excluirCliente(selecionado.getId());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoAdicionar() {
        ClienteFormView form = new ClienteFormView(this, "Adicionar Cliente", true, null);
        form.setVisible(true);
        if (form.isSalvo()) {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
        }
    }

    private void acaoAtualizar() throws SQLException {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            Cliente clienteParaAtualizar = model.getClienteAt(linha);
            ClienteFormView form = new ClienteFormView(this, "Atualizar Cliente", true, clienteParaAtualizar);
            form.setVisible(true);
            if (form.isSalvo()) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoAbrirConta() {
        ContaCadastroView contaView = new ContaCadastroView(this, "Abrir Nova Conta", true);
        contaView.setVisible(true);
    }

    private void acaoOperacoesConta() {
        ContaOperacoesView operacoesView = new ContaOperacoesView(this, "Operações de Conta", true);
        operacoesView.setVisible(true);
    }

    private void acaoOrdenarClientes() {
        String selectedOption = (String) comboOrdenacao.getSelectedItem();
        switch (selectedOption) {
            case "Nome (A-Z)":
                currentOrderByColumn = "nome";
                currentOrderDirection = "ASC";
                break;
            case "Nome (Z-A)":
                currentOrderByColumn = "nome";
                currentOrderDirection = "DESC";
                break;
            case "Sobrenome (A-Z)":
                currentOrderByColumn = "sobrenome";
                currentOrderDirection = "ASC";
                break;
            case "Sobrenome (Z-A)":
                currentOrderByColumn = "sobrenome";
                currentOrderDirection = "DESC";
                break;
            case "Salário (Maior-Menor)":
                currentOrderByColumn = "salario";
                currentOrderDirection = "DESC";
                break;
            case "Salário (Menor-Maior)":
                currentOrderByColumn = "salario";
                currentOrderDirection = "ASC";
                break;
        }
        refreshTable();
    }

    private void refreshTable() {
        try {
            // If there's a search term, apply search, otherwise apply sorting
            if (!txtBusca.getText().trim().isEmpty()) {
                String termo = txtBusca.getText();
                String filtro = (String) comboFiltro.getSelectedItem();
                model.setDados(ClienteRepository.buscarClientes(termo, filtro));
            } else {
                model.setDados(ClienteRepository.listarClientes(currentOrderByColumn, currentOrderDirection));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao recarregar a lista de clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
