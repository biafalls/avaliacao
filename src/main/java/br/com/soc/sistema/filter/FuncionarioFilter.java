package br.com.soc.sistema.filter;

import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.enums.OpcoesComboBuscar;

public class FuncionarioFilter extends BaseFilter {
	@Override
    public List<OpcoesComboBuscar> getOpcoesDisponiveis() {
        return Arrays.asList(OpcoesComboBuscar.ID,OpcoesComboBuscar.NOME
        );
    }
}
