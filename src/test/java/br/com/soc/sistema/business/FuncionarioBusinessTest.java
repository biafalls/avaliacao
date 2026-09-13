package br.com.soc.sistema.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusinessTest {

	private FuncionarioDao dao;
	private FuncionarioBusiness business;

	@BeforeEach
	void setUp() {
		dao = mock(FuncionarioDao.class);
		business = new FuncionarioBusiness(dao);
	}

	@Test
	void deveCadastrarFuncionarioValido() {
		FuncionarioVo funcionario = criarFuncionario(null, "Beatriz");
		business.cadastrarFuncionario(funcionario);
		verify(dao).insertFuncionario(funcionario);
	}
	
	@Test
	void deveLancarErroQuandoFuncionarioForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.cadastrarFuncionario(null));

		assertEquals("Funcionário inválido.", exception.getMessage());
		verify(dao, never()).insertFuncionario(any());
	}
	
	@Test
	void deveLancarErroQuandoNomeParaCadastroForNulo() {
		FuncionarioVo funcionario = criarFuncionario(null, null);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.cadastrarFuncionario(funcionario));

		assertEquals("O nome deve ser preenchido.", exception.getMessage());
		verify(dao, never()).insertFuncionario(any());
	}

	@Test
	void deveAtualizarFuncionarioValido() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		when(dao.updateFuncionario(funcionario)).thenReturn(true);

		business.atualizarFuncionario(funcionario);
		
		verify(dao).updateFuncionario(funcionario);
	}

	@Test
	void deveLancarErroQuandoCodigoParaAtualizacaoForNulo() {
		FuncionarioVo funcionario = criarFuncionario(null, "Beatriz");

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.atualizarFuncionario(funcionario));

		assertEquals("O código do funcionário deve ser informado.", exception.getMessage());
		verify(dao, never()).updateFuncionario(any());
	}

	@Test
	void deveLancarErroQuandoFuncionarioParaAtualizarNaoForEncontrado() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		when(dao.updateFuncionario(funcionario)).thenReturn(false);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.atualizarFuncionario(funcionario));

		assertEquals("Funcionário não encontrado.", exception.getMessage());
	}

	@Test
	void deveExcluirFuncionarioValido() {
		when(dao.deleteFuncionarioComCompromisso(1L)).thenReturn(true);
		business.excluirFuncionario(1L);
		verify(dao).deleteFuncionarioComCompromisso(1L);
	}

	@Test
	void deveLancarErroQuandoCodigoParaExclusaoForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirFuncionario(null));

		assertEquals("O código do funcionário deve ser informado.", exception.getMessage());
		verify(dao, never()).deleteFuncionarioComCompromisso(anyLong());
	}

	@Test
	void deveLancarErroQuandoFuncionarioParaExclusaoNaoForEncontrado() {
		when(dao.deleteFuncionarioComCompromisso(1L)).thenReturn(false);
		BusinessException exception = assertThrows(BusinessException.class, () -> business.excluirFuncionario(1L));
		assertEquals("Funcionário não encontrado.", exception.getMessage());
	}

	@Test
	void deveBuscarFuncionarioPorCodigo() {
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		when(dao.findByCodigo(1L)).thenReturn(funcionario);

		FuncionarioVo funcionarioPesquisado = business.buscarFuncionarioPorCodigo(1L);

		assertSame(funcionario, funcionarioPesquisado);
	}

	@Test
	void deveLancarErroQuandoFuncionarioNaoForEncontrado() {
		when(dao.findByCodigo(1L)).thenReturn(null);

		BusinessException exception = assertThrows(BusinessException.class,
				() -> business.buscarFuncionarioPorCodigo(1L));

		assertEquals("Funcionário não encontrado.", exception.getMessage());
	}

	@Test
	void deveFiltrarFuncionarioPorId() {
		FuncionarioFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "1");
		FuncionarioVo funcionario = criarFuncionario(1L, "Beatriz");
		when(dao.findByCodigo(1L)).thenReturn(funcionario);

		List<FuncionarioVo> resultado = business.filtrarFuncionarios(filter);

		assertEquals(1, resultado.size());
		assertEquals(1L, resultado.get(0).getRowid());
	}

	@Test
	void deveLancarErroQuandoFiltroPorIdNaoForNumerico() {
		FuncionarioFilter filter = criarFiltro(OpcoesComboBuscar.ID.getCodigo(), "abc");

		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarFuncionarios(filter));

		assertEquals("O código informado deve ser numérico.", exception.getMessage());
		verify(dao, never()).findByCodigo(anyLong());
	}

	@Test
	void deveFiltrarFuncionariosPorNome() {
		FuncionarioFilter filter = criarFiltro(OpcoesComboBuscar.NOME.getCodigo(), "Beatriz");
		FuncionarioVo funcionario1 = criarFuncionario(1L, "Beatriz");
		FuncionarioVo funcionario2 = criarFuncionario(2L, "Ana Beatriz");
		List<FuncionarioVo> funcionarios = Arrays.asList(funcionario1, funcionario2);
		when(dao.findAllByNome("Beatriz")).thenReturn(funcionarios);

		List<FuncionarioVo> resultado = business.filtrarFuncionarios(filter);

		assertEquals(2, resultado.size());
	}

	@Test
	void deveLancarErroQuandoFiltroForNulo() {
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarFuncionarios(null));
		assertEquals("Selecione uma opção de busca.", exception.getMessage());
	}

	@Test
	void deveLancarErroQuandoValorDeBuscaForNulo() {
		FuncionarioFilter filter = criarFiltro(OpcoesComboBuscar.NOME.getCodigo(), null);
		BusinessException exception = assertThrows(BusinessException.class, () -> business.filtrarFuncionarios(filter));
		assertEquals("Informe um valor para a busca.", exception.getMessage());
	}

	private FuncionarioVo criarFuncionario(Long codigo, String nome) {
		FuncionarioVo funcionario = new FuncionarioVo();
		funcionario.setRowid(codigo);
		funcionario.setNome(nome);
		return funcionario;
	}

	private FuncionarioFilter criarFiltro(String opcao, String valor) {
		FuncionarioFilter filter = new FuncionarioFilter();
		filter.setOpcoesCombo(opcao);
		filter.setValorBusca(valor);
		return filter;
	}
}
