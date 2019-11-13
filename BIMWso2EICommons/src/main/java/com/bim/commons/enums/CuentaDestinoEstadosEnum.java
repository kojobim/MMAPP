package com.bim.commons.enums;

public enum CuentaDestinoEstadosEnum {
    
    ACTIVO, PENDIENTE;

    public static CuentaDestinoEstadosEnum validarEstado(String estado) {
        switch(estado) {
            case "ACTIVO":
                return CuentaDestinoEstadosEnum.ACTIVO;

            case "PENDIENTE":
                return CuentaDestinoEstadosEnum.PENDIENTE;

            default:
                return null;
        }
    }
}