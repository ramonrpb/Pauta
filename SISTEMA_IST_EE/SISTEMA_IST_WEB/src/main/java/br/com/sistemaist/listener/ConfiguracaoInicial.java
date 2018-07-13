package br.com.sistemaist.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfiguracaoInicial implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	//Para não passar null no retorno dos inputs quando não preenchidos
        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //Sem ação
    }

}