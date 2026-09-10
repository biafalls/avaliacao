package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.util.NormalizadorTexto;
import br.com.soc.sistema.util.Validador;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class AgendaBusiness {
	
	private static final String AGENDA_NAO_ENCONTRADA = "Agenda não encontrada.";
	private static final String AGENDA_POSSUI_COMPROMISSOS = "Não é possível excluir uma agenda que possui compromissos cadastrados.";
	private AgendaDao dao;
	private CompromissoDao compromissoDao;
	
	public AgendaBusiness() {
		this.dao = new AgendaDao();
		this.compromissoDao = new CompromissoDao();
	}
	
	public void cadastrarAgenda(AgendaVo agendaVo) {
		validarAgenda(agendaVo);
		dao.insertAgenda(agendaVo);
	}
	
	public void atualizarAgenda(AgendaVo agendaVo) {
		validarAgenda(agendaVo);
		boolean atualizado = dao.updateAgenda(agendaVo);
		
		if (!atualizado)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
	}
	
	public void excluirAgenda(Long codigo) {
		if (compromissoDao.existeCompromissoPorAgenda(codigo))
			throw new BusinessException(AGENDA_POSSUI_COMPROMISSOS);
		
		boolean excluido = dao.deleteAgenda(codigo);
		
		if (!excluido)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
	}
	
	public AgendaVo buscarAgendaParaEdicao(Long codigo) {
		AgendaVo agenda = dao.findByCodigo(codigo);
		
		if (agenda == null)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
		
		return agenda;
	}
	
	public List<AgendaVo> buscarAgendasPorNome(String nome) {
		String nomeNormalizado = NormalizadorTexto.normalizarEspacos(nome);
		
		return dao.findAllByNome(nomeNormalizado);
	}
	
	public List<AgendaVo> buscarAgendasPorPeriodo(PeriodoDisponivel periodo) {
		return dao.findAllByPeriodo(periodo);
	}
	
	public List<AgendaVo> buscarTodasAsAgendas() {
		return dao.findAllAgendas();
	}
	
	public List<AgendaVo> filtrarAgendas(AgendaFilter filter) {
		
		validarFiltro(filter);
		
		List<AgendaVo> agendas = new ArrayList<>();
		
		switch (filter.getOpcoesCombo()) {
			case ID :
				Long codigo = converterCodigo(filter.getValorBusca());
				
				AgendaVo agenda = dao.findByCodigo(codigo);
				if (agenda != null)
					agendas.add(agenda);
				break;
			
			case NOME :
				agendas.addAll(buscarAgendasPorNome(filter.getValorBusca()));
				break;
				
			case PERIODO :
				PeriodoDisponivel periodo = PeriodoDisponivel.buscarPor(filter.getValorBusca());
				agendas.addAll(buscarAgendasPorPeriodo(periodo));
				break;
			
			default: throw new BusinessException("Opção de busca inválida para agenda.");
		}
		
		return agendas;
	}
	
	private void normalizarEValidarNome(AgendaVo agendaVo) {
		agendaVo.setNome(Validador.validarTextoObrigatorio(agendaVo.getNome(), "O nome deve ser preenchido."));
	}
	
	private Long converterCodigo(String valorBusca) {
		return Validador.converterLong( valorBusca, "O código informado deve ser numérico.");
	}
	
	private void validarAgenda(AgendaVo agendaVo) {

	    if (agendaVo == null)
	        throw new BusinessException("Agenda inválida.");

	    normalizarEValidarNome(agendaVo);

	    if (agendaVo.getPeriodoDisponivel() == null)
	        throw new BusinessException("Selecione o período disponível.");
	}
	
	private void validarFiltro(AgendaFilter filter) {

	    if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException("Selecione uma opção de busca.");

	    String valorBusca = NormalizadorTexto.normalizarEspacos(filter.getValorBusca());

	    if (valorBusca == null || valorBusca.isEmpty())
	        throw new BusinessException("Informe um valor para a busca.");
	}
}
