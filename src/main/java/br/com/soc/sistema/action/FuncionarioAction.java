package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioAction extends Action {
	
	private static final String ERRO_CARREGAR_FUNCIONARIOS = "Não foi possível carregar os funcionários. Tente novamente.";
	private static final String ERRO_CONSULTAR_FUNCIONARIOS = "Não foi possível consultar os funcionários. Tente novamente.";
	private static final String ERRO_SALVAR_FUNCIONARIO = "Não foi possível salvar o funcionário. Tente novamente.";
	private static final String ERRO_CARREGAR_FUNCIONARIO_EDICAO = "Não foi possível carregar o funcionário para edição. Tente novamente.";
	private static final String ERRO_EXCLUIR_FUNCIONARIO = "Não foi possível excluir o funcionário. Tente novamente.";
	
	private List<FuncionarioVo> funcionarios = new ArrayList<>();
	private FuncionarioBusiness business = new FuncionarioBusiness();
	private FuncionarioFilter filtrar = new FuncionarioFilter();
	private FuncionarioVo funcionarioVo = new FuncionarioVo();
	
	private void carregarFuncionarios() {
		funcionarios = business.buscarTodosOsFuncionarios();
	}
	
	public String todos() {
		try {
			carregarFuncionarios();	
			
			if (funcionarios.isEmpty())
	            addActionError("Nenhum funcionário cadastrado.");
			
		} catch (TechnicalException e) {
	        addActionError(ERRO_CARREGAR_FUNCIONARIOS);
	    }
		
		return SUCCESS;
	}
	
	public String filtrar() {	
		  try {
			  funcionarios = business.filtrarFuncionarios(filtrar);

		      if (funcionarios.isEmpty())
		    	  addActionError("Nenhum funcionário encontrado.");
		        
		    } catch (BusinessException e) {
		        addActionError(e.getMessage());
		        carregarFuncionarios();
		        
		    } catch (TechnicalException e) {
		        addActionError(ERRO_CONSULTAR_FUNCIONARIOS);
		    }

		    return SUCCESS;
	}
	
	public String novo() {
		return INPUT;
	}
	
	public String salvar() {
		try {
			
			if (funcionarioVo.getRowid() == null) 
	            business.cadastrarFuncionario(funcionarioVo);
	         else 
	            business.atualizarFuncionario(funcionarioVo);
			
			return REDIRECT;
			
		} catch (BusinessException e) {
			addFieldError("funcionarioVo.nome", e.getMessage());
	        return INPUT;
	        
		} catch (TechnicalException e) {
	        addActionError( ERRO_SALVAR_FUNCIONARIO);
	        return INPUT;
	    }
	}
	
	public String editar() {
		if(funcionarioVo.getRowid() == null)
			return REDIRECT;
		
		try {

	        funcionarioVo = business.buscarFuncionarioPorCodigo(funcionarioVo.getRowid());
	        return INPUT;

	    } catch (BusinessException e) {
	        addActionError(e.getMessage());
	        carregarFuncionarios();
	        return SUCCESS;

	    } catch (TechnicalException e) {
	        addActionError(ERRO_CARREGAR_FUNCIONARIO_EDICAO);
	        return SUCCESS;
	    }
	}
	
	public String excluir() {
	    if (funcionarioVo.getRowid() == null) 
	        return REDIRECT;

	    try {
	        business.excluirFuncionario(funcionarioVo.getRowid());
	        return REDIRECT;

	    } catch (BusinessException e) {
	        addActionError(e.getMessage());
	        carregarFuncionarios();
	        return SUCCESS;

	    } catch (TechnicalException e) {
	        addActionError(ERRO_EXCLUIR_FUNCIONARIO);
	        return SUCCESS;
	    }
	}
	
	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return filtrar.getOpcoesDisponiveis();
	}
	
	public List<FuncionarioVo> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<FuncionarioVo> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public FuncionarioFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(FuncionarioFilter filtrar) {
		this.filtrar = filtrar;
	}

	public FuncionarioVo getFuncionarioVo() {
		return funcionarioVo;
	}

	public void setFuncionarioVo(FuncionarioVo funcionarioVo) {
		this.funcionarioVo = funcionarioVo;
	}
}
