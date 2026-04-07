package com.br.ufpr.lpoo2.telas;

import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.ContaCorrente;
import com.br.ufpr.lpoo2.models.ContaInvestimento;
import com.br.ufpr.lpoo2.repository.ClienteRepository;
import com.br.ufpr.lpoo2.repository.ContaRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContaCadastroView extends JDialog {
    private JComboBox<Cliente> cbClientes;
    private JComboBox<String> cbTipoConta;
    private JTextField txtDepositoInicial;
    private JTextField txtLimite; // For ContaCorrente
    private JTextField txtMontanteMinimo; // For ContaInvestimento
    private JTextField txtDepositoMinimo; // For ContaInvestimento
    private JButton btnSalvar;
    private JButton btnCancelar;

    private JPanel panelContaCorrente;
    private JPanel panelContaInvestimento;

    public ContaCadastroView(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
        loadClientes();
        setupListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cbClientes = new JComboBox<>();
        add(cbClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Tipo de Conta:"), gbc);
        gbc.gridx = 1;
        cbTipoConta = new JComboBox<>(new String[]{"Corrente", "Investimento"});
        add(cbTipoConta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Depósito Inicial:"), gbc);
        gbc.gridx = 1;
        txtDepositoInicial = new JTextField(15);
        add(txtDepositoInicial, gbc);

        panelContaCorrente = new JPanel(new GridBagLayout());
        panelContaCorrente.setBorder(BorderFactory.createTitledBorder("Conta Corrente"));
        GridBagConstraints gbcCc = new GridBagConstraints();
        gbcCc.insets = new Insets(2, 2, 2, 2);
        gbcCc.fill = GridBagConstraints.HORIZONTAL;

        gbcCc.gridx = 0;
        gbcCc.gridy = 0;
        panelContaCorrente.add(new JLabel("Limite:"), gbcCc);
        gbcCc.gridx = 1;
        txtLimite = new JTextField(15);
        panelContaCorrente.add(txtLimite, gbcCc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelContaCorrente, gbc);

        panelContaInvestimento = new JPanel(new GridBagLayout());
        panelContaInvestimento.setBorder(BorderFactory.createTitledBorder("Conta Investimento"));
        GridBagConstraints gbcCi = new GridBagConstraints();
        gbcCi.insets = new Insets(2, 2, 2, 2);
        gbcCi.fill = GridBagConstraints.HORIZONTAL;

        gbcCi.gridx = 0;
        gbcCi.gridy = 0;
        panelContaInvestimento.add(new JLabel("Montante Mínimo:"), gbcCi);
        gbcCi.gridx = 1;
        txtMontanteMinimo = new JTextField(15);
        panelContaInvestimento.add(txtMontanteMinimo, gbcCi);

        gbcCi.gridx = 0;
        gbcCi.gridy = 1;
        panelContaInvestimento.add(new JLabel("Depósito Mínimo:"), gbcCi);
        gbcCi.gridx = 1;
        txtDepositoMinimo = new JTextField(15);
        panelContaInvestimento.add(txtDepositoMinimo, gbcCi);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(panelContaInvestimento, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> acaoSalvar());
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        updateAccountTypePanel();
    }

    private void loadClientes() {
        try {
            List<Cliente> clientes = ClienteRepository.listarClientes();
            for (Cliente cliente : clientes) {
                cbClientes.addItem(cliente);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupListeners() {
        cbTipoConta.addActionListener(e -> updateAccountTypePanel());
    }

    private void updateAccountTypePanel() {
        String selectedType = (String) cbTipoConta.getSelectedItem();
        panelContaCorrente.setVisible("Corrente".equals(selectedType));
        panelContaInvestimento.setVisible("Investimento".equals(selectedType));
        pack();
    }

    private void acaoSalvar() {
        Cliente selectedCliente = (Cliente) cbClientes.getSelectedItem();
        if (selectedCliente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipoConta = (String) cbTipoConta.getSelectedItem();
        double depositoInicial;
        
        if (txtDepositoInicial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Depósito Inicial' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            depositoInicial = Double.parseDouble(txtDepositoInicial.getText());
            if (depositoInicial <= 0) {
                JOptionPane.showMessageDialog(this, "Depósito Inicial deve ser um valor positivo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Depósito Inicial inválido. Digite um número.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numeroConta = ContaRepository.gerarProximoNumeroConta();

            if ("Corrente".equals(tipoConta)) {
                double limite;
                if (txtLimite.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O campo 'Limite' é obrigatório para Conta Corrente.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    limite = Double.parseDouble(txtLimite.getText());
                    if (limite < 0) {
                        JOptionPane.showMessageDialog(this, "Limite deve ser um valor não negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Limite inválido. Digite um número.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ContaCorrente cc = new ContaCorrente(selectedCliente, numeroConta, depositoInicial, limite);
                ContaRepository.inserirConta(cc);
            } else if ("Investimento".equals(tipoConta)) {
                double montanteMinimo, depositoMinimo;
                
                if (txtMontanteMinimo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O campo 'Montante Mínimo' é obrigatório para Conta Investimento.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtDepositoMinimo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O campo 'Depósito Mínimo' é obrigatório para Conta Investimento.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    montanteMinimo = Double.parseDouble(txtMontanteMinimo.getText());
                    depositoMinimo = Double.parseDouble(txtDepositoMinimo.getText());
                    if (montanteMinimo < 0) {
                        JOptionPane.showMessageDialog(this, "Montante Mínimo deve ser um valor não negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (depositoMinimo < 0) {
                        JOptionPane.showMessageDialog(this, "Depósito Mínimo deve ser um valor não negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Montante Mínimo ou Depósito Mínimo inválido. Digite números.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ContaInvestimento ci = new ContaInvestimento(selectedCliente, numeroConta, depositoInicial, montanteMinimo, depositoMinimo);
                ContaRepository.inserirConta(ci);
            }
            JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Número da Conta: " + numeroConta, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar conta: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
