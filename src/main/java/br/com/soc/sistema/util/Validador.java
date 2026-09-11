package br.com.soc.sistema.util;

import br.com.soc.sistema.exception.BusinessException;

public class Validador {

	private Validador() {
	}

	public static String validarTextoObrigatorio(String valor, String mensagem) {
		if (valor == null)
	        throw new BusinessException(mensagem);
		
		String normalizado = NormalizadorTexto.normalizarEspacos(valor);

		if (normalizado.isEmpty())
			throw new BusinessException(mensagem);

		return normalizado;
	}
	
	public static Long validarLongObrigatorio(Long valor, String mensagem) {

	    if (valor == null)
	        throw new BusinessException(mensagem);

	    return valor;
	}

	public static Long converterLong(String valor, String mensagem) {

		String normalizado = NormalizadorTexto.normalizarEspacos(valor);

		try {
			return Long.parseLong(normalizado);
		} catch (NumberFormatException e) {
			throw new BusinessException(mensagem);
		}
	}
}
