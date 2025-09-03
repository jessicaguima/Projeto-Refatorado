package repository;

import model.Servicos;
import jakarta.persistence.EntityManager;
import java.util.List;
import util.JPAUtil;

public class ServicosRepository {
    
    public Servicos salvar(Servicos servico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(servico);
            em.getTransaction().commit();
            return servico;
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar serviço: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public List<Servicos> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {            
            String jpql = "SELECT s FROM Servicos s ORDER BY s.tipo";
            return em.createQuery(jpql, Servicos.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public Servicos buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Servicos.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Servicos> buscarPorTipo(String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT s FROM Servicos s WHERE LOWER(s.tipo) LIKE LOWER(:tipo) ORDER BY s.tipo";
            return em.createQuery(jpql, Servicos.class)
                    .setParameter("tipo", "%" + tipo + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public void deletar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Servicos servico = em.find(Servicos.class, id);
            if (servico != null) {
                em.remove(servico);
            }
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar serviço: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}