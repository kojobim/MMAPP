package com.bim.commons.enums;

public enum InversionesCedeTiposEnum {
    
    CEDE_VALOR("07"),
    CEDE_FIJA("01");
	
	private final String producto;

    public static String validarProducto(String producto) {
        switch(producto) {
            case "CEDE_VALOR":
                return InversionesCedeTiposEnum.CEDE_VALOR.getProducto();

            case "CEDE_FIJA":
                return InversionesCedeTiposEnum.CEDE_FIJA.getProducto();

            default:
                return null;
        }
    }
    
    private InversionesCedeTiposEnum(String producto) {
		this.producto = producto;
	}
	
	private String getProducto() {
		return this.producto;
	}
}