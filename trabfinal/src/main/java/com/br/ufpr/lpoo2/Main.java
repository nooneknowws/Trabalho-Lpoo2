package com.br.ufpr.lpoo2;
import com.br.ufpr.lpoo2.connection.ConnectionFactory;
import com.br.ufpr.lpoo2.controller.BibliotecaDAO;
import com.br.ufpr.lpoo2.database.CreateTable;
import com.br.ufpr.lpoo2.models.Autor;
import com.br.ufpr.lpoo2.repository.AutorRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.print("Hello and welcome!");
        CreateTable.criaTabelas();

        List<Autor> autores = AutorRepository.listarAutores();
        System.out.println("lista de autores:");
        for (Autor autor: autores){
            System.out.println("Nome do Autor: "+ autor.getNome_autor());
        }
    }
}