package com.example.crudcomJPA.controleDeVendas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //junção entre as chaves  estrangeiras
public abstract class Cliente implements Serializable {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int idCliente;

    //um para muitos no cliente e não no cliente pf, caso haja outro tipo de cliente
    @OneToMany(mappedBy = "cliente")
    private List<Venda> venda = new ArrayList<>();

    @JoinColumn(name = "id_usuario")
    @OneToOne(cascade = CascadeType.PERSIST)
    private Usuario usuario;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public List<Venda> getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda.add(venda);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}