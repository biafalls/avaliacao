package br.com.soc.sistema.soap;

import java.util.List;

import javax.jws.WebService;

import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.enums.OpcoesComboBuscar;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.filter.RelatorioCompromissoFilter;
import br.com.soc.sistema.vo.CompromissoVo;

@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceCompromissos")
public class WebServiceCompromissosImpl implements WebServiceCompromissos {

	private CompromissoBusiness business;

	public WebServiceCompromissosImpl() {
		this.business = new CompromissoBusiness();
	}

	@Override
	public CompromissoVo buscarCompromisso(Long codigo) {
		return business.buscarCompromissoPorCodigo(codigo);
	}

	@Override
	public CompromissoVo[] listarCompromissos() {
		List<CompromissoVo> compromissos = business.buscarTodosCompromissos();
		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}

	@Override
	public CompromissoVo[] buscarCompromissosPorFuncionario(String nome) {
		CompromissoFilter filtro = new CompromissoFilter();

		filtro.setOpcoesCombo(OpcoesComboBuscar.NOME_FUNCIONARIO.getCodigo());
		filtro.setValorBusca(nome);

		List<CompromissoVo> compromissos = business.filtrarCompromissos(filtro);

		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}

	@Override
	public CompromissoVo[] buscarCompromissosPorAgenda(String nome) {
		CompromissoFilter filtro = new CompromissoFilter();

		filtro.setOpcoesCombo(OpcoesComboBuscar.NOME_AGENDA.getCodigo());
		filtro.setValorBusca(nome);

		List<CompromissoVo> compromissos = business.filtrarCompromissos(filtro);

		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}

	@Override
	public CompromissoVo[] buscarCompromissosPorData(String data) {
		CompromissoFilter filtro = new CompromissoFilter();

		filtro.setOpcoesCombo(OpcoesComboBuscar.DATA.getCodigo());
		filtro.setValorBusca(data);

		List<CompromissoVo> compromissos = business.filtrarCompromissos(filtro);

		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}

	@Override
	public CompromissoVo[] buscarCompromissosPorHorario(String horario) {
		CompromissoFilter filtro = new CompromissoFilter();

		filtro.setOpcoesCombo(OpcoesComboBuscar.HORA.getCodigo());
		filtro.setValorBusca(horario);

		List<CompromissoVo> compromissos = business.filtrarCompromissos(filtro);

		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}

	@Override
	public CompromissoVo[] buscarCompromissosEntre(String dataInicial, String dataFinal) {
		RelatorioCompromissoFilter filtro = new RelatorioCompromissoFilter();

		filtro.setDataInicial(dataInicial);
		filtro.setDataFinal(dataFinal);

		List<CompromissoVo> compromissos = business.buscarCompromissosEntre(filtro);

		return compromissos.toArray(new CompromissoVo[compromissos.size()]);
	}
}
