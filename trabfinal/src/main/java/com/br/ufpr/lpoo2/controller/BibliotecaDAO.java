package com.br.ufpr.lpoo2.controller;

import com.br.ufpr.lpoo2.models.Autor;
import com.br.ufpr.lpoo2.repository.AutorRepository;

import java.sql.SQLException;
import java.util.List;

public class BibliotecaDAO {
    //GET
    public List<Autor> listarAutores() throws SQLException {
        return AutorRepository.listarAutores();
    }
    public void listarLivros(){

    }
    //POST
    public void inserirLivro() {

    }
    public void inserirAutor(){

    }
    //PUT
    public void alterarLivros(){

    }
    public void alterarAutor(){

    }
    //DELETE
    public void apagarLivros(){

    }
    public void apagarAutor(){
        //ISSO NAO PODE EXISTIR POIS CAUSARA UM ERRO EM CASCATA APOS DELETAR O AUTOR
    }
}
