package br.com.teste.acompanhamento_api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/ciclos")
public class CicloController {

    private final CicloService cicloService;

    public CicloController(CicloService cicloService) {
        this.cicloService = cicloService;
    }

    // CONSULTAR todos os ciclos
    @GetMapping
    public List<Ciclo> listar() {
        return cicloService.listar();
    }

    // CADASTRAR um ciclo menstrual
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Ciclo ciclo) {

        // Verificar se os dados são válidos
        if (ciclo.getIdCiclo() <= 0 ||
                ciclo.getIdUsuario() <= 0 ||
                ciclo.getDataInicio() == null ||
                ciclo.getDataFinal() == null ||
                ciclo.getDataFinal().isBefore(ciclo.getDataInicio())) {

            return ResponseEntity.badRequest()
                    .body("Dados do ciclo inválidos");
        }

        // Verificar se o ID já existe
        if (cicloService.buscar(ciclo.getIdCiclo()) != null) {
            return ResponseEntity.status(409)
                    .body("Ciclo já cadastrado");
        }

        // Salvar o ciclo
        cicloService.cadastrar(ciclo);

        return ResponseEntity.status(201).body(ciclo);
    }

    // ATUALIZAR um ciclo menstrual
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody Ciclo dados) {

        // Verificar se as datas são válidas
        if (dados.getDataInicio() == null ||
                dados.getDataFinal() == null ||
                dados.getDataFinal().isBefore(dados.getDataInicio())) {

            return ResponseEntity.badRequest()
                    .body("Datas do ciclo inválidas");
        }

        // Atualizar usando o CicloService
        boolean atualizado = cicloService.atualizar(
                id,
                dados.getDataInicio(),
                dados.getDataFinal()
        );

        // Verificar se o ciclo existe
        if (!atualizado) {
            return ResponseEntity.notFound().build();
        }

        // Retornar o ciclo atualizado
        return ResponseEntity.ok(cicloService.buscar(id));
    }

    // EXCLUIR um ciclo menstrual
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
           @PathVariable int id) {

        // Solicitar exclusão ao CicloService
        boolean excluido = cicloService.excluir(id);

        // Se não encontrou o ciclo
        if (!excluido) {
         return ResponseEntity.notFound().build();
    }

    // Exclusão realizada com sucesso
    return ResponseEntity.noContent().build();
    }
}
