package com.bim.commons.enums;

public enum InversionesCategoriasEnum {
    
    FIJA, VALOR, CEDE_RI, PAGARE;

    public static InversionesCategoriasEnum validarCategoria(String categoria) {
        switch(categoria) {
            case "FIJA":
                return InversionesCategoriasEnum.FIJA;

            case "VALOR":
                return InversionesCategoriasEnum.VALOR;

            case "CEDE_RI":
                return InversionesCategoriasEnum.CEDE_RI;

            case "PAGARE":
                return InversionesCategoriasEnum.PAGARE;

            default:
                return null;
        }
    }
}