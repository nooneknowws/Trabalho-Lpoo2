package com.br.ufpr.lpoo2.repository;
import com.br.ufpr.lpoo2.connection.ConnectionFactory;
import com.br.ufpr.lpoo2.models.Cliente;
import com.br.ufpr.lpoo2.models.Endereco;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    static Connection conn;

    static {
        try {
            conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Statement stmt;

    static {
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static ResultSet rs;

    //inserir cliente
    public static Cliente inserirCliente(String nome, String sobrenome, String rg, String cpf, Number salario, Endereco endereco) throws SQLException{

        Cliente enderecoCliente = new Cliente(endereco.rua, endereco.CEP, endereco.complemento, endereco.estado, endereco.pais, endereco.numero);
        rs = stmt.executeQuery("insert into endereco(rua, cep, complemento, estado, pais, numero) values(enderecoCliente.rua, enderecoCliente.cep, enderecoCliente.complemento, enderecoCliente.estado, enderecoCliente.pais, enderecoCliente.numero)");

        Cliente cliente = new Cliente(nome, sobrenome, rg, cpf, salario);
        rs = stmt.executeQuery(
                "insert into Clientes(nome, sobrenome, rg, cpf, salario, idendereco)+" +
                        "values (cliente.nome, cliente.sobrenome, cliente.rg, cliente.cpf, cliente.salario, cliente.enderecoid) "

        );
    }
    //listar todos
    public static List<Cliente> listarClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        rs = stmt.executeQuery("select * from Clientes;");
        while(rs.next()){

            Cliente cliente = new Cliente();
            cliente.setNome(rs.getString("nome"));
            lista.add(cliente);
        }
        return lista;
    }

    //atualizar cliente por id

    //apagar cliente - cascade nas contas

    //listar por nome, sobrenome(ordem alfabetica - asc), salario(desc),

}
