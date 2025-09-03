package service;

import model.Agendamentos;
import model.Cliente;
import model.Servicos;
import repository.AgendamentosRepository;
import repository.ClienteRepository;
import repository.ServicosRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendamentosService {
    private final AgendamentosRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicosRepository servicoRepository;
    
    public AgendamentosService(AgendamentosRepository agendamentoRepository,
                            ClienteRepository clienteRepository,
                            ServicosRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
    }
    
    public Agendamentos agendarServico(int clienteId, int servicoId, LocalDate data, LocalTime hora) {
        // 1. VALIDAÇÕES
        validarAgendamento(data, hora);
        
        // 2. VERIFICAR SE CLIENTE E SERVIÇO EXISTEM
        Cliente cliente = clienteRepository.buscarPorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId);
        }
        
        Servicos servico = servicoRepository.buscarPorId(servicoId);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + servicoId);
        }
        
        // 3. VERIFICAR CONFLITOS DE AGENDAMENTO
        verificarConflitoAgendamento(data, hora, servico);
        
        // 4. CRIAR E SALVAR AGENDAMENTO
        Agendamentos agendamento = new Agendamentos(data, hora, cliente, servico);
        return agendamentoRepository.salvar(agendamento);
    }
    
    public List<Agendamentos> listarTodosAgendamentos() {
        return agendamentoRepository.listarTodosCompletos();
    }
    
    public List<Agendamentos> listarAgendamentosPorData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return agendamentoRepository.listarPorData(data);
    }
    
    public List<Agendamentos> listarAgendamentosPorCliente(int clienteId) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId);
        }
        return agendamentoRepository.listarPorCliente(cliente);
    }
    
    public void cancelarAgendamento(int agendamentoId) {
        Agendamentos agendamento = agendamentoRepository.buscarPorId(agendamentoId);
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não encontrado com ID: " + agendamentoId);
        }
        
        if ("cancelado".equals(agendamento.getStatus())) {
            throw new IllegalStateException("Agendamento já está cancelado");
        }
        
        agendamentoRepository.atualizarStatus(agendamentoId, "cancelado");
    }
    
    public void concluirAgendamento(int agendamentoId) {
        Agendamentos agendamento = agendamentoRepository.buscarPorId(agendamentoId);
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não encontrado com ID: " + agendamentoId);
        }
        
        if ("concluído".equals(agendamento.getStatus())) {
            throw new IllegalStateException("Agendamento já está concluído");
        }
        
        agendamentoRepository.atualizarStatus(agendamentoId, "concluído");
    }
    
    // VALIDAÇÕES DE NEGÓCIO
    private void validarAgendamento(LocalDate data, LocalTime hora) {
        if (data == null) {
            throw new IllegalArgumentException("Data do agendamento é obrigatória");
        }
        
        if (hora == null) {
            throw new IllegalArgumentException("Hora do agendamento é obrigatória");
        }
        
        // Não permitir agendamentos no passado
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível agendar para datas passadas");
        }
        
        // Se for hoje, verificar horário
        if (data.isEqual(LocalDate.now()) && hora.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Não é possível agendar para horários passados");
        }
        
        // Validar horário comercial (exemplo: 8h às 18h)
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("Horário fora do expediente comercial (8h às 18h)");
        }
    }
    
    private void verificarConflitoAgendamento(LocalDate data, LocalTime hora, Servicos servico) {
        List<Agendamentos> agendamentosDoDia = agendamentoRepository.listarPorData(data);
        
        LocalTime horaFim = hora.plusMinutes(servico.getDuracaoMinutos());
        
        for (Agendamentos agendamento : agendamentosDoDia) {
            if (!"cancelado".equals(agendamento.getStatus())) {
                LocalTime horaInicioExistente = agendamento.getHora();
                LocalTime horaFimExistente = horaInicioExistente.plusMinutes(agendamento.getServico().getDuracaoMinutos());
                
                // Verifica se há sobreposição de horários
                if (hora.isBefore(horaFimExistente) && horaFim.isAfter(horaInicioExistente)) {
                    throw new IllegalStateException("Conflito de horário com agendamento existente: " +
                            agendamento.getCliente().getNome() + " - " +
                            horaInicioExistente + " às " + horaFimExistente);
                }
            }
        }
    }
}