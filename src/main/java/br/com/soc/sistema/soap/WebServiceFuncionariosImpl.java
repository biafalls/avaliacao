package br.com.soc.sistema.soap;

import java.util.List;

import javax.jws.WebService;

import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.vo.FuncionarioVo;

@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceFuncionarios" )
public class WebServiceFuncionariosImpl implements WebServiceFuncionarios {

	private FuncionarioBusiness business;
	
	public WebServiceFuncionariosImpl() {
		this.business = new FuncionarioBusiness();
	}
	
	@Override
	public FuncionarioVo buscarFuncionario(Long codigo) {		
		return business.buscarFuncionarioPorCodigo(codigo);
	}
	
	@Override 
	public FuncionarioVo[] buscarFuncionarioPorNome(String nome) {	
		List<FuncionarioVo> funcionarios = business.buscarFuncionariosPorNome(nome);
		return funcionarios.toArray(new FuncionarioVo[funcionarios.size()]);
	}
	
	@Override
	public FuncionarioVo[] listarFuncionarios() {
		List<FuncionarioVo> funcionarios =business.buscarTodosOsFuncionarios();
	    return funcionarios.toArray(new FuncionarioVo[funcionarios.size()]);
	}
}
