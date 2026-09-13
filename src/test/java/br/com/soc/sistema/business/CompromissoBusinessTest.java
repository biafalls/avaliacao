package br.com.soc.sistema.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.filter.RelatorioCompromissoFilter;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoBusinessTest {

	private CompromissoDao dao;
	private FuncionarioBusiness funcionarioBusiness;
	private AgendaBusiness agendaBusiness;
	private CompromissoBusiness business;

	@BeforeEach
	void setUp() {
		dao = mock(CompromissoDao.class);
		funcionarioBusiness = mock(FuncionarioBusiness.class);
		agendaBusiness = mock(AgendaBusiness.class);

		business = new CompromissoBusiness(dao, funcionarioBusiness, agendaBusiness);
	}

	@Test
	void deveCadastrarCompromissoValido() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		business.cadastrarCompromisso(compromisso);

		verify(dao).insertCompromisso(compromisso);
	}

	@Test
	void deveLancarErroQuandoCompromissoForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.cadastrarCompromisso(null));

		assertEquals("Compromisso inválido.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoFuncionarioForNulo() {
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, null, agenda, "2026-09-15", "10:00");

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("O funcionário deve ser informado.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoAgendaForNula() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		CompromissoVo compromisso = criarCompromisso(null, funcionario, null, "2026-09-15", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("A agenda deve ser informada.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoDataForNula() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, null, "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("A data deve ser informada.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoDataForInvalida() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "data-invalida", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("Informe uma data válida.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoHorarioForNulo() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", null);
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("O horário deve ser informado.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoHorarioForInvalido() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", "horario-invalido");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("Informe um horário válido.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoHorarioForTardeEAgendaForManha() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", "14:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("Informe um horário no período da manhã.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void deveLancarErroQuandoHorarioForManhaEAgendaForTarde() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.TARDE);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarCompromisso(compromisso));

		assertEquals("Informe um horário no período da tarde.", exception.getMessage());
		verify(dao, never()).insertCompromisso(any());
	}

	@Test
	void devePermitirHorarioQuandoAgendaForAmbos() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.AMBOS);
		CompromissoVo compromisso = criarCompromisso(null, funcionario, agenda, "2026-09-15", "14:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);

		business.cadastrarCompromisso(compromisso);

		verify(dao).insertCompromisso(compromisso);
	}

	@Test
	void deveAtualizarCompromissoValido() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(1L, funcionario, agenda, "2026-09-15", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);
		when(dao.updateCompromisso(compromisso)).thenReturn(true);

		business.atualizarCompromisso(compromisso);

		verify(dao).updateCompromisso(compromisso);
	}

	@Test
	void deveLancarErroQuandoCompromissoParaAtualizacaoNaoForEncontrado() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		AgendaVo agenda = criarAgenda(1L, "Agenda Médica", PeriodoDisponivel.MANHA);
		CompromissoVo compromisso = criarCompromisso(1L, funcionario, agenda, "2026-09-15", "10:00");
		when(funcionarioBusiness.buscarFuncionarioPorCodigo(1L)).thenReturn(funcionario);
		when(agendaBusiness.buscarAgendaPorCodigo(1L)).thenReturn(agenda);
		when(dao.updateCompromisso(compromisso)).thenReturn(false);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.atualizarCompromisso(compromisso));

		assertEquals("Compromisso não encontrado.", exception.getMessage());
	}

	@Test
	void deveExcluirCompromissoValido() {
		when(dao.deleteCompromisso(1L)).thenReturn(true);
		business.excluirCompromisso(1L);
		verify(dao).deleteCompromisso(1L);
	}

	@Test
	void deveLancarErroQuandoCompromissoParaExclusaoNaoForEncontrado() {
		when(dao.deleteCompromisso(1L)).thenReturn(false);
		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirCompromisso(1L));
		assertEquals("Compromisso não encontrado.", exception.getMessage());
	}

	@Test
	void deveLancarErroQuandoCompromissoNaoForEncontrado() {
		when(dao.findByCodigo(1L)).thenReturn(null);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.buscarCompromissoPorCodigo(1L));

		assertEquals("Compromisso não encontrado.", exception.getMessage());
	}

	@Test
	void deveFiltrarCompromissoPorId() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "1");
		CompromissoVo compromisso = new CompromissoVo();
		compromisso.setRowid(1L);
		when(dao.findByCodigo(1L)).thenReturn(compromisso);

		List<CompromissoVo> resultado = business.filtrarCompromissos(filter);

		assertEquals(1, resultado.size());
		assertEquals(1L, resultado.get(0).getRowid());
	}

	@Test
	void deveLancarErroQuandoFiltroPorIdNaoForNumerico() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "abc");

		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarCompromissos(filter));

		assertEquals("O código informado deve ser numérico.", exception.getMessage());
		verify(dao, never()).findByCodigo(anyLong());
	}

	@Test
	void deveFiltrarCompromissoPorFuncionario() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.NOME_FUNCIONARIO.getCodigo(), "Beatriz");
		business.filtrarCompromissos(filter);
		verify(dao).findAllByFuncionario("Beatriz");
	}

	@Test
	void deveFiltrarCompromissoPorAgenda() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.NOME_AGENDA.getCodigo(), "Agenda Médica");
		business.filtrarCompromissos(filter);
		verify(dao).findAllByAgenda("Agenda Médica");
	}

	@Test
	void deveFiltrarCompromissoPorData() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.DATA.getCodigo(), "2026-09-15");
		business.filtrarCompromissos(filter);
		verify(dao).findAllByData("2026-09-15");
	}

	@Test
	void deveFiltrarCompromissoPorHorario() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.HORA.getCodigo(), "14:30");
		business.filtrarCompromissos(filter);
		verify(dao).findAllByHorario("14:30");
	}

	@Test
	void deveLancarErroQuandoFiltroForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarCompromissos(null));
		assertEquals("Selecione uma opção de busca.", exception.getMessage());
	}

	@Test
	void deveLancarErroQuandoValorDeBuscaForNulo() {
		CompromissoFilter filter = criarFiltro(OpcoesComboBuscar.NOME_FUNCIONARIO.getCodigo(), null);
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarCompromissos(filter));
		assertEquals("Informe um valor para a busca.", exception.getMessage());
	}

	@Test
	void deveBuscarCompromissosEntreDatasValidas() {
		RelatorioCompromissoFilter filter = criarFiltroRelatorio("2026-09-01", "2026-09-30");
		business.buscarCompromissosEntre(filter);
		verify(dao).findAllCompromissosEntre(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
	}

	@Test
	void deveLancarErroQuandoDataInicialForPosteriorADataFinal() {
		RelatorioCompromissoFilter filter = criarFiltroRelatorio("2026-09-30", "2026-09-01");

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.buscarCompromissosEntre(filter));

		assertEquals("A data inicial não pode ser posterior à data final.", exception.getMessage());
		verify(dao, never()).findAllCompromissosEntre(any(), any());
	}

	private CompromissoVo criarCompromisso(Long codigo, FuncionarioVo funcionario, AgendaVo agenda, String data,
			String horario) {
		CompromissoVo compromisso = new CompromissoVo();
		compromisso.setRowid(codigo);
		compromisso.setFuncionario(funcionario);
		compromisso.setAgenda(agenda);
		compromisso.setData(data);
		compromisso.setHorario(horario);
		return compromisso;
	}

	private FuncionarioVo criarFuncionario(Long codigo, String nome) {
		FuncionarioVo funcionario = new FuncionarioVo();
		funcionario.setRowid(codigo);
		funcionario.setNome(nome);
		return funcionario;
	}

	private AgendaVo criarAgenda(Long codigo, String nome, PeriodoDisponivel periodo) {
		AgendaVo agenda = new AgendaVo();
		agenda.setRowid(codigo);
		agenda.setNome(nome);
		agenda.setPeriodoDisponivel(periodo);
		return agenda;
	}

	private CompromissoFilter criarFiltro(String opcao, String valor) {
		CompromissoFilter filter = new CompromissoFilter();
		filter.setOpcoesCombo(opcao);
		filter.setValorBusca(valor);
		return filter;
	}

	private RelatorioCompromissoFilter criarFiltroRelatorio(String dataInicial, String dataFinal) {
		RelatorioCompromissoFilter filter = new RelatorioCompromissoFilter();
		filter.setDataInicial(dataInicial);
		filter.setDataFinal(dataFinal);
		return filter;
	}
}
