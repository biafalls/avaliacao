package br.com.soc.sistema.action;

import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.infra.OpcoesComboBuscar;

public class AgendaAction extends Action {
	
public String todas() {
		
		return SUCCESS;
	}
	
	public String filtrar() {	
		return SUCCESS;
	}
	
	public String nova() {
		//if(funcionarioVo.getNome() == null)
			return INPUT;
		
	//	return REDIRECT;
	}
	
	public String editar() {
	//	if(funcionarioVo.getRowid() == null)
	//		return REDIRECT;
		
		
	        return INPUT;
	}
	
	public String excluir() {
		
	    return SUCCESS;
	}
	
	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return Arrays.asList(OpcoesComboBuscar.values());
	}
}
