package br.com.teste.acompanhamento_api;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Reproduz, de forma automatizada, os casos da coleção Postman
 * (tests/postman/colecao_postman.json).
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    // ---------- 01 - Usuarios ----------

    @Test
    @Order(1)
    void usuarioInexistenteRetorna404() throws Exception {
        mvc.perform(get("/usuarios/999")).andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void cadastrarUsuarioComIdRetorna201() throws Exception {
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idUsuario\": 3, \"nome\": \"Carlos Teste\", \"dataNascimento\": \"2002-03-20\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(3))
                .andExpect(jsonPath("$.nome").value("Carlos Teste"));

        mvc.perform(get("/usuarios/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataNascimento").value("2002-03-20"));
    }

    @Test
    @Order(3)
    void cadastrarUsuarioSemIdGeraIdAutomatico() throws Exception {
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Maria Front\", \"email\": \"maria@example.com\", \"dataNascimento\": \"1999-05-10\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(4))
                .andExpect(jsonPath("$.email").value("maria@example.com"));
    }

    @Test
    @Order(4)
    void cadastrarUsuarioComEmailRepetidoRetorna409() throws Exception {
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Outra\", \"email\": \"MARIA@example.com\", \"dataNascimento\": \"1999-05-10\" }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("E-mail já cadastrado"));
    }

    @Test
    @Order(5)
    void cadastrarUsuarioComIdRepetidoRetorna409() throws Exception {
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idUsuario\": 3, \"nome\": \"Duplicado\", \"dataNascimento\": \"2002-03-20\" }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("ID já cadastrado"));
    }

    @Test
    @Order(6)
    void cadastrarUsuarioSemNomeRetorna400() throws Exception {
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idUsuario\": 5, \"nome\": \"  \", \"dataNascimento\": \"2002-03-20\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Dados inválidos"));
    }

    @Test
    @Order(7)
    void atualizarNomeDoUsuario() throws Exception {
        mvc.perform(put("/usuarios/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Vanessa Atualizado\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vanessa Atualizado"));

        mvc.perform(put("/usuarios/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"\" }"))
                .andExpect(status().isBadRequest());

        mvc.perform(put("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"X\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void excluirUsuario() throws Exception {
        mvc.perform(delete("/usuarios/3")).andExpect(status().isNoContent());
        mvc.perform(delete("/usuarios/3")).andExpect(status().isNotFound());
    }

    // ---------- 02 - Ciclos ----------

    @Test
    @Order(10)
    void cadastrarCicloValido() throws Exception {
        mvc.perform(post("/ciclos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idCiclo\": 1, \"idUsuario\": 1, \"dataInicio\": \"2026-09-01\", \"dataFinal\": \"2026-09-06\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCiclo").value(1));

        mvc.perform(get("/ciclos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(11)
    void cadastrarCicloComDataFinalAntesDaInicialRetorna400() throws Exception {
        mvc.perform(post("/ciclos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idCiclo\": 2, \"idUsuario\": 1, \"dataInicio\": \"2026-10-10\", \"dataFinal\": \"2026-10-05\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados do ciclo inválidos"));
    }

    @Test
    @Order(12)
    void cadastrarCicloDuplicadoRetorna409() throws Exception {
        mvc.perform(post("/ciclos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idCiclo\": 1, \"idUsuario\": 1, \"dataInicio\": \"2026-09-01\", \"dataFinal\": \"2026-09-06\" }"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(13)
    void atualizarEExcluirCiclo() throws Exception {
        mvc.perform(put("/ciclos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"dataInicio\": \"2026-09-01\", \"dataFinal\": \"2026-09-08\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataFinal").value("2026-09-08"));

        mvc.perform(put("/ciclos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"dataInicio\": \"2026-09-01\", \"dataFinal\": \"2026-09-08\" }"))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/ciclos/1")).andExpect(status().isNoContent());
        mvc.perform(delete("/ciclos/1")).andExpect(status().isNotFound());
    }

    // ---------- 03 - Sintomas ----------

    @Test
    @Order(20)
    void cadastrarSintomaEDuplicado() throws Exception {
        String corpo = "{ \"idSintomas\": 1, \"descricao\": \"Colica menstrual\", \"intensidade\": 3, \"idCiclo\": 1 }";

        mvc.perform(post("/sintomas").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated());

        mvc.perform(post("/sintomas").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict())
                .andExpect(content().string("Sintoma já cadastrado"));

        mvc.perform(get("/sintomas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(21)
    void cadastrarSintomaSemDescricaoRetorna400() throws Exception {
        mvc.perform(post("/sintomas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idSintomas\": 2, \"descricao\": \"\", \"intensidade\": 3, \"idCiclo\": 1 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(22)
    void atualizarEExcluirSintoma() throws Exception {
        mvc.perform(put("/sintomas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"descricao\": \"Colica menstrual intensa\", \"intensidade\": 5, \"idCiclo\": 1 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intensidade").value(5));

        mvc.perform(put("/sintomas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"descricao\": \"Teste de sintoma inexistente\", \"intensidade\": 3, \"idCiclo\": 1 }"))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/sintomas/1")).andExpect(status().isNoContent());
        mvc.perform(delete("/sintomas/999")).andExpect(status().isNotFound());
    }

    // ---------- CORS ----------

    @Test
    @Order(30)
    void preflightCorsDoFrontEhAceito() throws Exception {
        mvc.perform(options("/usuarios")
                        .header("Origin", "http://localhost:5500")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
