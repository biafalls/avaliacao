package br.com.soc.sistema.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import br.com.soc.sistema.exception.BusinessException;

public enum PeriodoDisponivel {
	
	MANHA("1", "Manhã"),
	TARDE("2", "Tarde"),
	AMBOS("3", "Ambos");
	
	private String codigo;
	private String descricao;
	
	private final static Map<String, PeriodoDisponivel> periodos = new HashMap<>();
	
	static {
		Arrays.asList(PeriodoDisponivel.values())
		.forEach(periodo -> periodos.put(periodo.getCodigo(), periodo));
	}
	
	private PeriodoDisponivel(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static PeriodoDisponivel buscarPor(String codigo) {
		if (codigo == null)
			throw new IllegalArgumentException("Informe um código válido");

		return getPeriodo(codigo)
				.orElseThrow(() -> 
					new BusinessException("Período informado não existe")
				);
	}

	private static Optional<PeriodoDisponivel> getPeriodo(String codigo) {
		return Optional.ofNullable(periodos.get(codigo));
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
}
