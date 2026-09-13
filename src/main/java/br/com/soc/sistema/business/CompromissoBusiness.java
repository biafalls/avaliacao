package br.com.soc.sistema.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.filter.RelatorioCompromissoFilter;
import br.com.soc.sistema.util.Validador;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoBusiness {
	
	private static final String COMPROMISSO_NAO_ENCONTRADO = "Compromisso não encontrado.";
	private static final String COMPROMISSO_INVALIDO = "Compromisso inválido.";
	private static final String CODIGO_INVALIDO = "O código informado deve ser numérico.";
	private static final String CODIGO_OBRIGATORIO = "O código do compromisso deve ser informado.";
	private static final String OPCAO_BUSCA_OBRIGATORIA = "Selecione uma opção de busca.";
	private static final String VALOR_BUSCA_OBRIGATORIO = "Informe um valor para a busca.";
	private static final String FUNCIONARIO_OBRIGATORIO = "O funcionário deve ser informado.";
	private static final String AGENDA_OBRIGATORIA = "A agenda deve ser informada.";
	private static final String DATA_OBRIGATORIA = "A data deve ser informada.";
	private static final String DATA_INVALIDA = "Informe uma data válida.";
	private static final String HORARIO_OBRIGATORIO = "O horário deve ser informado.";
	private static final String HORARIO_INVALIDO = "Informe um horário válido.";
	
	private CompromissoDao dao;
	private FuncionarioBusiness funcionarioBusiness;
	private AgendaBusiness agendaBusiness;
	
	public CompromissoBusiness() {
		this(new CompromissoDao(), new FuncionarioBusiness(), new AgendaBusiness());
	}

	public CompromissoBusiness(CompromissoDao dao, FuncionarioBusiness funcionarioBusiness, AgendaBusiness agendaBusiness) {
		this.dao = dao;
		this.funcionarioBusiness = funcionarioBusiness;
		this.agendaBusiness = agendaBusiness;
	}
	
	public void cadastrarCompromisso(CompromissoVo compromissoVo) {
		validarCompromisso(compromissoVo);
		dao.insertCompromisso(compromissoVo);
	}
	
	public void atualizarCompromisso(CompromissoVo compromissoVo) {
		validarCompromisso(compromissoVo);
		Validador.validarLongObrigatorio(compromissoVo.getRowid(), CODIGO_OBRIGATORIO);
		
		boolean atualizado = dao.updateCompromisso(compromissoVo);
		
		if (!atualizado)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
	}
	
	public void excluirCompromisso(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);
		
		boolean excluido = dao.deleteCompromisso(codigo);
		
		if (!excluido)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
	}
	
	public CompromissoVo buscarCompromissoPorCodigo(Long codigo) {
		codigo = Validador.validarLongObrigatorio(codigo, CODIGO_OBRIGATORIO);

		CompromissoVo compromisso = dao.findByCodigo(codigo);
		
		if (compromisso == null)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
		
		return compromisso;
	}
	
	public List<CompromissoVo> buscarTodosCompromissos() {
		return dao.findAllCompromissos();
	}
	
	public List<CompromissoVo> filtrarCompromissos(CompromissoFilter filter) {
		
		String valorBusca = validarFiltro(filter);
		
		List<CompromissoVo> compromissos = new ArrayList<>();

		switch (filter.getOpcoesCombo()) {
			case ID:
				Long codigo = Validador.converterLong(valorBusca, CODIGO_INVALIDO);
	            CompromissoVo compromisso = dao.findByCodigo(codigo);

	            if (compromisso != null)
	                compromissos.add(compromisso);

	            break;
			
			case NOME_FUNCIONARIO:
				compromissos.addAll(dao.findAllByFuncionario(valorBusca));
	            break;
				
			case NOME_AGENDA:
				compromissos.addAll(dao.findAllByAgenda(valorBusca));
	            break;
				
			case DATA:
				validarData(valorBusca, DATA_OBRIGATORIA);
	            compromissos.addAll(dao.findAllByData(valorBusca));
	            break;
			
			case HORA:
				validarHorario(valorBusca);
	            compromissos.addAll(dao.findAllByHorario(valorBusca));
	            break;
			
			default:
				throw new BusinessException("Opção de busca inválida para compromisso.");
		}
		
		return compromissos;
	}
	
	public List<CompromissoVo> buscarCompromissosEntre(RelatorioCompromissoFilter filter) {
		LocalDate[] periodo = validarFiltroRelatorio(filter);
		return dao.findAllCompromissosEntre(periodo[0], periodo[1]);
	}
	
	private void validarCompromisso(CompromissoVo compromissoVo) {
		if (compromissoVo == null)
			throw new BusinessException(COMPROMISSO_INVALIDO);
		  
		validarFuncionario(compromissoVo);
        AgendaVo agenda = validarAgenda(compromissoVo);
        validarData(compromissoVo.getData(), DATA_OBRIGATORIA);
        LocalTime horario = validarHorario(compromissoVo.getHorario());
        
        validarPeriodoDisponivel(agenda, horario);
	}
	
    private void validarFuncionario(CompromissoVo compromissoVo) {
        if (compromissoVo.getFuncionario() == null || compromissoVo.getFuncionario().getRowid() == null) 
            throw new BusinessException(FUNCIONARIO_OBRIGATORIO);
        
        funcionarioBusiness.buscarFuncionarioPorCodigo(compromissoVo.getFuncionario().getRowid());
    }

    private AgendaVo validarAgenda(CompromissoVo compromissoVo) {
        if (compromissoVo.getAgenda() == null || compromissoVo.getAgenda().getRowid() == null) 
            throw new BusinessException(AGENDA_OBRIGATORIA);
        
        return agendaBusiness.buscarAgendaPorCodigo(compromissoVo.getAgenda().getRowid());
    }

    private LocalDate validarData(String data, String mensagemObrigatoria) {
        if (data == null || data.trim().isEmpty())
            throw new BusinessException(mensagemObrigatoria);

        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            throw new BusinessException(DATA_INVALIDA);
        }
    }

    private LocalTime validarHorario(String horario) {
        if (horario == null || horario.trim().isEmpty())
            throw new BusinessException(HORARIO_OBRIGATORIO);

        try {
            return LocalTime.parse(horario);
        } catch (DateTimeParseException e) {
            throw new BusinessException(HORARIO_INVALIDO);
        }
    }
	
    private void validarPeriodoDisponivel(AgendaVo agenda, LocalTime horario) {
    	LocalTime inicioTarde = LocalTime.of(12, 0);

        switch (agenda.getPeriodoDisponivel()) {
            case MANHA:
                if (!horario.isBefore(inicioTarde))
                    throw new BusinessException("Informe um horário no período da manhã.");
                break;
                
            case TARDE:
                if (horario.isBefore(inicioTarde))
                    throw new BusinessException("Informe um horário no período da tarde.");
                break;

            case AMBOS:
                break;
        }
    }
    
    private String validarFiltro(CompromissoFilter filter) {
		
		if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException(OPCAO_BUSCA_OBRIGATORIA);

	    return Validador.validarTextoObrigatorio(filter.getValorBusca(),VALOR_BUSCA_OBRIGATORIO);
	}
	
	private LocalDate[] validarFiltroRelatorio(RelatorioCompromissoFilter filter) {

	    if (filter == null)
	        throw new BusinessException("Filtro inválido.");

	    LocalDate dataInicial = validarData(filter.getDataInicial(), "A data inicial deve ser informada.");
	    LocalDate dataFinal = validarData(filter.getDataFinal(), "A data final deve ser informada.");

	    if (dataInicial.isAfter(dataFinal))
	    	throw new BusinessException("A data inicial não pode ser posterior à data final.");
	    
	    return new LocalDate[] {dataInicial,dataFinal};
	}
}
