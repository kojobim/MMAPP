package com.bim.commons.enums;

public enum TransferenciasProgramadasLimiteEnum {
	
	F("Limitar por fecha"),
	T("Limitar por cantidad"),
	I("Sin limite");
	
	private final String limite;
	
	public static String validarLimite(String limite) {
		switch (limite) {
		case "F":
			return TransferenciasProgramadasLimiteEnum.F.getLimite();

		case "T":
			return TransferenciasProgramadasLimiteEnum.T.getLimite();

		case "I":
			return TransferenciasProgramadasLimiteEnum.I.getLimite();

		default:
			return null;
		}
	}
	
	private TransferenciasProgramadasLimiteEnum(String limite) {
		this.limite = limite;
	}
	
	public String getLimite() {
		return this.limite;
	}
}