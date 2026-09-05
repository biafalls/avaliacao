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
	        addActionError("Não foi possível carregar os funcionários. Tente novamente.");
	    }
		
		return SUCCESS;
	}
	
	public String filtrar() {	
		  try {
			  
			  if (!validarFiltroPesquisa()){
					carregarFuncionarios();
					return SUCCESS;
				}

			  funcionarios = business.filtrarFuncionarios(filtrar);

		      if (funcionarios.isEmpty())
		    	  addActionError("Nenhum funcionário encontrado.");
		        
		    } catch (NumberFormatException e) {
		        addActionError("O código informado deve ser numérico.");
		        carregarFuncionarios();
		        
		    } catch (TechnicalException e) {

		        addActionError(
		            "Não foi possível realizar a consulta. Tente novamente."
		        );
		    }

		    return SUCCESS;
	}
	
	public String novo() {
		if(funcionarioVo.getNome() == null)
			return INPUT;
		
		try {
			if (funcionarioVo.getRowid() == null) {
	            business.cadastrarFuncionario(funcionarioVo);
	        } else {
	            business.atualizarFuncionario(funcionarioVo);
	        }
			
			return REDIRECT;
			
		} catch (BusinessException e) {
			addFieldError("funcionarioVo.nome", e.getMessage());
	        return INPUT;
	        
		} catch (TechnicalException e) {
	        addActionError( "Não foi possível salvar o funcionário. Tente novamente.");
	        return INPUT;
	    }
	}
	
	public String editar() {
		if(funcionarioVo.getRowid() == null)
			return REDIRECT;
		
		try {

	        funcionarioVo = business.buscarFuncionarioParaEdicao(funcionarioVo.getRowid());
	        return INPUT;

	    } catch (BusinessException e) {
	        addActionError(e.getMessage());
	        carregarFuncionarios();
	        return SUCCESS;

	    } catch (TechnicalException e) {
	        addActionError("Não foi possível carregar o funcionário para edição.");
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
	        addActionError( "Não foi possível excluir o funcionário. Tente novamente.");
	        return SUCCESS;
	    }
	}
	
	private boolean validarFiltroPesquisa() {

	    if (filtrar == null || filtrar.isNullOpcoesCombo()) {
	        addActionError("Selecione uma opção para realizar a busca.");
	        return false;
	    }

	    if (filtrar.getValorBusca() == null || filtrar.getValorBusca().trim().isEmpty()) {
	        addActionError("Informe um valor para realizar a busca.");
	        return false;
	    }

	    return true;
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
