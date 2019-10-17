package com.bim.msf4j;

import com.bim.msf4j.ctrl.FiltroInversionesCtrl;
import com.bim.msf4j.ctrl.HSMCtrl;
import com.bim.msf4j.ctrl.InversionesCtrl;
import com.bim.msf4j.ctrl.LoginCtrl;
import com.bim.msf4j.ctrl.PaginadoCtrl;
import com.bim.msf4j.ctrl.PingCtrl;
import com.bim.msf4j.ctrl.TokenCtrl;

import org.wso2.msf4j.MicroservicesRunner;

public class Application {
    public static void main(String[] args) {
        new MicroservicesRunner()
                .deploy(new PingCtrl(), 
                		new TokenCtrl(), 
                		new HSMCtrl(), 
                		new PaginadoCtrl(),
                		new LoginCtrl(),
                        new FiltroInversionesCtrl(),
                        new InversionesCtrl())
                .start();
    }
}
