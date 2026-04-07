package com.br.ufpr.lpoo2.database;

import com.br.ufpr.lpoo2.connection.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class CreateTable {

    public static void criaTabelas() {
        String sql = "";
        try (InputStream inputStream = CreateTable.class.getClassLoader().getResourceAsStream("banco.sql")) {
            if (inputStream == null) {
                throw new IOException("Não foi possível encontrar o arquivo banco.sql");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo banco.sql: " + e.getMessage());
            return;
        }

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String s : sql.split(";")) {
                if (!s.trim().isEmpty()) {
                    stmt.execute(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gerar tabelas:" + e.getMessage());
        }
    }
}
