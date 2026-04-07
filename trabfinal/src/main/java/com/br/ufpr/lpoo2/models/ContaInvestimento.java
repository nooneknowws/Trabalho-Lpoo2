package com.br.ufpr.lpoo2.models;

public class ContaInvestimento extends Conta {
    private double montanteMinimo;
    private double depositoMinimo;

    public ContaInvestimento(Cliente dono, int numero, double saldo, double montanteMinimo, double depositoMinimo) {
        super(dono, numero, saldo);
        this.montanteMinimo = montanteMinimo;
        this.depositoMinimo = depositoMinimo;
    }

    public ContaInvestimento() {
        super();
    }

    @Override
    public boolean deposita(double valor) {
        if (valor >= depositoMinimo) {
            return super.deposita(valor);
        } else {
            // Removed System.out.println
            return false;
        }
    }

    @Override
    public boolean saca(double valor) {
        if (valor <= 0) {
            // Removed System.out.println
            return false;
        }
        if (this.saldo - valor >= montanteMinimo) {
            this.saldo -= valor;
            return true;
        } else {
            // Removed System.out.println
            return false;
        }
    }

    @Override
    public void remunera() {
        this.saldo += this.saldo * 0.02;
    }

    public double getMontanteMinimo() {
        return montanteMinimo;
    }

    public void setMontanteMinimo(double montanteMinimo) {
        this.montanteMinimo = montanteMinimo;
    }

    public double getDepositoMinimo() {
        return depositoMinimo;
    }

    public void setDepositoMinimo(double depositoMinimo) {
        this.depositoMinimo = depositoMinimo;
    }
}
