package com.br.ufpr.lpoo2.models;

public class Autor {
    String nome_autor;

    public Autor(String nome_autor) {
        this.nome_autor = nome_autor;
    }

    public String getNome_autor() {
        return nome_autor;
    }

    public void setNome_autor(String nome_autor) {
        this.nome_autor = nome_autor;
    }
}
