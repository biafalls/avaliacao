package br.com.soc.sistema.soap;

import java.util.List;

import javax.jws.WebService;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.vo.AgendaVo;

@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceAgendas" )
public class WebServiceAgendasImpl implements WebServiceAgendas{
	
	private AgendaBusiness business;
	
	public WebServiceAgendasImpl() {
		this.business = new AgendaBusiness();
	}
	
	@Override
	public AgendaVo buscarAgenda(Long codigo) {		
		return business.buscarAgendaPorCodigo(codigo);
	}
	
	@Override 
	public AgendaVo[] buscarAgendaPorNome(String nome) {	
		List<AgendaVo> agendas = business.buscarAgendasPorNome(nome);
		return agendas.toArray(new AgendaVo[agendas.size()]);
	}
	
	@Override
	public AgendaVo[] buscarAgendaPorPeriodo(PeriodoDisponivel periodo) {
	    List<AgendaVo> agendas = business.buscarAgendasPorPeriodo(periodo);
	    return agendas.toArray(new AgendaVo[agendas.size()]);
	}
	
	@Override
	public AgendaVo[] listarAgendas() {
	    List<AgendaVo> agendas = business.buscarTodasAsAgendas();
	    return agendas.toArray(new AgendaVo[agendas.size()]);
	}
}
