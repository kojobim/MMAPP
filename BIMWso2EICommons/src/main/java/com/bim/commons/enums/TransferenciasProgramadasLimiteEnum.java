package com.bim.commons.enums;

public enum TransferenciasProgramadasLimiteEnum {
	
	F, // Limita por fecha
	T, // Limita por cantidad
	I; // Sin limite
	
	public static TransferenciasProgramadasLimiteEnum validarLimite(String limite) {
		switch (limite) {
		case "F":
			return TransferenciasProgramadasLimiteEnum.F;

		case "T":
			return TransferenciasProgramadasLimiteEnum.T;

		case "I":
			return TransferenciasProgramadasLimiteEnum.I;

		default:
			return null;
		}
	}
}