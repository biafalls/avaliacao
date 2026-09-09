package br.com.soc.sistema.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.filter.RelatorioCompromissoFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.util.RelatorioExcel;
import br.com.soc.sistema.vo.CompromissoVo;

public class RelatorioCompromissoAction extends Action{
	
	private CompromissoBusiness business = new CompromissoBusiness();
    private RelatorioCompromissoFilter filtro = new RelatorioCompromissoFilter();
    private List<CompromissoVo> compromissos = new ArrayList<>();
    private InputStream arquivoExcel;
    
    public String abrir() {
        return SUCCESS;
    }

    public String gerar() {
        try {
            compromissos = business.buscarCompromissosEntre(filtro);

            if (compromissos.isEmpty())
            	addActionError("Nenhum compromisso encontrado no período informado.");

            return SUCCESS;

        } catch (BusinessException e) {
            addActionError(e.getMessage());
            return SUCCESS;

        } catch (TechnicalException e) {
            addActionError("Não foi possível gerar o relatório. Tente novamente.");
            return SUCCESS;
        }
    }
    
    public String exportar() {
        try {
        	compromissos = business.buscarCompromissosEntre(filtro);
        	
        	if (compromissos.isEmpty()) {
        	    addActionError("Nenhum compromisso encontrado para exportação.");
        	    return SUCCESS;
        	}
        	
        	RelatorioExcel relatorioExcel = new RelatorioExcel();
        	
        	arquivoExcel = new ByteArrayInputStream(relatorioExcel.gerar(compromissos));
        	
        	return EXCEL;
        	 
        } catch (BusinessException e) {
            addActionError(e.getMessage());
            return SUCCESS;

        } catch (TechnicalException e) {
            addActionError("Não foi possível gerar o relatório Excel. Tente novamente.");
            return SUCCESS;
        }  
    }
    
    public RelatorioCompromissoFilter getFiltro() {
        return filtro;
    }

    public void setFiltro(RelatorioCompromissoFilter filtro) {
        this.filtro = filtro;
    }

    public List<CompromissoVo> getCompromissos() {
        return compromissos;
    }
    
    public InputStream getArquivoExcel() {
        return arquivoExcel;
    }
}
