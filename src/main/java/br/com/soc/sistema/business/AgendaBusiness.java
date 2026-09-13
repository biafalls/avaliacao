package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.util.Validador;
import br.com.soc.sistema.vo.AgendaVo;


public class AgendaBusiness {
	
	private static final String AGENDA_NAO_ENCONTRADA = "Agenda não encontrada.";
	private static final String AGENDA_INVALIDA = "Agenda inválida.";
	private static final String AGENDA_POSSUI_COMPROMISSOS = "Não é possível excluir uma agenda que possui compromissos cadastrados.";
	private static final String NOME_OBRIGATORIO = "O nome deve ser preenchido.";
	private static final String CODIGO_INVALIDO = "O código informado deve ser numérico.";
	private static final String CODIGO_OBRIGATORIO = "O código da agenda deve ser informado.";
	private static final String PERIODO_OBRIGATORIO = "O período disponível deve ser informado.";
	private static final String OPCAO_BUSCA_OBRIGATORIA = "Selecione uma opção de busca.";
	private static final String VALOR_BUSCA_OBRIGATORIO = "Informe um valor para a busca.";
	
	private AgendaDao dao;
	private CompromissoDao compromissoDao;
	
	public AgendaBusiness() {
		this(new AgendaDao(), new CompromissoDao());
	}
	
	public AgendaBusiness(AgendaDao dao, CompromissoDao compromissoDao) {
	    this.dao = dao;
	    this.compromissoDao = compromissoDao;
	}
	
	public void cadastrarAgenda(AgendaVo agendaVo) {
		validarAgenda(agendaVo);
		dao.insertAgenda(agendaVo);
	}
	
	public void atualizarAgenda(AgendaVo agendaVo) {
		validarAgenda(agendaVo);
		Validador.validarLongObrigatorio(agendaVo.getRowid(),CODIGO_OBRIGATORIO);

		boolean atualizado = dao.updateAgenda(agendaVo);
		
		if (!atualizado)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
	}
	
	public void excluirAgenda(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);
		
		if (compromissoDao.existeCompromissoPorAgenda(codigo))
			throw new BusinessException(AGENDA_POSSUI_COMPROMISSOS);
		
		boolean excluido = dao.deleteAgenda(codigo);
		
		if (!excluido)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
	}
	
	public AgendaVo buscarAgendaPorCodigo(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);
		
		AgendaVo agenda = dao.findByCodigo(codigo);
		
		if (agenda == null)
			throw new BusinessException(AGENDA_NAO_ENCONTRADA);
		
		return agenda;
	}
	
	public List<AgendaVo> buscarAgendasPorNome(String nome) {
		nome = Validador.validarTextoObrigatorio(nome, NOME_OBRIGATORIO);
		return dao.findAllByNome(nome);
	}
	
	public List<AgendaVo> buscarAgendasPorPeriodo(PeriodoDisponivel periodo) {
		if (periodo == null)
	        throw new BusinessException(PERIODO_OBRIGATORIO);
		
		return dao.findAllByPeriodo(periodo);
	}
	
	public List<AgendaVo> buscarTodasAsAgendas() {
		return dao.findAllAgendas();
	}
	
	public List<AgendaVo> filtrarAgendas(AgendaFilter filter) {
		
		String valorBusca = validarFiltro(filter);
		
		List<AgendaVo> agendas = new ArrayList<>();
		
		switch (filter.getOpcoesCombo()) {
			case ID :
				Long codigo = Validador.converterLong(valorBusca, CODIGO_INVALIDO);
				AgendaVo agenda = dao.findByCodigo(codigo);
				
				if (agenda != null)
					agendas.add(agenda);
				break;
			
			case NOME :
				agendas.addAll(buscarAgendasPorNome(valorBusca));
				break;
				
			case PERIODO :
				PeriodoDisponivel periodo = PeriodoDisponivel.buscarPor(valorBusca);
				agendas.addAll(buscarAgendasPorPeriodo(periodo));
				break;
			
			default: 
				throw new BusinessException("Opção de busca inválida para agenda.");
		}
		
		return agendas;
	}
	
	private void validarAgenda(AgendaVo agendaVo) {

	    if (agendaVo == null)
	        throw new BusinessException(AGENDA_INVALIDA);

	    agendaVo.setNome(Validador.validarTextoObrigatorio(agendaVo.getNome(), NOME_OBRIGATORIO));

	    if (agendaVo.getPeriodoDisponivel() == null)
	        throw new BusinessException(PERIODO_OBRIGATORIO);
	}
	
	private String validarFiltro(AgendaFilter filter) {
		
		if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException(OPCAO_BUSCA_OBRIGATORIA);

	    return Validador.validarTextoObrigatorio(filter.getValorBusca(), VALOR_BUSCA_OBRIGATORIO);
	}
}
