package com.br.ufpr.lpoo2.models;

public class ContaCorrente extends Conta {
    private double limite;

    public ContaCorrente(Cliente dono, int numero, double saldo, double limite) {
        super(dono, numero, saldo);
        this.limite = limite;
    }

    public ContaCorrente() {
        super();
    }

    @Override
    public boolean saca(double valor) {
        if (valor <= 0) {
            // Removed System.out.println
            return false;
        }
        if (this.saldo - valor >= -limite) {
            this.saldo -= valor;
            return true;
        } else {
            // Removed System.out.println
            return false;
        }
    }

    @Override
    public void remunera() {
        this.saldo += this.saldo * 0.01;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }
}
