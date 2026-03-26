package com.br.ufpr.lpoo2.repository;

import com.br.ufpr.lpoo2.connection.ConnectionFactory;
import com.br.ufpr.lpoo2.models.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorRepository {
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
    public AutorRepository() throws SQLException {
    }

    public static List<Autor> listarAutores() throws SQLException {
        List<Autor> lista = new ArrayList<>();
        rs = stmt.executeQuery("select * from autor;");
        while(rs.next()){

            Autor autor = new Autor(null);
            autor.setNome_autor(rs.getString("nome_autor"));
            lista.add(autor);
        }
        return lista;
    }
}
