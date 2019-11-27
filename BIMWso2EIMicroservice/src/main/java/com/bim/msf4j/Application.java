package com.bim.msf4j;

import org.wso2.msf4j.MicroservicesRunner;

import com.bim.msf4j.ctrl.AvisoPrivacidadCtrl;
import com.bim.msf4j.ctrl.CatalogosCtrl;
import com.bim.msf4j.ctrl.CuentaDestinoCtrl;
import com.bim.msf4j.ctrl.CuentasCtrl;
import com.bim.msf4j.ctrl.InversionesCtrl;
import com.bim.msf4j.ctrl.LoginCtrl;
import com.bim.msf4j.ctrl.LogoutCtrl;
import com.bim.msf4j.ctrl.PingCtrl;
import com.bim.msf4j.ctrl.TransferenciasBIMCtrl;
import com.bim.msf4j.ctrl.UsuarioCtrl;
import com.bim.msf4j.exceptions.BimExceptionMapper;

public class Application {
    public static void main(String[] args) {
        new MicroservicesRunner()
        		.addExceptionMapper(new BimExceptionMapper())
                .deploy(
                		new PingCtrl(), 
                		new LoginCtrl(),
                		new LogoutCtrl(),
                        new InversionesCtrl(),
                		new CuentaDestinoCtrl(),
                        new CuentasCtrl(),
                        new CatalogosCtrl(),
                        new AvisoPrivacidadCtrl(),
                        new UsuarioCtrl(),
                        new TransferenciasBIMCtrl())
                .start();
    }
}
