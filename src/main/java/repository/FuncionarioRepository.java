package repository;

import model.Funcionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import util.JPAUtil;

public class FuncionarioRepository {
    
    public Funcionario buscarPorNomeESenha(String nome, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Funcionario> query = em.createQuery(
                "SELECT f FROM Funcionario f WHERE f.nome = :nome AND f.senha = :senha", 
                Funcionario.class);
            
            query.setParameter("nome", nome);
            query.setParameter("senha", senha);
            
            List<Funcionario> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
            
        } finally {
            em.close();
        }
    }
    
    public Funcionario salvar(Funcionario funcionario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(funcionario);
            em.getTransaction().commit();
            return funcionario;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    public Funcionario buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Funcionario> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}