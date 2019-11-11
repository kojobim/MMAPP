package com.bim.commons.enums;

public enum AvisoPrivacidadFormatosEnum {
    
    HTML;

    public static AvisoPrivacidadFormatosEnum validarFormato(String formato) {
        switch(formato) {
            case "HTML":
                return AvisoPrivacidadFormatosEnum.HTML;

            default:
                return null;
        }
    }
}