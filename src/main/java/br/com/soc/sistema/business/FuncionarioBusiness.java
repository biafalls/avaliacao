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
	
	public List<FuncionarioVo> trazerTodosOsFuncionarios(){
		return dao.findAllFuncionarios();
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
	    boolean excluido = dao.deleteFuncionario(codigo);

	    if (!excluido)
	        throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
	}
	
	public FuncionarioVo buscarFuncionarioPorCodigo(Long codigo) {
		 return dao.findByCodigo(codigo);
	}
	
	public List<FuncionarioVo> buscarFuncionariosPorNome(String nome) {

	    String nomeNormalizado = NormalizadorTexto.normalizarEspacos(nome);

	    return dao.findAllByNome(nomeNormalizado);
	}
	
	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter) {

	    List<FuncionarioVo> funcionarios = new ArrayList<>();

	    switch (filter.getOpcoesCombo()) {

	        case ID:

	            Long codigo = converterCodigo(filter.getValorBusca());

	            FuncionarioVo funcionario = buscarFuncionarioPorCodigo(codigo);

	            if (funcionario != null) 
	                funcionarios.add(funcionario);
	            break;

	        case NOME:
	            funcionarios.addAll(buscarFuncionariosPorNome(filter.getValorBusca()));
	            break;
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

//	public void salvarFuncionario(FuncionarioVo funcionarioVo) {
//	String nome = NormalizadorTexto.normalizarEspacos(funcionarioVo.getNome());
//		
//	if(nome == null || nome.isEmpty())
//		throw new BusinessException("O Nome deve ser preenchido");
//		
//	funcionarioVo.setNome(nome);
//		
//	if (funcionarioVo.getRowid() == null) {
//	    dao.insertFuncionario(funcionarioVo);
//	} else {
//		boolean atualizado = dao.updateFuncionario(funcionarioVo);
//
//	    if (!atualizado)
//	    	throw new BusinessException(FUNCIONARIO_NAO_ENCONTRADO);
//	}
//}	
	
//	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter){
//		List<FuncionarioVo> funcionarios = new ArrayList<>();
//		String valorBusca = NormalizadorTexto.normalizarEspacos(filter.getValorBusca());
//		
//		switch (filter.getOpcoesCombo()) {
//			case ID:
//			
//					Long codigo = Long.parseLong(valorBusca);
//					 FuncionarioVo funcionario = dao.findByCodigo(codigo);
//
//		                if (funcionario != null)
//		                    funcionarios.add(funcionario);
//				
//			break;
//
//			case NOME:
//				funcionarios.addAll(dao.findAllByNome(valorBusca));
//			break;
//		}
//
//		return funcionarios;
//	}
	
	
	
//}
