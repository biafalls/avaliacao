package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.util.NormalizadorTexto;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusiness {

	private static final String FUNCIONARIO_NAO_ENCONTRADO ="Funcionário não encontrado.";
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
		if (dao.findSeFuncionarioTemCompromisso(codigo)) 
			throw new BusinessException("O funcionário possui compromissos. Não pode ser deletado.");
		
	    boolean excluido = dao.deleteFuncionario(codigo);

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
		 String nome = NormalizadorTexto.normalizarEspacos(funcionarioVo.getNome());
		 
		 if (nome == null || nome.isEmpty())
			 throw new BusinessException("O nome deve ser preenchido.");

		funcionarioVo.setNome(nome);
	}
	
	private Long converterCodigo(String valorBusca) {

	    String valorNormalizado = NormalizadorTexto.normalizarEspacos(valorBusca);

	    return Long.parseLong(valorNormalizado);
	}
}
