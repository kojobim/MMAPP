package com.bim.msf4j;

import com.bim.msf4j.ctrl.HSMCtrl;
import com.bim.msf4j.ctrl.InversionesCtrl;
import com.bim.msf4j.ctrl.LoginCtrl;
import com.bim.msf4j.ctrl.PingCtrl;
import com.bim.msf4j.ctrl.TokenCtrl;
import com.bim.msf4j.exceptions.BimExceptionMapper;

import org.wso2.msf4j.MicroservicesRunner;

public class Application {
    public static void main(String[] args) {
        new MicroservicesRunner()
        		.addExceptionMapper(new BimExceptionMapper())
                .deploy(new PingCtrl(), 
                		new TokenCtrl(), 
                		new HSMCtrl(), 
                		new LoginCtrl(),
                        new InversionesCtrl())
                .start();
    }
}
