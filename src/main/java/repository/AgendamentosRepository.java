package repository;

import model.Agendamentos;
import model.Cliente;
import model.Servicos;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import util.JPAUtil;

public class AgendamentosRepository {
    
    public Agendamentos salvar(Agendamentos agendamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Garante que cliente e serviço estão managed
            agendamento.setCliente(em.merge(agendamento.getCliente()));
            agendamento.setServico(em.merge(agendamento.getServico()));
            
            em.persist(agendamento);
            em.getTransaction().commit();
            return agendamento;
            
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar agendamento: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public List<Agendamentos> listarTodosCompletos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Agendamentos a " +
                         "JOIN FETCH a.cliente c " +
                         "JOIN FETCH a.servico s " +
                         "ORDER BY a.data DESC, a.hora DESC";
            
            return em.createQuery(jpql, Agendamentos.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Agendamentos> listarPorData(LocalDate data) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Agendamentos a " +
                         "JOIN FETCH a.cliente c " +
                         "JOIN FETCH a.servico s " +
                         "WHERE a.data = :data " +
                         "ORDER BY a.hora ASC";
            
            return em.createQuery(jpql, Agendamentos.class)
                    .setParameter("data", data)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Agendamentos> listarPorCliente(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Agendamentos a " +
                         "JOIN FETCH a.cliente c " +
                         "JOIN FETCH a.servico s " +
                         "WHERE a.cliente.id = :clienteId " +
                         "ORDER BY a.data DESC, a.hora DESC";
            
            return em.createQuery(jpql, Agendamentos.class)
                    .setParameter("clienteId", cliente.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public Agendamentos buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Agendamentos a " +
                         "JOIN FETCH a.cliente c " +
                         "JOIN FETCH a.servico s " +
                         "WHERE a.id = :id";
            
            return em.createQuery(jpql, Agendamentos.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public void atualizarStatus(int id, String status) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Agendamentos agendamento = em.find(Agendamentos.class, id);
            if (agendamento != null) {
                agendamento.setStatus(status);
                em.merge(agendamento);
            }
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar status: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}