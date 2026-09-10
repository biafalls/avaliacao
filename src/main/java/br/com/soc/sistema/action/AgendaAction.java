package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaAction extends Action {
	
	private static final String ERRO_CARREGAR_AGENDAS = "Não foi possível carregar as agendas. Tente novamente.";
	private static final String ERRO_CONSULTAR_AGENDAS = "Não foi possível consultar as agendas. Tente novamente.";
	private static final String ERRO_SALVAR_AGENDA = "Não foi possível salvar a agenda. Tente novamente.";
	private static final String ERRO_CARREGAR_AGENDA_EDICAO = "Não foi possível carregar a agenda para edição. Tente novamente.";
	private static final String ERRO_EXCLUIR_AGENDA = "Não foi possível excluir a agenda. Tente novamente.";
	
	private List<AgendaVo> agendas = new ArrayList<>();
	private AgendaBusiness business = new AgendaBusiness();
	private AgendaFilter filtrar = new AgendaFilter();
	private AgendaVo agendaVo = new AgendaVo();
	private String codigoPeriodo;
	
	private void carregarAgendas() {
		agendas = business.buscarTodasAsAgendas();
	}
	
	public String todas() {
		try {
			carregarAgendas();
			
			if(agendas.isEmpty()) 
				addActionError("Nenhuma agenda cadastrada.");
			
		} catch (TechnicalException e) {
			addActionError(ERRO_CARREGAR_AGENDAS);
		}
		
		return SUCCESS;
	}
	
	public String filtrar() {	
		try {
			agendas = business.filtrarAgendas(filtrar);
			
			if (agendas.isEmpty())
				addActionError("Nenhuma agenda encontrada.");
			
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarAgendas();
			
		} catch (TechnicalException e) {
	        addActionError(ERRO_CONSULTAR_AGENDAS);
	    }    
		return SUCCESS;
	}
	
	public String nova() {
		return INPUT;
	}
	
	public String salvar() {
		try {
	        agendaVo.setPeriodoDisponivel(PeriodoDisponivel.buscarPor(codigoPeriodo));

	        if (agendaVo.getRowid() == null) {
	            business.cadastrarAgenda(agendaVo);
	        } else {
	            business.atualizarAgenda(agendaVo);
	        }

	        return REDIRECT;

	    } catch (BusinessException e) {
	    	addActionError(e.getMessage());
	        return INPUT;

	    } catch (TechnicalException e) {
	        addActionError(ERRO_SALVAR_AGENDA);
	        return INPUT;
	    }
	}
	
	public String editar() {
		if(agendaVo.getRowid() == null)
			return REDIRECT;
		
		try {
			agendaVo = business.buscarAgendaParaEdicao(agendaVo.getRowid());
			codigoPeriodo = agendaVo.getPeriodoDisponivel().getCodigo();
			return INPUT;
			
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarAgendas();
			return SUCCESS;
			
		} catch (TechnicalException e) {
	        addActionError(ERRO_CARREGAR_AGENDA_EDICAO);
	        return SUCCESS;
	    }    
	}
	
	public String excluir() {
		if(agendaVo.getRowid() == null)
			return REDIRECT;
		
		try {
			business.excluirAgenda(agendaVo.getRowid());
			return REDIRECT;
			
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarAgendas();
			return SUCCESS;
			
		} catch (TechnicalException e) {
	        addActionError(ERRO_EXCLUIR_AGENDA);
	        return SUCCESS;
	    }    
	}
	
	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return filtrar.getOpcoesDisponiveis();
	}
	
	public List<PeriodoDisponivel> getListaPeriodos() {
		return Arrays.asList(PeriodoDisponivel.values());
	}
	
	public String getCodigoPeriodo() {
	    return codigoPeriodo;
	}

	public void setCodigoPeriodo(String codigoPeriodo) {
	    this.codigoPeriodo = codigoPeriodo;
	}

	public List<AgendaVo> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<AgendaVo> agendas) {
		this.agendas = agendas;
	}

	public AgendaBusiness getBusiness() {
		return business;
	}

	public void setBusiness(AgendaBusiness business) {
		this.business = business;
	}

	public AgendaFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(AgendaFilter filtrar) {
		this.filtrar = filtrar;
	}

	public AgendaVo getAgendaVo() {
		return agendaVo;
	}

	public void setAgendaVo(AgendaVo agendaVo) {
		this.agendaVo = agendaVo;
	}
}
