package com.br.ufpr.lpoo2.tablemodel;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import com.br.ufpr.lpoo2.models.Cliente;

public class ClienteTableModel extends AbstractTableModel {
    private List<Cliente> lista;
    private final String[] colunas = {"Nome", "Sobrenome", "CPF", "Salário"};

    public ClienteTableModel(List<Cliente> clientes) {
        this.lista = clientes;
    }

    @Override public int getRowCount() { return lista.size(); }
    @Override public int getColumnCount() { return colunas.length; }
    @Override public String getColumnName(int col) { return colunas[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Cliente c = lista.get(row);
        switch (col) {
            case 0: return c.getNome();
            case 1: return c.getSobrenome();
            case 2: return c.getCpf();
            case 3: return String.format("R$ %.2f", c.getSalario());
            default: return null;
        }
    }

    public void setDados(List<Cliente> novosDados) {
        this.lista = novosDados;
        fireTableDataChanged();
    }

    public Cliente getClienteAt(int row) { return lista.get(row); }
}