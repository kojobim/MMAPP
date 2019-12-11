package com.bim.commons.enums;

public enum TransferenciasProgramadasFrecuenciaEnum {

	U("Única vez"),
	D("Diario"),
	S("Semanal"),
	C("Cada 2 semanas"),
	Q("Quincenal"), 
	M("Mensual");

	private final String frecuencia;
	
	public static String validarFrecuencia(String frecuencia) {
		if(frecuencia == null || frecuencia.isEmpty())
			return null;
		
		switch (frecuencia) {
		case "U":
			return TransferenciasProgramadasFrecuenciaEnum.U.getFrecuencia();

		case "D":
			return TransferenciasProgramadasFrecuenciaEnum.D.getFrecuencia();

		case "S":
			return TransferenciasProgramadasFrecuenciaEnum.S.getFrecuencia();

		case "C":
			return TransferenciasProgramadasFrecuenciaEnum.C.getFrecuencia();

		case "Q":
			return TransferenciasProgramadasFrecuenciaEnum.Q.getFrecuencia();

		case "M":
			return TransferenciasProgramadasFrecuenciaEnum.M.getFrecuencia();

		default:
			return null;
		}
	}
	
	private TransferenciasProgramadasFrecuenciaEnum(String frecuencia) {
		this.frecuencia = frecuencia;
	}
	
	public String getFrecuencia() {
		return this.frecuencia;
	}
}