package com.br.ufpr.lpoo2.models;

public class Endereco {
    public String rua;
    public String CEP;
    public String complemento;
    public String estado;
    public String pais;
    public int numero;

    public Endereco() {
    }

    public Endereco(String rua, String CEP, String complemento, String estado, String pais, int numero) {
        this.rua = rua;
        this.CEP = CEP;
        this.complemento = complemento;
        this.estado = estado;
        this.pais = pais;
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
