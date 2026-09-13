package br.com.soc.sistema.vo;

import br.com.soc.sistema.util.FormatadorDataHora;

public class CompromissoVo {

    private Long rowid;
    private FuncionarioVo funcionario = new FuncionarioVo();;
    private AgendaVo agenda = new AgendaVo();
    private String data;
    private String horario;
    
    public CompromissoVo() {}
    
    public CompromissoVo(Long rowid, FuncionarioVo funcionario, AgendaVo agenda, String data, String horario) {
    	this.rowid = rowid;
    	this.funcionario = funcionario;
    	this.agenda = agenda;
    	this.data = data;
    	this.horario = horario;
    }

	public Long getRowid() {
		return rowid;
	}

	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}

	public FuncionarioVo getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVo funcionario) {
		this.funcionario = funcionario;
	}

	public AgendaVo getAgenda() {
		return agenda;
	}

	public void setAgenda(AgendaVo agenda) {
		this.agenda = agenda;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}
	
	public String getDataFormatada() {
	    return FormatadorDataHora.formatarData(data);
	}

	public String getHorarioFormatado() {
	    return FormatadorDataHora.formatarHora(horario);
	}

	@Override
	public String toString() {
		return "CompromissoVo [rowid=" + rowid + ", funcionario=" + funcionario + ", agenda=" + agenda + ", data="
				+ data + ", horario=" + horario + "]";
	}
}
