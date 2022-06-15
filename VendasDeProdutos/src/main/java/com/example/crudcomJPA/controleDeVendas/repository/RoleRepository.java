package com.example.crudcomJPA.controleDeVendas.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.example.crudcomJPA.controleDeVendas.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class RoleRepository {

    @PersistenceContext
    private EntityManager em;

    public Role role(long idRole) {
        return em.find(Role.class, idRole);
    }
}