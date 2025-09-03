package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Agendamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private LocalDate data;
    private LocalTime hora;
    private String status; // "agendado", "concluído", "cancelado"
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servicos servico;
    
    // Construtores
    public Agendamentos() {}
    
    public Agendamentos(LocalDate data, LocalTime hora, Cliente cliente, Servicos servico) {
        this.data = data;
        this.hora = hora;
        this.cliente = cliente;
        this.servico = servico;
        this.status = "agendado";
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Servicos getServico() { return servico; }
    public void setServico(Servicos servico) { this.servico = servico; }
}