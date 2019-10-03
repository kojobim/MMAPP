package com.bim.msf4j;

import org.wso2.msf4j.MicroservicesRunner;

import com.bim.msf4j.ctrl.HSMCtrl;
import com.bim.msf4j.ctrl.PingCtrl;

public class Application {
    public static void main(String[] args) {
        new MicroservicesRunner()
                .deploy(new PingCtrl(), new HSMCtrl())
                .start();
    }
}
