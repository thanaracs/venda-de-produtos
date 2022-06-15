package com.example.crudcomJPA.controleDeVendas.model;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "tb_itemvenda")
public class ItemVenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItemVenda;


    private int quantidade;

    @OneToOne(cascade = CascadeType.MERGE)
    private Produto produto;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Venda venda;

    public int getIdItemVenda() {
        return idItemVenda;
    }

    public void setId(int idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Venda getVenda() {
        return venda;
    }
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    public double TotalItem() {
        return this.produto.getValor() * this.quantidade;
    }

}