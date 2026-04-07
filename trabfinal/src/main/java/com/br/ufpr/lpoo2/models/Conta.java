package com.br.ufpr.lpoo2.models;

public abstract class Conta implements ContaI {
    protected int id;
    protected Cliente dono;
    protected int numero;
    protected double saldo;

    public Conta(Cliente dono, int numero, double saldo) {
        this.dono = dono;
        this.numero = numero;
        this.saldo = saldo;
    }

    public Conta() {
    }

    @Override
    public boolean deposita(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            return true;
        }
        return false;
    }

    @Override
    public boolean saca(double valor) {
        if (valor > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public abstract void remunera();

    @Override
    public String toString() {
        String tipo = "Desconhecido";
        if (this instanceof ContaCorrente) {
            tipo = "Corrente";
        } else if (this instanceof ContaInvestimento) {
            tipo = "Investimento";
        }
        return String.format("Conta %s (Número: %d) - Saldo: R$ %.2f", tipo, numero, saldo);
    }
}
