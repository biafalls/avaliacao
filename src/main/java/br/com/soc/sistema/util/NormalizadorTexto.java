package br.com.soc.sistema.util;

public class NormalizadorTexto {

	private NormalizadorTexto() {
	}

	public static String normalizarEspacos(String valor) {
		if (valor == null)
			return null;

		return valor.trim().replaceAll("\\s+", " ");
	}
}
