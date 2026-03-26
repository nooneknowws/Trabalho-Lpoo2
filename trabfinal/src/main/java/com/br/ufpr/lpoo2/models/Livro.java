package com.br.ufpr.lpoo2.models;

public class Livro {
    String nome;
    int nomeAutorId;

    public Livro(String nome, int nomeAutorId) {
        this.nome = nome;
        this.nomeAutorId = nomeAutorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNomeAutorId() {
        return nomeAutorId;
    }

    public void setNomeAutorId(int nomeAutorId) {
        this.nomeAutorId = nomeAutorId;
    }
}
