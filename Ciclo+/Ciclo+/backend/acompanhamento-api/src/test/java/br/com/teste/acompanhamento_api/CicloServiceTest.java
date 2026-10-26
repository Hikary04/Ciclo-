package br.com.teste.acompanhamento_api;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CicloServiceTest {

    private Ciclo ciclo(int id) {
        Ciclo c = new Ciclo();
        c.setIdCiclo(id);
        c.setIdUsuario(1);
        c.setDataInicio(LocalDate.of(2026, 9, 1));
        c.setDataFinal(LocalDate.of(2026, 9, 6));
        return c;
    }

    @Test
    void cadastraListaBuscaAtualizaExclui() {
        CicloService service = new CicloService();
        service.cadastrar(ciclo(1));

        assertEquals(1, service.listar().size());
        assertNotNull(service.buscar(1));
        assertNull(service.buscar(2));

        assertTrue(service.atualizar(1, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 8)));
        assertEquals(LocalDate.of(2026, 9, 8), service.buscar(1).getDataFinal());
        assertFalse(service.atualizar(2, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 8)));

        assertTrue(service.excluir(1));
        assertFalse(service.excluir(1));
    }

    @Test
    void listarRetornaCopiaImutavel() {
        CicloService service = new CicloService();
        service.cadastrar(ciclo(1));
        assertThrows(UnsupportedOperationException.class, () -> service.listar().clear());
    }
}
