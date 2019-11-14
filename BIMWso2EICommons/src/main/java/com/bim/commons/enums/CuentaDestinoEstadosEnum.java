package com.bim.commons.enums;

public enum CuentaDestinoEstadosEnum {
    
    A, I;

    public static CuentaDestinoEstadosEnum validarEstado(String estado) {
        switch(estado) {
            case "ACTIVO":
                return CuentaDestinoEstadosEnum.A;

            case "PENDIENTE":
                return CuentaDestinoEstadosEnum.I;

            default:
                return null;
        }
    }
}