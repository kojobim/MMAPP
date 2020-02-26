package com.bim.commons.enums;

public enum InversionesCedeFormulasEnum {
    
    CEDE_VALOR("002"),
    CEDE_FIJA("001");
	
	private final String producto;

    public static String validarProducto(String producto) {
    	if(producto == null)
    		return null;
    	
        switch(producto) {
            case "CEDE_VALOR":
                return InversionesCedeFormulasEnum.CEDE_VALOR.getProducto();

            case "CEDE_FIJA":
                return InversionesCedeFormulasEnum.CEDE_FIJA.getProducto();

            default:
                return null;
        }
    }
    
    private InversionesCedeFormulasEnum(String producto) {
		this.producto = producto;
	}
	
	private String getProducto() {
		return this.producto;
	}
}