package com.bim.commons.enums;

public enum MesesEnum {
	Enero("01"),
	Febrero("02"),
	Marzo("03"),
	Abril("04"),
	Mayo("05"),
	Junio("06"),
	Julio("07"),
	Agosto("08"),
	Septiembre("09"),
	Octubre("10"),
	Noviembre("11"),
	Diciembre("12");
	
	private final String mes;
	
	public static String validaMes(String valor) {
		if(valor == null || valor.isEmpty())
			return null;
		
        switch(valor) {
            case "01":
                return MesesEnum.Enero.getMes();
            case "02":
                return MesesEnum.Febrero.getMes();
            case "03":
                return MesesEnum.Marzo.getMes();
            case "04":
                return MesesEnum.Abril.getMes();
            case "05":
                return MesesEnum.Mayo.getMes();
            case "06":
                return MesesEnum.Junio.getMes();
            case "07":
                return MesesEnum.Julio.getMes();
            case "08":
                return MesesEnum.Agosto.getMes();
            case "09":
                return MesesEnum.Septiembre.getMes();
            case "10":
                return MesesEnum.Octubre.getMes();
            case "11":
                return MesesEnum.Noviembre.getMes();
            case "12":
                return MesesEnum.Diciembre.getMes();

            default:
                return null;
        }
	}
	
	private MesesEnum(String mes) {
		this.mes = mes;
	}
	
	public String getMes() {
		return this.mes;
	}
}
