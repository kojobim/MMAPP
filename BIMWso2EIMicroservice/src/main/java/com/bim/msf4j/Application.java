package com.bim.msf4j;

import org.wso2.msf4j.MicroservicesRunner;

import com.bim.msf4j.ctrl.HSMCtrl;
import com.bim.msf4j.ctrl.InversionesCtrl;
import com.bim.msf4j.ctrl.PaginadoCtrl;
import com.bim.msf4j.ctrl.PingCtrl;
import com.bim.msf4j.ctrl.TokenCtrl;
import com.bim.msf4j.ctrl.FiltroInversionesCtrl;

public class Application {
    public static void main(String[] args) {
        new MicroservicesRunner()
                .deploy(
                    new PingCtrl(),
                    new TokenCtrl(),
                    new HSMCtrl(),
                    new PaginadoCtrl(),
                    new FiltroInversionesCtrl(),
                    new InversionesCtrl()
                ).start();
    }
}
