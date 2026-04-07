package com.br.ufpr.lpoo2.telas;

import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.Conta;
import com.br.ufpr.lpoo2.models.ContaCorrente;
import com.br.ufpr.lpoo2.models.ContaInvestimento;
import com.br.ufpr.lpoo2.repository.ClienteRepository;
import com.br.ufpr.lpoo2.repository.ContaRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContaOperacoesView extends JDialog {
    private JTextField txtCpfBusca;
    private JButton btnBuscarCliente;
    private JComboBox<Conta> cbContasCliente;
    private JButton btnVerSaldo;
    private JButton btnDepositar;
    private JButton btnSacar;
    private JButton btnRemunerar;
    private JLabel lblSaldoAtual;

    private Cliente clienteSelecionado;
    private Conta contaSelecionada;

    public ContaOperacoesView(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
        setupListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBuscaCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscaCliente.setBorder(BorderFactory.createTitledBorder("Buscar Cliente por CPF"));
        txtCpfBusca = new JTextField(15);
        btnBuscarCliente = new JButton("Buscar");
        panelBuscaCliente.add(new JLabel("CPF:"));
        panelBuscaCliente.add(txtCpfBusca);
        panelBuscaCliente.add(btnBuscarCliente);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(panelBuscaCliente, gbc);

        JPanel panelSelecaoConta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSelecaoConta.setBorder(BorderFactory.createTitledBorder("Selecionar Conta"));
        cbContasCliente = new JComboBox<>();
        cbContasCliente.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        panelSelecaoConta.add(new JLabel("Contas do Cliente:"));
        panelSelecaoConta.add(cbContasCliente);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(panelSelecaoConta, gbc);

        JPanel panelOperacoes = new JPanel(new GridLayout(2, 2, 10, 10));
        panelOperacoes.setBorder(BorderFactory.createTitledBorder("Operações da Conta"));
        btnVerSaldo = new JButton("Ver Saldo");
        btnDepositar = new JButton("Depositar");
        btnSacar = new JButton("Sacar");
        btnRemunerar = new JButton("Remunerar");

        panelOperacoes.add(btnVerSaldo);
        panelOperacoes.add(btnDepositar);
        panelOperacoes.add(btnSacar);
        panelOperacoes.add(btnRemunerar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(panelOperacoes, gbc);

        lblSaldoAtual = new JLabel("Saldo Atual: R$ 0.00");
        lblSaldoAtual.setFont(new Font("Serif", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblSaldoAtual, gbc);

        setOperationButtonsEnabled(false);
    }

    private void setupListeners() {
        btnBuscarCliente.addActionListener(e -> acaoBuscarCliente());
        cbContasCliente.addActionListener(e -> acaoSelecionarConta());
        btnVerSaldo.addActionListener(e -> acaoVerSaldo());
        btnDepositar.addActionListener(e -> acaoDepositar());
        btnSacar.addActionListener(e -> acaoSacar());
        btnRemunerar.addActionListener(e -> acaoRemunerar());
    }

    private void setOperationButtonsEnabled(boolean enabled) {
        btnVerSaldo.setEnabled(enabled);
        btnDepositar.setEnabled(enabled);
        btnSacar.setEnabled(enabled);
        btnRemunerar.setEnabled(enabled);
    }

    private void acaoBuscarCliente() {
        String cpf = txtCpfBusca.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite o CPF do cliente.", "Erro de Busca", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Cliente> clientes = ClienteRepository.buscarClientes(cpf, "cpf");
            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado com o CPF: " + cpf, "Cliente Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
                clienteSelecionado = null;
                cbContasCliente.removeAllItems();
                setOperationButtonsEnabled(false);
                lblSaldoAtual.setText("Saldo Atual: R$ 0.00");
            } else {
                clienteSelecionado = clientes.get(0); // Assuming CPF is unique, take the first one
                JOptionPane.showMessageDialog(this, "Cliente encontrado: " + clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome(), "Cliente Encontrado", JOptionPane.INFORMATION_MESSAGE);
                loadContasCliente(clienteSelecionado.getId());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContasCliente(int idCliente) {
        cbContasCliente.removeAllItems();
        try {
            List<Conta> contas = ContaRepository.listarContasPorCliente(idCliente);
            if (contas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma conta encontrada para este cliente.", "Contas", JOptionPane.INFORMATION_MESSAGE);
                setOperationButtonsEnabled(false);
                lblSaldoAtual.setText("Saldo Atual: R$ 0.00");
            } else {
                for (Conta conta : contas) {
                    cbContasCliente.addItem(conta);
                }
                cbContasCliente.setSelectedIndex(0);
                setOperationButtonsEnabled(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar contas do cliente: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoSelecionarConta() {
        contaSelecionada = (Conta) cbContasCliente.getSelectedItem();
        if (contaSelecionada != null) {
            acaoVerSaldo();
            setOperationButtonsEnabled(true);
        } else {
            lblSaldoAtual.setText("Saldo Atual: R$ 0.00");
            setOperationButtonsEnabled(false);
        }
    }

    private void acaoVerSaldo() {
        if (contaSelecionada != null) {
            lblSaldoAtual.setText(String.format("Saldo Atual: R$ %.2f", contaSelecionada.getSaldo()));
        }
    }

    private void acaoDepositar() {
        if (contaSelecionada == null) return;

        String valorStr = JOptionPane.showInputDialog(this, "Digite o valor para depósito:");
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) { // Basic validation moved here
                    JOptionPane.showMessageDialog(this, "O valor a ser depositado deve ser positivo.", "Erro de Depósito", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (contaSelecionada.deposita(valor)) {
                    ContaRepository.atualizarSaldo(contaSelecionada);
                    acaoVerSaldo();
                    JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Specific error message for ContaInvestimento
                    if (contaSelecionada instanceof ContaInvestimento) {
                        ContaInvestimento ci = (ContaInvestimento) contaSelecionada;
                        JOptionPane.showMessageDialog(this, "Falha ao realizar depósito. O valor (" + String.format("%.2f", valor) + ") é menor que o depósito mínimo (" + String.format("%.2f", ci.getDepositoMinimo()) + ").", "Erro de Depósito", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Falha ao realizar depósito. Verifique o valor.", "Erro de Depósito", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar saldo no banco: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void acaoSacar() {
        if (contaSelecionada == null) return;

        String valorStr = JOptionPane.showInputDialog(this, "Digite o valor para saque:");
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) { // Basic validation moved here
                    JOptionPane.showMessageDialog(this, "O valor a ser sacado deve ser positivo.", "Erro de Saque", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (contaSelecionada.saca(valor)) {
                    ContaRepository.atualizarSaldo(contaSelecionada);
                    acaoVerSaldo();
                    JOptionPane.showMessageDialog(this, "Saque realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Specific error messages for different account types
                    if (contaSelecionada instanceof ContaCorrente) {
                        ContaCorrente cc = (ContaCorrente) contaSelecionada;
                        JOptionPane.showMessageDialog(this, "Falha ao realizar saque. O saque excede o limite da conta. Saldo atual: " + String.format("%.2f", cc.getSaldo()) + ", Limite: " + String.format("%.2f", cc.getLimite()) + ".", "Erro de Saque", JOptionPane.ERROR_MESSAGE);
                    } else if (contaSelecionada instanceof ContaInvestimento) {
                        ContaInvestimento ci = (ContaInvestimento) contaSelecionada;
                        JOptionPane.showMessageDialog(this, "Falha ao realizar saque. O saque excede o montante mínimo da conta. Saldo atual: " + String.format("%.2f", ci.getSaldo()) + ", Montante Mínimo: " + String.format("%.2f", ci.getMontanteMinimo()) + ".", "Erro de Saque", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Falha ao realizar saque. Verifique o valor e o limite/montante mínimo.", "Erro de Saque", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar saldo no banco: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void acaoRemunerar() {
        if (contaSelecionada == null) return;

        contaSelecionada.remunera();
        try {
            ContaRepository.atualizarSaldo(contaSelecionada);
            acaoVerSaldo();
            JOptionPane.showMessageDialog(this, "Remuneração aplicada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao aplicar remuneração no banco: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
