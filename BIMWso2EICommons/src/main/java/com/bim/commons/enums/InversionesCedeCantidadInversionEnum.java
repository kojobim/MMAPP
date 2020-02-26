package com.bim.commons.enums;

public enum InversionesCedeCantidadInversionEnum {
    
    CEDE_VALOR(5000.00),
    CEDE_FIJA(25000.00);

    private final Double cantidad;

    public static Double validarCantidadInversion(String producto) {
        if(producto == null)
            return null;
            
        switch(producto) {
            case "CEDE_VALOR":
                return InversionesCedeCantidadInversionEnum.CEDE_VALOR.getCantidad();

            case "CEDE_FIJA":
                return InversionesCedeCantidadInversionEnum.CEDE_FIJA.getCantidad();

            default:
                return null;
        }
    }
    
    private InversionesCedeCantidadInversionEnum(Double cantidad) {
        this.cantidad = cantidad;
	}
	
	private Double getCantidad() {
		return this.cantidad;
	}
}