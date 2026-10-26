package br.com.teste.acompanhamento_api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SintomasServiceTest {

    private Sintomas sintoma(int id, String descricao) {
        Sintomas s = new Sintomas();
        s.setIdSintomas(id);
        s.setDescricao(descricao);
        s.setIntensidade(3);
        s.setIdCiclo(1);
        return s;
    }

    @Test
    void cadastroRejeitaIdDuplicado() {
        SintomasService service = new SintomasService();
        assertTrue(service.cadastrar(sintoma(1, "Cólica")));
        assertFalse(service.cadastrar(sintoma(1, "Cólica de novo")));
        assertEquals(1, service.listar().size());
    }

    @Test
    void atualizaEExclui() {
        SintomasService service = new SintomasService();
        service.cadastrar(sintoma(1, "Cólica"));

        Sintomas novo = sintoma(1, "Cólica intensa");
        novo.setIntensidade(5);
        assertTrue(service.atualizar(1, novo));
        assertEquals(5, service.buscar(1).getIntensidade());
        assertEquals("Cólica intensa", service.buscar(1).getDescricao());

        assertFalse(service.atualizar(999, novo));
        assertTrue(service.excluir(1));
        assertFalse(service.excluir(999));
    }
}
