package com.bim.commons.enums;

public enum CuentaDestinoTipoEnum {
    
    BIM, NACIONAL;

    public static CuentaDestinoTipoEnum validarTipoDestino(String tipo) {
        switch(tipo) {
            case "BIM":
                return CuentaDestinoTipoEnum.BIM;

            case "NACIONAL":
                return CuentaDestinoTipoEnum.NACIONAL;

            default:
                return null;
        }
    }
}