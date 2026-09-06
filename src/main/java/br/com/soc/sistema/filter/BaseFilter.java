package br.com.soc.sistema.filter;

import java.util.List;

import br.com.soc.sistema.enums.OpcoesComboBuscar;

public abstract class BaseFilter {
    private OpcoesComboBuscar opcoesCombo;
    private String valorBusca;

    public OpcoesComboBuscar getOpcoesCombo() {
        return opcoesCombo;
    }

    public void setOpcoesCombo(String codigo) {
    	if (codigo == null || codigo.trim().isEmpty()) {
            this.opcoesCombo = null;
            return;
        }
    	
        this.opcoesCombo = OpcoesComboBuscar.buscarPor(codigo);
    }

    public String getValorBusca() {
        return valorBusca;
    }

    public void setValorBusca(String valorBusca) {
        this.valorBusca = valorBusca;
    }

    public boolean isNullOpcoesCombo() {
        return opcoesCombo == null;
    }

    public abstract List<OpcoesComboBuscar> getOpcoesDisponiveis();
}
