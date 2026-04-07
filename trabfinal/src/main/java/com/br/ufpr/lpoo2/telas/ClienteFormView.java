package com.br.ufpr.lpoo2.telas;

import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.Endereco;
import com.br.ufpr.lpoo2.repository.ClienteRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ClienteFormView extends JDialog {
    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtRg;
    private JTextField txtCpf;
    private JTextField txtSalario;
    private JTextField txtRua;
    private JTextField txtCep;
    private JTextField txtComplemento;
    private JTextField txtEstado;
    private JTextField txtPais;
    private JTextField txtNumero;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private Cliente cliente;
    private boolean salvo = false;

    public ClienteFormView(Frame owner, String title, boolean modal, Cliente cliente) {
        super(owner, title, modal);
        this.cliente = cliente;
        initComponents();
        if (cliente != null) {
            preencherFormulario(cliente);
        }
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
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Sobrenome:"), gbc);
        gbc.gridx = 1;
        txtSobrenome = new JTextField(20);
        add(txtSobrenome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("RG:"), gbc);
        gbc.gridx = 1;
        txtRg = new JTextField(20);
        add(txtRg, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        txtCpf = new JTextField(20);
        add(txtCpf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Salário:"), gbc);
        gbc.gridx = 1;
        txtSalario = new JTextField(20);
        add(txtSalario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Rua:"), gbc);
        gbc.gridx = 1;
        txtRua = new JTextField(20);
        add(txtRua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        txtCep = new JTextField(20);
        add(txtCep, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 1;
        txtComplemento = new JTextField(20);
        add(txtComplemento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        txtEstado = new JTextField(20);
        add(txtEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("País:"), gbc);
        gbc.gridx = 1;
        txtPais = new JTextField(20);
        add(txtPais, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        txtNumero = new JTextField(20);
        add(txtNumero, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> acaoSalvar());
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private void preencherFormulario(Cliente c) {
        txtNome.setText(c.getNome());
        txtSobrenome.setText(c.getSobrenome());
        txtRg.setText(c.getRg());
        txtCpf.setText(c.getCpf());
        txtSalario.setText(String.valueOf(c.getSalario()));
        if (c.getEndereco() != null) {
            txtRua.setText(c.getEndereco().getRua());
            txtCep.setText(c.getEndereco().getCep());
            txtComplemento.setText(c.getEndereco().getComplemento());
            txtEstado.setText(c.getEndereco().getEstado());
            txtPais.setText(c.getEndereco().getPais());
            txtNumero.setText(String.valueOf(c.getEndereco().getNumero()));
        }
    }

    private void acaoSalvar() {
        try {
            // Input validation for required fields
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Nome' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtSobrenome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Sobrenome' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtRg.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'RG' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtCpf.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'CPF' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // CPF format validation
            String cpf = txtCpf.getText().trim();
            if (!cpf.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "O CPF deve conter exatamente 11 dígitos numéricos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // RG format validation (up to 9 digits)
            String rg = txtRg.getText().trim();
            if (!rg.matches("\\d{1,9}")) {
                JOptionPane.showMessageDialog(this, "O RG deve conter até 9 dígitos numéricos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (txtRua.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Rua' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtCep.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'CEP' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtEstado.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Estado' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtPais.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'País' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtNumero.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Número' do endereço é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }


            double salario;
            int numero;
            try {
                salario = Double.parseDouble(txtSalario.getText());
                numero = Integer.parseInt(txtNumero.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário e Número devem ser valores numéricos válidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Endereco endereco = new Endereco();
            endereco.setRua(txtRua.getText());
            endereco.setCep(txtCep.getText());
            endereco.setComplemento(txtComplemento.getText());
            endereco.setEstado(txtEstado.getText());
            endereco.setPais(txtPais.getText());
            endereco.setNumero(numero);

            if (cliente == null) {
                cliente = new Cliente();
            }
            cliente.setNome(txtNome.getText());
            cliente.setSobrenome(txtSobrenome.getText());
            cliente.setRg(txtRg.getText());
            cliente.setCpf(cpf); // Use validated CPF
            cliente.setSalario(salario);
            cliente.setEndereco(endereco);

            if (cliente.getId() == 0) {
                ClienteRepository.inserirCliente(cliente);
            } else {
                ClienteRepository.atualizarCliente(cliente);
            }

            salvo = true;
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() {
        return salvo;
    }
}
