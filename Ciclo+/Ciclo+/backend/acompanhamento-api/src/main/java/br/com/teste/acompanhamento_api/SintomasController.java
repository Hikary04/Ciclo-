package br.com.teste.acompanhamento_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sintomas")
public class SintomasController {

    private final SintomasService sintomasService;

    public SintomasController(SintomasService sintomasService) {
        this.sintomasService = sintomasService;
    }

    // CONSULTAR todos os sintomas
    @GetMapping
    public List<Sintomas> listar() {
        return sintomasService.listar();
    }

    // CADASTRAR um sintoma
    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody Sintomas sintoma) {

        // Verificar se os dados são válidos
        if (sintoma.getIdSintomas() <= 0 ||
                sintoma.getIdCiclo() <= 0 ||
                sintoma.getDescricao() == null ||
                sintoma.getDescricao().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Dados do sintoma inválidos");
        }

        // Verificar se já existe esse ID
        boolean cadastrado =
                sintomasService.cadastrar(sintoma);

        if (!cadastrado) {
            return ResponseEntity.status(409)
                    .body("Sintoma já cadastrado");
        }

        // Cadastro realizado com sucesso
        return ResponseEntity.status(201).body(sintoma);
    }

    // ATUALIZAR um sintoma
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody Sintomas dados) {

        // Verificar se os dados são válidos
        if (dados.getDescricao() == null ||
                dados.getDescricao().isBlank() ||
                dados.getIdCiclo() <= 0) {

            return ResponseEntity.badRequest()
                    .body("Dados do sintoma inválidos");
        }

        // Atualizar usando o SintomasService
        boolean atualizado =
                sintomasService.atualizar(id, dados);

        // Verificar se o sintoma existe
        if (!atualizado) {
            return ResponseEntity.notFound().build();
        }

        // Retornar o sintoma atualizado
        return ResponseEntity.ok(
                sintomasService.buscar(id)
        );
    }

    // EXCLUIR um sintoma
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable int id) {

        // Solicitar exclusão ao SintomasService
        boolean excluido = sintomasService.excluir(id);

        // Verificar se o sintoma existe
        if (!excluido) {
            return ResponseEntity.notFound().build();
        }

        // Exclusão realizada com sucesso
        return ResponseEntity.noContent().build();
    }
}
