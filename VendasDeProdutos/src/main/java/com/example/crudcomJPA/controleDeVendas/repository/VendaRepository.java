package com.example.crudcomJPA.controleDeVendas.repository;


import com.example.crudcomJPA.controleDeVendas.model.ClientePF;
import com.example.crudcomJPA.controleDeVendas.model.Venda;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public class VendaRepository {
    @PersistenceContext
    private EntityManager em;

    public Venda Venda(int idVenda) {
        return em.find(Venda.class, idVenda);
    }

    public void save(Venda venda) {
        em.persist(venda);
    }

    @SuppressWarnings("unchecked")
    public List<Venda> vendas() {
        Query query = em.createQuery("from Venda");
        return query.getResultList();
    }

    public void remove(int idVenda){
        Venda v = em.find(Venda.class, idVenda);
        em.remove(v);
    }


    public void update(Venda venda) {
        em.merge(venda);
    }

    @SuppressWarnings("unchecked")
    public List<Venda> venda(int idVenda){
        String sql = "from Venda as v where v.idVenda = :idVenda";
        Query query = em.createQuery(sql, Venda.class);
        query.setParameter("idVenda", idVenda);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Venda> vendas(LocalDate localDate) {
        String hql = "from Venda as v where v.localDate = :localDate";
        Query query = em.createQuery(hql, Venda.class);
        query.setParameter("localDate", localDate);
        return query.getResultList();
    }
    @SuppressWarnings("unchecked")
    public List<Venda> clienteVendas(ClientePF clientePF) { //Listar todas as vendas de um cliente em seu acesso
        Query query = em.createQuery("from Venda as v where v.cliente = :clientePF", Venda.class);
        query.setParameter("clientePF", clientePF);
        return query.getResultList();
    }
}