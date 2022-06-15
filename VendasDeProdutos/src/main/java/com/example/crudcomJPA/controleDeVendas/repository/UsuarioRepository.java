package com.example.crudcomJPA.controleDeVendas.repository;

import com.example.crudcomJPA.controleDeVendas.model.ClientePF;
import com.example.crudcomJPA.controleDeVendas.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager em;

    public Usuario usuario(String login) {
        TypedQuery<Usuario> query = em.createQuery("from Usuario as u where u.login = :login", Usuario.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    public Usuario usuario(long idUsuario) {
        return em.find(Usuario.class, idUsuario);
    }

    public void save(Usuario usuario) {
        em.persist(usuario);
    }

    public void update(Usuario usuario) {
        em.merge(usuario);
    }

    public void remove(long idUsuario){
        Usuario u = em.find(Usuario.class, idUsuario);
        em.remove(u);
    }

    public Usuario usuarioCliente(ClientePF cliente) { //Retorna usuario pelo cliente
        TypedQuery<Usuario> query = em.createQuery("from Usuario as u where u.cliente = :cliente", Usuario.class);
        query.setParameter("cliente", cliente);
        return query.getSingleResult();
    }

}
