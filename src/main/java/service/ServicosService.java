package service;

import model.Servicos;
import repository.ServicosRepository;
import java.util.List;

public class ServicosService {
    private final ServicosRepository servicoRepository;
    
    public ServicosService(ServicosRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }
    
    public Servicos cadastrarServico(String tipo, String descricao, double preco, int duracaoMinutos) {
        // 1. VALIDAÇÕES DE NEGÓCIO
        validarServico(tipo, descricao, preco, duracaoMinutos);
        
        // 2. VERIFICAR SE JÁ EXISTE (opcional - depende da regra)
        List<Servicos> existentes = servicoRepository.buscarPorTipo(tipo);
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Já existe um serviço com este tipo: " + tipo);
        }
        
        // 3. CRIAR E SALVAR
        Servicos novoServico = new Servicos(tipo, descricao, preco, duracaoMinutos);
        return servicoRepository.salvar(novoServico);
    }
    
    public List<Servicos> listarTodosServicos() {
        return servicoRepository.listarTodos();
    }
    
    public List<Servicos> buscarServicosPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de serviço não pode ser vazio");
        }
        return servicoRepository.buscarPorTipo(tipo);
    }
    
    public Servicos buscarServicoPorId(int id) {
        Servicos servico = servicoRepository.buscarPorId(id);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + id);
        }
        return servico;
    }
    
    public void deletarServico(int id) {
        // Verificar se existe antes de deletar
        Servicos servico = servicoRepository.buscarPorId(id);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + id);
        }
        servicoRepository.deletar(id);
    }
    
    // VALIDAÇÕES DE NEGÓCIO
    private void validarServico(String tipo, String descricao, double preco, int duracaoMinutos) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do serviço é obrigatório");
        }
        
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do serviço é obrigatória");
        }
        
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        if (duracaoMinutos <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero");
        }
        
        if (tipo.length() < 2 || tipo.length() > 50) {
            throw new IllegalArgumentException("Tipo deve ter entre 2 e 50 caracteres");
        }
    }
    
    // MÉTODO PARA ATUALIZAR PREÇO (exemplo de regra de negócio)
    public Servicos atualizarPreco(int id, double novoPreco) {
        if (novoPreco <= 0) {
            throw new IllegalArgumentException("Novo preço deve ser maior que zero");
        }
        
        Servicos servico = buscarServicoPorId(id);
        servico.setPreco(novoPreco);
        return servicoRepository.salvar(servico);
    }
}