package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.util.NormalizadorTexto;
import br.com.soc.sistema.util.Validador;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusiness {

	private static final String FUNCIONARIO_NAO_ENCONTRADO ="Funcionário não encontrado.";
	private static final String NOME_OBRIGATORIO = "O nome deve ser preenchido.";
	private static final String CODIGO_INVALIDO = "O código informado deve ser numérico.";
	private static final String OPCAO_BUSCA_OBRIGATORIA = "Selecione uma opção de busca.";
	private static final String VALOR_BUSCA_OBRIGATORIO ="Informe um valor para a busca.";
	
	private FuncionarioDao dao;
	
	public FuncionarioBusiness() {
		this.dao = new FuncionarioDao();
	}
	
	public void cadastrarFuncionario(FuncionarioVo funcionarioVo) {
		normalizarEValidarNome(funcionarioVo);
	    dao.insertFuncionario(funcionarioVo);
	}
	
	public void atualizarFuncionario(FuncionarioVo funcionarioVo) {
		normalizarEValidarNome(funcionarioVo);
	    boolean atualizado = dao.updateFuncionario(funcionarioVo);

	    if (!atualizado)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
	}
	
	public void excluirFuncionario(Long codigo) {
	    boolean excluido = dao.deleteFuncionarioComCompromisso(codigo);

	    if (!excluido)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
	}
	
	public FuncionarioVo buscarFuncionarioParaEdicao(Long codigo) {

		FuncionarioVo funcionario = dao.findByCodigo(codigo);

	    if (funcionario == null)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);

	    return funcionario;
	}

	public List<FuncionarioVo> buscarFuncionariosPorNome(String nome) {

	    String nomeNormalizado = NormalizadorTexto.normalizarEspacos(nome);

	    return dao.findAllByNome(nomeNormalizado);
	}
	
	public List<FuncionarioVo>  buscarTodosOsFuncionarios() {
		return dao.findAllFuncionarios();
	}	
	
	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter) {

	    validarFiltro(filter);
	    
		List<FuncionarioVo> funcionarios = new ArrayList<>();

	    switch (filter.getOpcoesCombo()) {

	        case ID:

	            Long codigo = converterCodigo(filter.getValorBusca());

	            FuncionarioVo funcionario = dao.findByCodigo(codigo);

	            if (funcionario != null) 
	                funcionarios.add(funcionario);
	            break;

	        case NOME:
	            funcionarios.addAll(buscarFuncionariosPorNome(filter.getValorBusca()));
	            break;
	            
	        default:
	            throw new BusinessException("Opção de busca inválida para funcionário.");
	    }

	    return funcionarios;
	}
	
	private void normalizarEValidarNome(FuncionarioVo funcionarioVo) {
		 funcionarioVo.setNome(Validador.validarTextoObrigatorio(funcionarioVo.getNome(), NOME_OBRIGATORIO));
	}
	
	private Long converterCodigo(String valorBusca) {
		return Validador.converterLong(valorBusca, CODIGO_INVALIDO);
	}
	
	private void validarFiltro(FuncionarioFilter filter) {
		
		if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException(OPCAO_BUSCA_OBRIGATORIA);

	    String valorBusca = NormalizadorTexto.normalizarEspacos(filter.getValorBusca());

	    if (valorBusca == null || valorBusca.isEmpty())
	        throw new BusinessException(VALOR_BUSCA_OBRIGATORIO);
	}
}
