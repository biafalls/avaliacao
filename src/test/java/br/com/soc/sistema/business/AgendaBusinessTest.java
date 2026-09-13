package br.com.soc.sistema.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaBusinessTest {

	private AgendaDao dao;
	private CompromissoDao compromissoDao;
	private AgendaBusiness business;

	@BeforeEach
	void setUp() {
		dao = mock(AgendaDao.class);
		compromissoDao = mock(CompromissoDao.class);

		business = new AgendaBusiness(dao, compromissoDao);
	}

	@Test
	void deveCadastrarAgendaValida() {
		AgendaVo agenda = criarAgenda(null, "Agenda Médica", PeriodoDisponivel.MANHA);
		business.cadastrarAgenda(agenda);
		verify(dao).insertAgenda(agenda);
	}

	@Test
	void deveLancarErroQuandoAgendaForNula() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.cadastrarAgenda(null));
		assertEquals("Agenda inválida.", exception.getMessage());
		verify(dao, never()).insertAgenda(any());
	}

	@Test
	void deveLancarErroQuandoNomeDaAgendaForNulo() {
		AgendaVo agenda = criarAgenda(null, null, PeriodoDisponivel.MANHA);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.cadastrarAgenda(agenda));

		assertEquals("O nome deve ser preenchido.", exception.getMessage());
		verify(dao, never()).insertAgenda(any());
	}

	@Test
	void deveLancarErroQuandoPeriodoDaAgendaForNulo() {
		AgendaVo agenda = criarAgenda(null, "Agenda Médica", null);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.cadastrarAgenda(agenda));

		assertEquals("O período disponível deve ser informado.", exception.getMessage());
		verify(dao, never()).insertAgenda(any());
	}

	@Test
	void deveAtualizarAgendaValida() {
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		when(dao.updateAgenda(agenda)).thenReturn(true);

		business.atualizarAgenda(agenda);

		verify(dao).updateAgenda(agenda);
	}

	@Test
	void deveLancarErroQuandoCodigoParaAtualizacaoForNulo() {
		AgendaVo agenda = criarAgenda(null, "Agenda Médica", PeriodoDisponivel.MANHA);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.atualizarAgenda(agenda));

		assertEquals("O código da agenda deve ser informado.", exception.getMessage());
		verify(dao, never()).updateAgenda(any());
	}

	@Test
	void deveLancarErroQuandoAgendaParaAtualizacaoNaoForEncontrada() {
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		when(dao.updateAgenda(agenda)).thenReturn(false);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.atualizarAgenda(agenda));

		assertEquals("Agenda não encontrada.", exception.getMessage());
	}

	@Test
	void deveExcluirAgendaSemCompromissos() {
		when(compromissoDao.existeCompromissoPorAgenda(1L)).thenReturn(false);
		when(dao.deleteAgenda(1L)).thenReturn(true);

		business.excluirAgenda(1L);

		verify(dao).deleteAgenda(1L);
	}

	@Test
	void deveLancarErroQuandoAgendaPossuirCompromissos() {
		when(compromissoDao.existeCompromissoPorAgenda(1L)).thenReturn(true);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirAgenda(1L));

		assertEquals("Não é possível excluir uma agenda que possui compromissos cadastrados.", exception.getMessage());
		verify(dao, never()).deleteAgenda(anyLong());
	}

	@Test
	void deveLancarErroQuandoCodigoParaExclusaoForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirAgenda(null));

		assertEquals("O código da agenda deve ser informado.", exception.getMessage());
		verify(compromissoDao, never()).existeCompromissoPorAgenda(anyLong());
		verify(dao, never()).deleteAgenda(anyLong());
	}

	@Test
	void deveLancarErroQuandoAgendaParaExclusaoNaoForEncontrada() {
		when(compromissoDao.existeCompromissoPorAgenda(1L)).thenReturn(false);
		when(dao.deleteAgenda(1L)).thenReturn(false);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirAgenda(1L));

		assertEquals("Agenda não encontrada.", exception.getMessage());
		verify(dao).deleteAgenda(1L);
	}

	@Test
	void deveBuscarAgendaPorCodigo() {
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		when(dao.findByCodigo(1L)).thenReturn(agenda);

		AgendaVo resultado = business.buscarAgendaPorCodigo(1L);

		assertSame(agenda, resultado);
	}

	@Test
	void deveLancarErroQuandoAgendaNaoForEncontrada() {
		when(dao.findByCodigo(1L)).thenReturn(null);

		BusinessException exception = assertThrows(BusinessException.class, () -> business.buscarAgendaPorCodigo(1L));

		assertEquals("Agenda não encontrada.", exception.getMessage());
		verify(dao).findByCodigo(1L);
	}

	@Test
	void deveFiltrarAgendaPorId() {
		AgendaFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "1");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		when(dao.findByCodigo(1L)).thenReturn(agenda);

		List<AgendaVo> resultado = business.filtrarAgendas(filter);

		assertEquals(1, resultado.size());
		assertEquals(1L, resultado.get(0).getRowid());
	}

	@Test
	void deveLancarErroQuandoFiltroPorIdNaoForNumerico() {
		AgendaFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "abc");

		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarAgendas(filter));

		assertEquals("O código informado deve ser numérico.", exception.getMessage());
		verify(dao, never()).findByCodigo(anyLong());
	}

	@Test
	void deveFiltrarAgendaPorNome() {
		AgendaFilter filter = criarFiltro(OpcoesComboBuscar.NOME.getCodigo(), "Médica");
		AgendaVo agenda1 = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		AgendaVo agenda2 = criarAgenda(2L, "Agenda Médica II", PeriodoDisponivel.TARDE);
		List<AgendaVo> agendas = Arrays.asList(agenda1, agenda2);
		when(dao.findAllByNome("Médica")).thenReturn(agendas);

		List<AgendaVo> resultado = business.filtrarAgendas(filter);

		assertEquals(2, resultado.size());
	}

	@Test
	void deveFiltrarAgendaPorPeriodo() {
		AgendaFilter filter = criarFiltro(OpcoesComboBuscar.PERIODO.getCodigo(), PeriodoDisponivel.MANHA.getCodigo());
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		when(dao.findAllByPeriodo(PeriodoDisponivel.MANHA)).thenReturn(Arrays.asList(agenda));

		List<AgendaVo> resultado = business.filtrarAgendas(filter);

		assertEquals(1, resultado.size());
		assertEquals(PeriodoDisponivel.MANHA, resultado.get(0).getPeriodoDisponivel());
	}

	@Test
	void deveLancarErroQuandoFiltroForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarAgendas(null));
		assertEquals("Selecione uma opção de busca.", exception.getMessage());
	}

	@Test
	void deveLancarErroQuandoValorDeBuscaForNulo() {
		AgendaFilter filter = criarFiltro(OpcoesComboBuscar.NOME.getCodigo(), null);
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarAgendas(filter));
		assertEquals("Informe um valor para a busca.", exception.getMessage());
	}

	private AgendaVo criarAgenda(Long codigo, String nome, PeriodoDisponivel periodo) {
		AgendaVo agenda = new AgendaVo();
		agenda.setRowid(codigo);
		agenda.setNome(nome);
		agenda.setPeriodoDisponivel(periodo);
		return agenda;
	}

	private AgendaFilter criarFiltro(String opcao, String valor) {
		AgendaFilter filter = new AgendaFilter();
		filter.setOpcoesCombo(opcao);
		filter.setValorBusca(valor);
		return filter;
	}
}
