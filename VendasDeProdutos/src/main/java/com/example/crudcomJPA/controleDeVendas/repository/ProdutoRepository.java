package com.example.crudcomJPA.controleDeVendas.repository;

import com.example.crudcomJPA.controleDeVendas.model.Produto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProdutoRepository {
    @PersistenceContext
    private EntityManager em;

    public Produto produto(int idProduto) {
        return em.find(Produto.class, idProduto);
    }

    public void save(Produto produto) {
        em.persist(produto);
    }

    @SuppressWarnings("unchecked")
    public List<Produto> produtos() {
        Query query = em.createQuery("from Produto");
        return query.getResultList();
    }

    public void remove(int idProduto) {
        Produto p = em.find(Produto.class, idProduto);
        em.remove(p);
    }

    public void update(Produto produto) {
        em.merge(produto);
    }



}