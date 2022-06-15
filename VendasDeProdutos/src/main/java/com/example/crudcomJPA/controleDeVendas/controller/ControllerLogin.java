package com.example.crudcomJPA.controleDeVendas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Transactional
@Controller
public class ControllerLogin {

    @GetMapping("/login")
    public String form(){
        return "autenticacao/login";
    }
}