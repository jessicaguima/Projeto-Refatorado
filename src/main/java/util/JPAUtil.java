package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    /* Nome da unidade de persistência, se for necessário mudar muda aqui */
    private static final String PERSISTENCE_UNIT = "ProjetoIntegradorSalao-PI";
    
    /* Entity Manager Factory gerencia a criação do EntityManager */
    private static EntityManagerFactory fabrica; 
    
    /* Emtity Manager é a interação com o banco de dados */
    private static EntityManager em;
    
    
    /* Método para verificar se a fabrica ou o em estão vazios ou fechados */
    public static EntityManager getEntityManager(){
        if(fabrica == null || !fabrica.isOpen())
            fabrica = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);        
        if(em == null || !em.isOpen()) 
            em = fabrica.createEntityManager();
        return em;
    }
    
    /* Método para fechar o EntityManager, Evitar vazamentos */
    public static void closeEntityManager(){
        if(em.isOpen() && em != null){
            em.close();
            fabrica.close();
        }
    }
}
