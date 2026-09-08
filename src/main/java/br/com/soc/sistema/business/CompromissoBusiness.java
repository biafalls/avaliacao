package br.com.soc.sistema.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.util.NormalizadorTexto;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoBusiness {
	
	private static final String COMPROMISSO_NAO_ENCONTRADO = "Compromisso não encontrado.";
	private CompromissoDao dao;
	private FuncionarioBusiness funcionarioBusiness;
	private AgendaBusiness agendaBusiness;
	
	public CompromissoBusiness() {
		this.dao = new CompromissoDao();
		this.funcionarioBusiness = new FuncionarioBusiness();
		this.agendaBusiness = new AgendaBusiness();
	}
	
	public void cadastrarCompromisso(CompromissoVo compromissoVo) {
		validarCompromisso(compromissoVo);
		
		dao.insertCompromisso(compromissoVo);
	}
	
	public void atualizarCompromisso(CompromissoVo compromissoVo) {
		validarCompromisso(compromissoVo);
		
		boolean atualizado = dao.updateCompromisso(compromissoVo);
		
		if (!atualizado)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
	}
	
	public void excluirCompromisso(Long codigo) {
		boolean excluido = dao.deleteCompromisso(codigo);
		
		if (!excluido)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
	}
	
	public CompromissoVo buscarCompromissoParaEdicao(Long codigo) {
		CompromissoVo compromisso = dao.findByCodigo(codigo);
		
		if (compromisso == null)
			throw new BusinessException(COMPROMISSO_NAO_ENCONTRADO);
		
		return compromisso;
	}
	
	public List<CompromissoVo> buscarTodosCompromissos() {
		return dao.findAllCompromissos();
	}
	
	public List<CompromissoVo> filtrarCompromissos(CompromissoFilter filter) {
		
		validarFiltro(filter);
		
		String valorBusca =  NormalizadorTexto.normalizarEspacos(filter.getValorBusca());
		
		switch (filter.getOpcoesCombo()) {
			case ID:
				return buscarPorCodigo(valorBusca);
			
			case NOME_FUNCIONARIO:
				return dao.findAllByFuncionario(valorBusca);
				
			case NOME_AGENDA:
				return dao.findAllByAgenda(valorBusca);
				
			case DATA:
				validarData(valorBusca);
	            return dao.findAllByData(valorBusca);
			
			case HORA:
				validarHorario(valorBusca);
	            return dao.findAllByHorario(valorBusca);
			
			default:
				throw new BusinessException("Opção de busca inválida.");
		}
	}
	
	private List<CompromissoVo> buscarPorCodigo(String valorBusca) {

        Long codigo = converterCodigo(valorBusca);

        CompromissoVo compromisso = dao.findByCodigo(codigo);

        if (compromisso == null)
            return new ArrayList<>();

        return Arrays.asList(compromisso);
    }
	
	private void validarFiltro(CompromissoFilter filter) {

	    if (filter == null || filter.getOpcoesCombo() == null)
	        throw new BusinessException("Selecione uma opção de busca.");

	    String valorBusca = NormalizadorTexto.normalizarEspacos(filter.getValorBusca());

	    if (valorBusca == null || valorBusca.isEmpty())
	        throw new BusinessException("Informe um valor para a busca.");
	}
	
	private Long converterCodigo(String valorBusca) {
	    try {
	        return Long.parseLong(valorBusca);
	    } catch (NumberFormatException e) {
	        throw new BusinessException(
	            "O código informado deve ser numérico."
	        );
	    }
	}
	
	private void validarCompromisso(CompromissoVo compromissoVo) {
		if (compromissoVo == null)
			throw new BusinessException("Compromisso inválido.");
		  
		validarFuncionario(compromissoVo);
        AgendaVo agenda = validarAgenda(compromissoVo);
        validarData(compromissoVo.getData());
        LocalTime horario = validarHorario(compromissoVo.getHorario());
        
        validarPeriodoDisponivel(agenda, horario);
	}
	
    private void validarFuncionario(CompromissoVo compromissoVo) {
        if (compromissoVo.getFuncionario() == null || compromissoVo.getFuncionario().getRowid() == null) 
            throw new BusinessException("Selecione um funcionário.");
        
        funcionarioBusiness.buscarFuncionarioParaEdicao(compromissoVo.getFuncionario().getRowid());
    }

    private AgendaVo validarAgenda(CompromissoVo compromissoVo) {
        if (compromissoVo.getAgenda() == null || compromissoVo.getAgenda().getRowid() == null) 
            throw new BusinessException("Selecione uma agenda.");
        
        return agendaBusiness.buscarAgendaParaEdicao(compromissoVo.getAgenda().getRowid());
    }

    private void validarData(String data) {
        if (data == null || data.trim().isEmpty())
            throw new BusinessException("Informe a data.");

        try {
            LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Informe uma data válida.");
        }
    }

    private LocalTime validarHorario(String horario) {
        if (horario == null || horario.trim().isEmpty())
            throw new BusinessException("Informe o horário.");

        try {
            return LocalTime.parse(horario);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Informe um horário válido.");
        }
    }
	
    private void validarPeriodoDisponivel(AgendaVo agenda, LocalTime horario) {
    	LocalTime inicioTarde = LocalTime.of(12, 0);

        switch (agenda.getPeriodoDisponivel()) {
            case MANHA:
                if (!horario.isBefore(inicioTarde))
                    throw new BusinessException("O horário deve estar no período da manhã.");
                break;
                
            case TARDE:
                if (horario.isBefore(inicioTarde))
                    throw new BusinessException("O horário deve estar no período da tarde.");
                break;

            case AMBOS:
                break;
        }
    }
    
}
