import model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ClienteRepository;
import service.ClienteService;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService(new ClienteRepository());
    }

    @Test
    void deveCadastrarClienteValido() {
        Cliente cliente = clienteService.cadastrarCliente("Wagner", 25, "(11) 9999-8888");
        assertNotNull(cliente);
        assertEquals("Wagner", cliente.getNome());
    }

    @Test
    void deveFalharAoCadastrarClienteComNomeVazio() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrarCliente("", 25, "(11) 9999-8888");
        });
        assertEquals("Nome do cliente é obrigatório", e.getMessage());
    }
}
