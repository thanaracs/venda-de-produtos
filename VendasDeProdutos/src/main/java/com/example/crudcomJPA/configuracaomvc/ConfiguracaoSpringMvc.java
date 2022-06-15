package com.example.crudcomJPA.configuracaomvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracaoSpringMvc implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/cadastros").setViewName("links/cadastros");
        registry.addViewController("/consultas").setViewName("links/consultas");
        registry.addViewController("/vendas/compra-finalizada").setViewName("vendas/compra-finalizada");


    }

}


