package com.example.crudcomJPA.controleDeVendas.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tb_clientepf")
public class ClientePF  extends Cliente implements Serializable {
    @NotBlank (message = "Necessario informar nome do cliente")
    private String nome;

    @Positive (message = "Necessario informar o CPF do cliente")
    private String CPF;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }


}