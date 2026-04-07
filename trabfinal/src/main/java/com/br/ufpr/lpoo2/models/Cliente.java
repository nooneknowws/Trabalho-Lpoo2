package com.br.ufpr.lpoo2.models;

public class Cliente implements Comparable<Cliente> {
    private int id;
    private String nome, sobrenome, rg, cpf;
    private Double salario;
    private Endereco endereco;

    public Cliente(String nome, String sobrenome, String rg, String cpf, Double salario, Endereco endereco) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.rg = rg;
        this.cpf = cpf;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Cliente(String nome, String sobrenome, String rg, String cpf, double salario) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.rg = rg;
        this.cpf = cpf;
        this.salario = salario;
    }

    @Override
    public int compareTo(Cliente outro) {
        return this.nome.compareToIgnoreCase(outro.getNome());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Cliente() {
    }

    @Override
    public String toString() {
        return nome + " " + sobrenome + " (CPF: " + cpf + ")";
    }
}
