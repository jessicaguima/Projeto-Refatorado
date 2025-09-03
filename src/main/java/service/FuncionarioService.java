package service;

import model.Funcionario;
import repository.FuncionarioRepository;

public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }
    
    public Funcionario autenticar(String nome, String senha) {
        // 1. VALIDAÇÕES
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        
        // 2. TENTAR AUTENTICAR
        Funcionario funcionario = funcionarioRepository.buscarPorNomeESenha(nome, senha);
        
        if (funcionario == null) {
            throw new SecurityException("Credenciais inválidas");
        }
        
        return funcionario;
    }
    
    public Funcionario cadastrarFuncionario(String nome, String senha) {
        // 1. VALIDAÇÕES
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        
        if (senha.length() < 4) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 4 caracteres");
        }
        
        // 2. VERIFICAR SE JÁ EXISTE
        // (Implementar método no repository se necessário)
        
        // 3. CRIAR E SALVAR
        Funcionario novoFuncionario = new Funcionario(nome, senha);
        return funcionarioRepository.salvar(novoFuncionario);
    }
}