package br.com.soc.sistema.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import br.com.soc.sistema.vo.FuncionarioVo;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WebServiceFuncionarios {
	
	@WebMethod
	public FuncionarioVo buscarFuncionario(Long codigo);
	
	@WebMethod
	public FuncionarioVo[] buscarFuncionarioPorNome(String nome);
	
	@WebMethod 
	public FuncionarioVo[] listarFuncionarios();
}
