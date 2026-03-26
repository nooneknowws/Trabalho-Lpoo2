package com.br.ufpr.lpoo2.database;

import com.br.ufpr.lpoo2.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

public static void criaTabelas() throws SQLException {
    String sqlAutor = "CREATE TABLE IF NOT EXISTS autor ("+
            "id SERIAL PRIMARY KEY," +
            "nome varchar(55)" +
            ");";
    String sqlLivros = "CREATE TABLE IF NOT EXISTS livros (" +
            "id SERIAL PRIMARY KEY," +
            "nome VARCHAR(255) NOT NULL," +
            "nome_autor integer REFERENCES autor(id)" +
            ");";
    try(Connection conn = ConnectionFactory.getConnection();
        Statement stmt = conn.createStatement()) {
        stmt.execute(sqlAutor);
        stmt.execute(sqlLivros);
    }
    catch(SQLException e){
        System.out.println("Erro ao gerar tabelas:" + e.getMessage());
    }
    }

    public CreateTable() throws SQLException {
    }
}
