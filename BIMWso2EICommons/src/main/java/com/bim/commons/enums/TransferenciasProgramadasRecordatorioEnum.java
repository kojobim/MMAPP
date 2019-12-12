package com.bim.commons.enums;

public enum TransferenciasProgramadasRecordatorioEnum {
	NO_RECORDARME(0, "No recordarme"),
	UN_DIA_ANTES(1, "1 día antes de enviar"),
	DOS_DIAS_ANTES(2, "2 dias antes de enviar"),
	TRES_DIAS_ANTES(3, "3 dias antes de enviar");

	private final Integer recordatorio;
	private final String descripcion;
	
	public static String validarRecordatorio(Integer recordatorio) {
			if(recordatorio == null)
				return null;
			
			switch (recordatorio) {
			case 0:
				return TransferenciasProgramadasRecordatorioEnum.NO_RECORDARME.getDescripcion();

			case 1:
				return TransferenciasProgramadasRecordatorioEnum.UN_DIA_ANTES.getDescripcion();

			case 2:
				return TransferenciasProgramadasRecordatorioEnum.DOS_DIAS_ANTES.getDescripcion();

			case 3:
				return TransferenciasProgramadasRecordatorioEnum.TRES_DIAS_ANTES.getDescripcion();
				
			default:
				return null;
			}
		}	
	
	private TransferenciasProgramadasRecordatorioEnum(Integer recordatorio, String descripcion) {
		this.recordatorio = recordatorio;
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}
}
