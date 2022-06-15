package com.example.crudcomJPA.controleDeVendas.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
    public static void main(String[]args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}