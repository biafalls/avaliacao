package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoAction extends Action {
	
	private List<CompromissoVo> compromissos = new ArrayList<>();
	private CompromissoBusiness business = new CompromissoBusiness();
	private CompromissoFilter filtrar = new CompromissoFilter();
	private CompromissoVo compromissoVo = new CompromissoVo();
	
	private List<FuncionarioVo> funcionarios = new ArrayList<>();
	private List<AgendaVo> agendas = new ArrayList<>();
	private FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
	private AgendaBusiness agendaBusiness = new AgendaBusiness();
	
	private void carregarCompromissos() {
		compromissos = business.buscarTodosCompromissos();
	}
	
	private void carregarCombos() {
	    funcionarios = funcionarioBusiness.buscarTodosOsFuncionarios();
	    agendas = agendaBusiness.buscarTodasAsAgendas();
	}
	
	public String todos() {
		try {
			carregarCompromissos();
			
			if (compromissos.isEmpty())
				addActionError("Nenhum compromisso cadastrado.");
				
		} catch (TechnicalException e) {
			addActionError(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String filtrar() {
		try {
			compromissos = business.filtrarCompromissos(filtrar);
			
			if (compromissos.isEmpty())
				addActionError("Nenhum compromisso encontrado.");
			
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarCompromissos();

		} catch (TechnicalException e) {
			addActionError(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String novo() {
		try {
			carregarCombos();
			return INPUT;
			
		} catch (TechnicalException e) {
	        addActionError(e.getMessage());
	        return SUCCESS;
	    }
	}
	
	public String salvar() {
		try {
			
			if (compromissoVo.getRowid() == null) {
				business.cadastrarCompromisso(compromissoVo);
			} else {
				business.atualizarCompromisso(compromissoVo);
			}
			
	        return REDIRECT;

	    } catch (BusinessException e) {
	        addActionError(e.getMessage());
	        carregarCombos();
	        return INPUT;

	    } catch (TechnicalException e) {
	        addActionError(e.getMessage());
	        carregarCombos();
	        return INPUT;
	    }
	}
	
	public String editar() {
		 try {
		        compromissoVo = business.buscarCompromissoParaEdicao(compromissoVo.getRowid());
		        carregarCombos();
		        return INPUT;

		    } catch (BusinessException e) {
		        addActionError(e.getMessage());
		        carregarCompromissos();
		        return SUCCESS;

		    } catch (TechnicalException e) {
		        addActionError(e.getMessage());
		        return SUCCESS;
		    }
	}
	
	public String excluir() {
		if(compromissoVo.getRowid() == null)
			return REDIRECT;
		
		try {
			business.excluirCompromisso(compromissoVo.getRowid());
			return REDIRECT;
			
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarCompromissos();
			return SUCCESS;
			
		} catch (TechnicalException e) {
	        addActionError(e.getMessage());
	        return SUCCESS;
	    }    
	}
	
	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return filtrar.getOpcoesDisponiveis();
	}

	public List<CompromissoVo> getCompromissos() {
		return compromissos;
	}

	public void setCompromissos(List<CompromissoVo> compromissos) {
		this.compromissos = compromissos;
	}

	public CompromissoBusiness getBusiness() {
		return business;
	}

	public void setBusiness(CompromissoBusiness business) {
		this.business = business;
	}

	public CompromissoFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(CompromissoFilter filtrar) {
		this.filtrar = filtrar;
	}

	public CompromissoVo getCompromissoVo() {
		return compromissoVo;
	}

	public void setCompromissoVo(CompromissoVo compromissoVo) {
		this.compromissoVo = compromissoVo;
	}

	public List<FuncionarioVo> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<FuncionarioVo> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<AgendaVo> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<AgendaVo> agendas) {
		this.agendas = agendas;
	}

	public FuncionarioBusiness getFuncionarioBusiness() {
		return funcionarioBusiness;
	}

	public void setFuncionarioBusiness(FuncionarioBusiness funcionarioBusiness) {
		this.funcionarioBusiness = funcionarioBusiness;
	}

	public AgendaBusiness getAgendaBusiness() {
		return agendaBusiness;
	}

	public void setAgendaBusiness(AgendaBusiness agendaBusiness) {
		this.agendaBusiness = agendaBusiness;
	}
}
