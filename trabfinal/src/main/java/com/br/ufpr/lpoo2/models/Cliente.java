package com.br.ufpr.lpoo2.models;

public class Cliente extends Endereco{
    String nome;
    String sobrenome;
    String RG;
    String Cpf;
    Endereco endereco;

    

    public Cliente(String nome, String sobrenome, String rg, String cpf, Number salario) {
    }

    public Cliente(String rua, String cep, String complemento, String estado, String pais, int numero) {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCpf() {
        return Cpf;
    }

    public void setCpf(String cpf) {
        Cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Cliente() {
    }
}
