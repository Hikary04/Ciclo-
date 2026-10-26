package br.com.teste.acompanhamento_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService service;

    private Usuario novoUsuario(int id, String nome, String email) {
        Usuario u = new Usuario();
        u.setIdUsuario(id);
        u.setNome(nome);
        u.setEmail(email);
        u.setDataNascimento(LocalDate.of(2000, 1, 1));
        return u;
    }

    @BeforeEach
    void setUp() {
        service = new UsuarioService();
    }

    @Test
    void cadastraEBuscaUsuario() {
        assertTrue(service.cadastrar(novoUsuario(1, "Ana", "ana@example.com")));
        assertEquals("Ana", service.buscar(1).getNome());
        assertEquals(1, service.listar().size());
    }

    @Test
    void naoCadastraIdDuplicado() {
        service.cadastrar(novoUsuario(1, "Ana", "ana@example.com"));
        assertFalse(service.cadastrar(novoUsuario(1, "Outra", "outra@example.com")));
    }

    @Test
    void geraIdAutomaticoQuandoNaoInformado() {
        Usuario semId = novoUsuario(0, "Sem Id", "semid@example.com");
        assertTrue(service.cadastrar(semId));
        assertTrue(semId.getIdUsuario() > 0);

        Usuario segundo = novoUsuario(0, "Segundo", "segundo@example.com");
        service.cadastrar(segundo);
        assertNotEquals(semId.getIdUsuario(), segundo.getIdUsuario());
    }

    @Test
    void idAutomaticoNaoColideComIdInformado() {
        service.cadastrar(novoUsuario(10, "Dez", "dez@example.com"));
        Usuario auto = novoUsuario(0, "Auto", "auto@example.com");
        service.cadastrar(auto);
        assertEquals(11, auto.getIdUsuario());
    }

    @Test
    void detectaEmailDuplicadoIgnorandoCaixa() {
        service.cadastrar(novoUsuario(1, "Ana", "Ana@Example.com"));
        assertTrue(service.emailJaCadastrado("ana@example.com"));
        assertFalse(service.emailJaCadastrado("ninguem@example.com"));
        assertFalse(service.emailJaCadastrado(null));
    }

    @Test
    void atualizaNomeEExclui() {
        service.cadastrar(novoUsuario(1, "Ana", "ana@example.com"));
        assertTrue(service.atualizarNome(1, "Ana Paula"));
        assertEquals("Ana Paula", service.buscar(1).getNome());
        assertFalse(service.atualizarNome(99, "X"));

        assertTrue(service.excluir(1));
        assertFalse(service.excluir(1));
        assertNull(service.buscar(1));
    }
}
