package service;

import model.Cliente;
import repository.ClienteRepository;
import java.util.List;

public class ClienteService {
    private final ClienteRepository clienteRepository;
    
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    public Cliente cadastrarCliente(String nome, int idade, String telefone) {
        // Validações de negócio
        validarCliente(nome, idade, telefone);
        
        // Verificar se já existe cliente com mesmo nome
        Cliente existente = clienteRepository.buscarPorNome(nome);
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um cliente com o nome: " + nome);
        }
        
        // Criar e salvar novo cliente
        Cliente novoCliente = new Cliente(nome, idade, telefone);
        return clienteRepository.salvar(novoCliente);
    }
    
    public Cliente buscarClientePorId(int id) {
        Cliente cliente = clienteRepository.buscarPorId(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        return cliente;
    }
    
    public Cliente buscarClientePorNome(String nome) {
        return clienteRepository.buscarPorNome(nome);
    }
    
    public List<Cliente> listarTodosClientes() {
        return clienteRepository.listarTodos();
    }
    
    public Cliente atualizarCliente(int id, String nome, int idade, String telefone) {
        // Validações
        validarCliente(nome, idade, telefone);
        
        // Buscar cliente existente
        Cliente cliente = buscarClientePorId(id);
        
        // Atualizar dados
        cliente.setNome(nome);
        cliente.setIdade(idade);
        cliente.setTelefone(telefone);
        
        // Salvar alterações
        clienteRepository.atualizar(cliente);
        return cliente;
    }
    
    public void deletarCliente(int id) {
        // Verificar se existe antes de deletar
        buscarClientePorId(id);
        clienteRepository.deletar(id);
    }
    
    // VALIDAÇÕES DE NEGÓCIO
    private void validarCliente(String nome, int idade, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        
        if (nome.length() < 2 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 2 e 100 caracteres");
        }
        
        if (idade < 0 || idade > 150) {
            throw new IllegalArgumentException("Idade deve estar entre 0 e 150 anos");
        }
        
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        
        if (!telefone.matches("^[\\d\\s\\-\\(\\)\\+]+$")) {
            throw new IllegalArgumentException("Telefone em formato inválido");
        }
    }
    
    // MÉTODO ESPECÍFICO DE NEGÓCIO
    public boolean isClienteAdulto(int id) {
        Cliente cliente = buscarClientePorId(id);
        return cliente.getIdade() >= 18;
    }
}