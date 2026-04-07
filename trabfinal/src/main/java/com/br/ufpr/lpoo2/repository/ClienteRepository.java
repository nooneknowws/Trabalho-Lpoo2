package com.br.ufpr.lpoo2.repository;

import com.br.ufpr.lpoo2.connection.ConnectionFactory;
import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private static Connection conn;

    static {
        try {
            conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar: " + e.getMessage());
        }
    }

    // Inserir cliente
    public static void inserirCliente(Cliente cliente) throws SQLException {
        String sqlEndereco = "INSERT INTO endereco (rua, cep, complemento, estado, pais, numero) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtEnd = conn.prepareStatement(sqlEndereco, Statement.RETURN_GENERATED_KEYS)) {
                Endereco end = cliente.getEndereco();
                pstmtEnd.setString(1, end.getRua());
                pstmtEnd.setString(2, end.getCep());
                pstmtEnd.setString(3, end.getComplemento());
                pstmtEnd.setString(4, end.getEstado());
                pstmtEnd.setString(5, end.getPais());
                pstmtEnd.setInt(6, end.getNumero());
                pstmtEnd.executeUpdate();

                int idEnderecoGerado = 0;
                try (ResultSet rsKeys = pstmtEnd.getGeneratedKeys()) {
                    if (rsKeys.next()) idEnderecoGerado = rsKeys.getInt(1);
                }
                cliente.getEndereco().setId(idEnderecoGerado);

                try (PreparedStatement pstmtCli = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                    pstmtCli.setString(1, cliente.getNome());
                    pstmtCli.setString(2, cliente.getSobrenome());
                    pstmtCli.setString(3, cliente.getRg());
                    pstmtCli.setString(4, cliente.getCpf());
                    pstmtCli.setDouble(5, cliente.getSalario());
                    pstmtCli.setInt(6, idEnderecoGerado);
                    pstmtCli.executeUpdate();

                    int idClienteGerado = 0;
                    try (ResultSet rsKeys = pstmtCli.getGeneratedKeys()) {
                        if (rsKeys.next()) idClienteGerado = rsKeys.getInt(1);
                    }
                    cliente.setId(idClienteGerado);
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    // Atualizar cliente
    public static void atualizarCliente(Cliente cliente) throws SQLException {
        String sqlUpdateEndereco = "UPDATE endereco SET rua = ?, cep = ?, complemento = ?, estado = ?, pais = ?, numero = ? WHERE id = ?";
        String sqlUpdateCliente = "UPDATE clientes SET nome = ?, sobrenome = ?, rg = ?, cpf = ?, salario = ? WHERE id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtEnd = conn.prepareStatement(sqlUpdateEndereco)) {
                Endereco end = cliente.getEndereco();
                pstmtEnd.setString(1, end.getRua());
                pstmtEnd.setString(2, end.getCep());
                pstmtEnd.setString(3, end.getComplemento());
                pstmtEnd.setString(4, end.getEstado());
                pstmtEnd.setString(5, end.getPais());
                pstmtEnd.setInt(6, end.getNumero());
                pstmtEnd.setInt(7, end.getId()); // WHERE clause for Endereco
                pstmtEnd.executeUpdate();
            }

            try (PreparedStatement pstmtCli = conn.prepareStatement(sqlUpdateCliente)) {
                pstmtCli.setString(1, cliente.getNome());
                pstmtCli.setString(2, cliente.getSobrenome());
                pstmtCli.setString(3, cliente.getRg());
                pstmtCli.setString(4, cliente.getCpf());
                pstmtCli.setDouble(5, cliente.getSalario());
                pstmtCli.setInt(6, cliente.getId()); // WHERE clause for Cliente
                pstmtCli.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Listar todos (default sorting)
    public static List<Cliente> listarClientes() throws SQLException {
        return listarClientes("nome", "ASC"); // Default sort by name ascending
    }

    // Listar com filtros e ordens específicas
    public static List<Cliente> listarClientes(String orderByColumn, String orderDirection) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero, e.id as endereco_id " +
                "FROM clientes c " +
                "JOIN endereco e ON c.idEndereco = e.id";

        // Add ORDER BY clause dynamically
        if (orderByColumn != null && !orderByColumn.trim().isEmpty()) {
            sql += " ORDER BY c." + orderByColumn;
            if (orderDirection != null && (orderDirection.equalsIgnoreCase("ASC") || orderDirection.equalsIgnoreCase("DESC"))) {
                sql += " " + orderDirection;
            }
            // For salary, add secondary sort by name for consistency
            if (orderByColumn.equalsIgnoreCase("salario")) {
                sql += ", c.nome ASC, c.sobrenome ASC";
            }
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Endereco end = new Endereco(
                        rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                        rs.getString("estado"), rs.getString("pais"), rs.getInt("numero")
                );
                end.setId(rs.getInt("endereco_id")); // Set Endereco ID

                Cliente cli = new Cliente(
                        rs.getString("nome"), rs.getString("sobrenome"),
                        rs.getString("rg"), rs.getString("cpf"), rs.getDouble("salario")
                );
                cli.setId(rs.getInt("id"));
                cli.setEndereco(end);
                lista.add(cli);
            }
        }
        return lista;
    }

    public static void excluirCliente(int idCliente) throws SQLException {

        String sqlDeleteContas = "DELETE FROM contas WHERE idCliente = ?";
        String sqlDeleteCliente = "DELETE FROM clientes WHERE id = ?";
        String sqlSelectEnderecoId = "SELECT idEndereco FROM clientes WHERE id = ?";
        String sqlDeleteEndereco = "DELETE FROM endereco WHERE id = ?";


        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlDeleteContas);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlDeleteCliente);
                 PreparedStatement pstmt3 = conn.prepareStatement(sqlSelectEnderecoId);
                 PreparedStatement pstmt4 = conn.prepareStatement(sqlDeleteEndereco)) {

                pstmt1.setInt(1, idCliente);
                pstmt1.executeUpdate();

                pstmt3.setInt(1, idCliente);
                int idEndereco = 0;
                try (ResultSet rs = pstmt3.executeQuery()) {
                    if (rs.next()) {
                        idEndereco = rs.getInt("idEndereco");
                    }
                }

                pstmt2.setInt(1, idCliente);
                pstmt2.executeUpdate();

                if (idEndereco != 0) {
                    pstmt4.setInt(1, idEndereco);
                    pstmt4.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static List<Cliente> buscarClientes(String termo, String tipoFiltro) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero, e.id as endereco_id " +
                "FROM Clientes c " +
                "JOIN Endereco e ON c.idEndereco = e.id " +
                "WHERE c." + tipoFiltro + " ILIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Endereco end = new Endereco(
                            rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                            rs.getString("estado"), rs.getString("pais"), rs.getInt("numero")
                    );
                    end.setId(rs.getInt("endereco_id"));

                    Cliente cli = new Cliente(
                            rs.getString("nome"), rs.getString("sobrenome"),
                            rs.getString("rg"), rs.getString("cpf"), rs.getDouble("salario"), end
                    );
                    cli.setId(rs.getInt("id"));
                    lista.add(cli);
                }
            }
        }
        return lista;
    }
}
