package com.br.ufpr.lpoo2.repository;

import com.br.ufpr.lpoo2.connection.ConnectionFactory;
import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.Conta;
import com.br.ufpr.lpoo2.models.ContaCorrente;
import com.br.ufpr.lpoo2.models.ContaInvestimento;
import com.br.ufpr.lpoo2.models.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaRepository {

    private static Connection conn;

    static {
        try {
            conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar: " + e.getMessage());
        }
    }

    public static void inserirConta(Conta conta) throws SQLException {
        String sql = "INSERT INTO Contas (idCliente, tipo, deposito_inical, limite, numero_conta, montante_minimo, deposito_minimo, saldo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, conta.getDono().getId());
            
            if (conta instanceof ContaCorrente) {
                ContaCorrente cc = (ContaCorrente) conta;
                pstmt.setString(2, "Corrente");
                pstmt.setDouble(3, cc.getSaldo());
                pstmt.setDouble(4, cc.getLimite());
                pstmt.setInt(5, cc.getNumero());
                pstmt.setNull(6, Types.NUMERIC);
                pstmt.setNull(7, Types.NUMERIC);
                pstmt.setDouble(8, cc.getSaldo());
            } else if (conta instanceof ContaInvestimento) {
                ContaInvestimento ci = (ContaInvestimento) conta;
                pstmt.setString(2, "Investimento");
                pstmt.setDouble(3, ci.getSaldo());
                pstmt.setNull(4, Types.NUMERIC);
                pstmt.setInt(5, ci.getNumero());
                pstmt.setDouble(6, ci.getMontanteMinimo());
                pstmt.setDouble(7, ci.getDepositoMinimo());
                pstmt.setDouble(8, ci.getSaldo());
            } else {
                throw new IllegalArgumentException("Tipo de conta desconhecido.");
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    conta.setId(rs.getInt(1));
                }
            }
        }
    }

    public static Conta buscarContaPorNumero(int numeroConta) throws SQLException {
        String sql = "SELECT c.*, cli.id as cliente_id, cli.nome, cli.sobrenome, cli.rg, cli.cpf, cli.salario, " +
                     "e.id as endereco_id, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero as endereco_numero " +
                     "FROM Contas c " +
                     "JOIN Clientes cli ON c.idCliente = cli.id " +
                     "JOIN Endereco e ON cli.idEndereco = e.id " +
                     "WHERE c.numero_conta = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numeroConta);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Endereco endereco = new Endereco(
                        rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                        rs.getString("estado"), rs.getString("pais"), rs.getInt("endereco_numero")
                    );
                    endereco.setId(rs.getInt("endereco_id"));

                    Cliente cliente = new Cliente(
                        rs.getString("nome"), rs.getString("sobrenome"),
                        rs.getString("rg"), rs.getString("cpf"), rs.getDouble("salario"), endereco
                    );
                    cliente.setId(rs.getInt("cliente_id"));

                    String tipo = rs.getString("tipo");
                    Conta conta;

                    if ("Corrente".equals(tipo)) {
                        ContaCorrente cc = new ContaCorrente(
                            cliente, rs.getInt("numero_conta"), rs.getDouble("saldo"), rs.getDouble("limite")
                        );
                        cc.setId(rs.getInt("id"));
                        conta = cc;
                    } else if ("Investimento".equals(tipo)) {
                        ContaInvestimento ci = new ContaInvestimento(
                            cliente, rs.getInt("numero_conta"), rs.getDouble("saldo"), 
                            rs.getDouble("montante_minimo"), rs.getDouble("deposito_minimo")
                        );
                        ci.setId(rs.getInt("id"));
                        conta = ci;
                    } else {
                        throw new SQLException("Tipo de conta desconhecido no banco de dados: " + tipo);
                    }
                    return conta;
                }
            }
        }
        return null;
    }

    public static List<Conta> listarContasPorCliente(int idCliente) throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT c.*, cli.id as cliente_id, cli.nome, cli.sobrenome, cli.rg, cli.cpf, cli.salario, " +
                     "e.id as endereco_id, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero as endereco_numero " +
                     "FROM Contas c " +
                     "JOIN Clientes cli ON c.idCliente = cli.id " +
                     "JOIN Endereco e ON cli.idEndereco = e.id " +
                     "WHERE c.idCliente = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = new Endereco(
                        rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                        rs.getString("estado"), rs.getString("pais"), rs.getInt("endereco_numero")
                    );
                    endereco.setId(rs.getInt("endereco_id"));

                    Cliente cliente = new Cliente(
                        rs.getString("nome"), rs.getString("sobrenome"),
                        rs.getString("rg"), rs.getString("cpf"), rs.getDouble("salario"), endereco
                    );
                    cliente.setId(rs.getInt("cliente_id"));

                    String tipo = rs.getString("tipo");
                    Conta conta;

                    if ("Corrente".equals(tipo)) {
                        ContaCorrente cc = new ContaCorrente(
                            cliente, rs.getInt("numero_conta"), rs.getDouble("saldo"), rs.getDouble("limite")
                        );
                        cc.setId(rs.getInt("id"));
                        conta = cc;
                    } else if ("Investimento".equals(tipo)) {
                        ContaInvestimento ci = new ContaInvestimento(
                            cliente, rs.getInt("numero_conta"), rs.getDouble("saldo"), 
                            rs.getDouble("montante_minimo"), rs.getDouble("deposito_minimo")
                        );
                        ci.setId(rs.getInt("id"));
                        conta = ci;
                    } else {
                        throw new SQLException("Tipo de conta desconhecido no banco de dados: " + tipo);
                    }
                    contas.add(conta);
                }
            }
        }
        return contas;
    }

    public static void atualizarSaldo(Conta conta) throws SQLException {
        String sql = "UPDATE Contas SET saldo = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, conta.getSaldo());
            pstmt.setInt(2, conta.getId());
            pstmt.executeUpdate();
        }
    }

    public static void excluirConta(int idConta) throws SQLException {
        String sql = "DELETE FROM Contas WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idConta);
            pstmt.executeUpdate();
        }
    }

    public static int gerarProximoNumeroConta() throws SQLException {
        String sql = "SELECT MAX(numero_conta) FROM Contas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxNumero = rs.getInt(1);
                return maxNumero + 1;
            }
        }
        return 100001;
    }
}
