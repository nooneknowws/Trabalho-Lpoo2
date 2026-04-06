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
        String sqlEndereco = "INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtEnd = conn.prepareStatement(sqlEndereco, Statement.RETURN_GENERATED_KEYS)) {
                Endereco end = cliente.getEndereco();
                pstmtEnd.setString(1, end.getRua());
                pstmtEnd.setString(2, end.getCEP());
                pstmtEnd.setString(3, end.getComplemento());
                pstmtEnd.setString(4, end.getEstado());
                pstmtEnd.setString(5, end.getPais());
                pstmtEnd.setInt(6, end.getNumero());
                pstmtEnd.executeUpdate();

                int idEnderecoGerado = 0;
                try (ResultSet rsKeys = pstmtEnd.getGeneratedKeys()) {
                    if (rsKeys.next()) idEnderecoGerado = rsKeys.getInt(1);
                }

                try (PreparedStatement pstmtCli = conn.prepareStatement(sqlCliente)) {
                    pstmtCli.setString(1, cliente.getNome());
                    pstmtCli.setString(2, cliente.getSobrenome());
                    pstmtCli.setString(3, cliente.getRg());
                    pstmtCli.setString(4, cliente.getCpf());
                    pstmtCli.setDouble(5, cliente.getSalario()); // Conversão Double -> Numeric automática
                    pstmtCli.setInt(6, idEnderecoGerado);
                    pstmtCli.executeUpdate();
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

    // Listar todos
    public static List<Cliente> listarClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero " +
                "FROM Clientes c " +
                "JOIN Endereco e ON c.idEndereco = e.id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Endereco end = new Endereco(
                        rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                        rs.getString("estado"), rs.getString("pais"), rs.getInt("numero")
                );

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

    // Apagar cliente - Cascade manual para Contas
    public static void excluirCliente(int idCliente) throws SQLException {

        String sqlDeleteContas = "DELETE FROM Contas WHERE idCliente = ?";
        String sqlDeleteCliente = "DELETE FROM Clientes WHERE id = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlDeleteContas);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlDeleteCliente)) {

                pstmt1.setInt(1, idCliente);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, idCliente);
                pstmt2.executeUpdate();

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
    // 4. Listar com filtros e ordens específicas
    // Ordem: Nome e Sobrenome (ASC), Salário (DESC)
    public static List<Cliente> listarClientesOrdenados() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero " +
                "FROM Clientes c " +
                "JOIN endereco e ON c.idEndereco = e.idEndereco " +
                "ORDER BY c.nome ASC, c.sobrenome ASC, c.salario DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Endereco end = new Endereco(
                        rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                        rs.getString("estado"), rs.getString("pais"), rs.getInt("numero")
                );

                Cliente cli = new Cliente(
                        rs.getString("nome"), rs.getString("sobrenome"),
                        rs.getString("rg"), rs.getString("cpf"), rs.getDouble("salario")
                );
                cli.setEndereco(end);
                lista.add(cli);
            }
        }
        return lista;
    }
    public static List<Cliente> buscarClientes(String termo, String tipoFiltro) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        // O tipoFiltro será o nome da coluna no banco (nome, sobrenome, rg ou cpf)
        String sql = "SELECT c.*, e.rua, e.cep, e.complemento, e.estado, e.pais, e.numero " +
                "FROM Clientes c " +
                "JOIN Endereco e ON c.idEndereco = e.id " +
                "WHERE c." + tipoFiltro + " ILIKE ?"; // ILIKE no Postgres ignora maiúsculas/minúsculas

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%"); // O % permite busca por "parte do nome"

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Endereco end = new Endereco(
                            rs.getString("rua"), rs.getString("cep"), rs.getString("complemento"),
                            rs.getString("estado"), rs.getString("pais"), rs.getInt("numero")
                    );
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