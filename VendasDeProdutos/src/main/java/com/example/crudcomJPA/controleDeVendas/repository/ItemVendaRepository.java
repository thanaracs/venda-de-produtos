package com.example.crudcomJPA.controleDeVendas.repository;

import com.example.crudcomJPA.controleDeVendas.model.ItemVenda;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
@Repository
public class ItemVendaRepository {

    @PersistenceContext
    private EntityManager em;

    public ItemVenda ItemVenda(int idItemVenda) {
        return em.find(ItemVenda.class, idItemVenda);
    }

    public void save(ItemVenda itemVenda) {
        em.persist(itemVenda);
    }

    @SuppressWarnings("unchecked")
    public List<ItemVenda> itemVendas() {
        Query query = em.createQuery("from ItemVenda");
        return query.getResultList();
    }
    @SuppressWarnings("unchecked")
    public List<ItemVenda> itemVendas(int idVenda) {
        String sql = "from ItemVenda as i where i.venda.idVenda = :idVenda";
        Query query = em.createQuery(sql,ItemVenda.class);
        query.setParameter("idVenda", idVenda);
        return query.getResultList();
    }
    public void remove(int idItemVenda) {
        ItemVenda i = em.find(ItemVenda.class, idItemVenda);
        em.remove(i);
    }

    public void update(ItemVenda itemVenda){
        em.merge(itemVenda);
    }
}
