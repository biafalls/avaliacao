package br.com.soc.sistema.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.vo.AgendaVo;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WebServiceAgendas {
	
	@WebMethod
	public AgendaVo buscarAgenda(Long codigo);
	
	@WebMethod
	public AgendaVo[] buscarAgendaPorNome(String nome);
	
	@WebMethod
    public AgendaVo[] buscarAgendaPorPeriodo(PeriodoDisponivel periodo);
	
	@WebMethod
	public AgendaVo[] listarAgendas();
}
