package com.bim.commons.enums;

public enum TransferenciasProgramadasFrecuenciaEnum {

	U, // Unica vez
	D, // Diario
	S, // Semanal
	C, // Cada 2 semanas
	Q, // Quincenal
	M; // Mensual

	public static TransferenciasProgramadasFrecuenciaEnum validarFrecuencia(String frecuencia) {
		switch (frecuencia) {
		case "U":
			return TransferenciasProgramadasFrecuenciaEnum.U;

		case "D":
			return TransferenciasProgramadasFrecuenciaEnum.D;

		case "S":
			return TransferenciasProgramadasFrecuenciaEnum.S;

		case "C":
			return TransferenciasProgramadasFrecuenciaEnum.C;

		case "Q":
			return TransferenciasProgramadasFrecuenciaEnum.Q;

		case "M":
			return TransferenciasProgramadasFrecuenciaEnum.M;

		default:
			return null;
		}
	}
}