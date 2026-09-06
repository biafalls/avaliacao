package br.com.soc.sistema.vo;

import br.com.soc.sistema.enums.PeriodoDisponivel;

public class AgendaVo {
	
	private Long rowid;
	private String nome;
	private PeriodoDisponivel periodoDisponivel;
	
	public AgendaVo() {}
	
	public AgendaVo(Long rowid, String nome, PeriodoDisponivel periodoDisponivel) {
		this.rowid = rowid;
		this.nome = nome;
		this.periodoDisponivel = periodoDisponivel;
	} 
	
	public Long getRowid() {
		return rowid;
	}
	
	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public PeriodoDisponivel getPeriodoDisponivel() {
		return periodoDisponivel;
	}

	public void setPeriodoDisponivel(PeriodoDisponivel periodoDisponivel) {
		this.periodoDisponivel = periodoDisponivel;
	}

	@Override
	public String toString() {
		return "AgendaVo [rowid=" + rowid + ", nome=" + nome + ", periodoDisponivel=" + periodoDisponivel + "]";
	}
}
