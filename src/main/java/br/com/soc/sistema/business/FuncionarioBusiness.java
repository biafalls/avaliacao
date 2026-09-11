package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.util.Validador;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusiness {

	private static final String FUNCIONARIO_NAO_ENCONTRADO ="Funcionário não encontrado.";
	private static final String FUNCIONARIO_INVALIDO = "Funcionário inválido.";
	private static final String NOME_OBRIGATORIO = "O nome deve ser preenchido.";
	private static final String CODIGO_INVALIDO = "O código informado deve ser numérico.";
	private static final String CODIGO_OBRIGATORIO = "O código do funcionário deve ser informado.";
	private static final String OPCAO_BUSCA_OBRIGATORIA = "Selecione uma opção de busca.";
	private static final String VALOR_BUSCA_OBRIGATORIO ="Informe um valor para a busca.";
	
	private FuncionarioDao dao;
	
	public FuncionarioBusiness() {
		this.dao = new FuncionarioDao();
	}
	
	public void cadastrarFuncionario(FuncionarioVo funcionarioVo) {
		validarFuncionario(funcionarioVo);
	    dao.insertFuncionario(funcionarioVo);
	}
	
	public void atualizarFuncionario(FuncionarioVo funcionarioVo) {
		validarFuncionario(funcionarioVo);
		Validador.validarLongObrigatorio(funcionarioVo.getRowid(), CODIGO_OBRIGATORIO);
		
	    boolean atualizado = dao.updateFuncionario(funcionarioVo);

	    if (!atualizado)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
	}
	
	public void excluirFuncionario(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);
		
	    boolean excluido = dao.deleteFuncionarioComCompromisso(codigo);

	    if (!excluido)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
	}
	
	public FuncionarioVo buscarFuncionarioPorCodigo(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);
		
		FuncionarioVo funcionario = dao.findByCodigo(codigo);

	    if (funcionario == null)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);

	    return funcionario;
	}

	public List<FuncionarioVo> buscarFuncionariosPorNome(String nome) {

	   nome = Validador.validarTextoObrigatorio(nome, NOME_OBRIGATORIO);

	    return dao.findAllByNome(nome);
	}
	
	public List<FuncionarioVo>  buscarTodosOsFuncionarios() {
		return dao.findAllFuncionarios();
	}	
	
	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter) {

	    String valorBusca = validarFiltro(filter);
	    
		List<FuncionarioVo> funcionarios = new ArrayList<>();

	    switch (filter.getOpcoesCombo()) {

	        case ID:

	            Long codigo = Validador.converterLong(valorBusca,CODIGO_INVALIDO);

	            FuncionarioVo funcionario = dao.findByCodigo(codigo);

	            if (funcionario != null) 
	                funcionarios.add(funcionario);
	            break;

	        case NOME:
	            funcionarios.addAll(buscarFuncionariosPorNome(valorBusca));
	            break;
	            
	        default:
	            throw new BusinessException("Opção de busca inválida para funcionário.");
	    }

	    return funcionarios;
	}
	
	private void validarFuncionario(FuncionarioVo funcionarioVo) {

	    if (funcionarioVo == null)
	        throw new BusinessException(FUNCIONARIO_INVALIDO);

	    funcionarioVo.setNome(Validador.validarTextoObrigatorio(funcionarioVo.getNome(), NOME_OBRIGATORIO));
	}
	
	private String validarFiltro(FuncionarioFilter filter) {
		
		if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException(OPCAO_BUSCA_OBRIGATORIA);

	    return Validador.validarTextoObrigatorio(filter.getValorBusca(),VALOR_BUSCA_OBRIGATORIO);
	}
}
