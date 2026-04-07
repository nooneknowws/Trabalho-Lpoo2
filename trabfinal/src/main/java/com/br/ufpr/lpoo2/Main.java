package com.br.ufpr.lpoo2;

import com.br.ufpr.lpoo2.database.CreateTable;
import com.br.ufpr.lpoo2.telas.ClienteView;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        CreateTable.criaTabelas();
        new ClienteView();
    }
}
