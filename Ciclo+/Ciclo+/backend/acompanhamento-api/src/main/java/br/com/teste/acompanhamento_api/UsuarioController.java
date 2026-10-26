package br.com.teste.acompanhamento_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // LISTAR todos os usuários
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    // BUSCAR um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable int id) {
        Usuario usuario = usuarioService.buscar(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    // CADASTRAR um usuário
    // O ID é opcional: quando ausente, a API gera um automaticamente.
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {

        if (usuario.getNome() == null
                || usuario.getNome().isBlank()
                || usuario.getDataNascimento() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Dados inválidos"));
        }

        if (usuarioService.emailJaCadastrado(usuario.getEmail())) {
            return ResponseEntity.status(409)
                    .body(Map.of("erro", "E-mail já cadastrado"));
        }

        boolean cadastrado = usuarioService.cadastrar(usuario);

        if (!cadastrado) {
            return ResponseEntity.status(409)
                    .body(Map.of("erro", "ID já cadastrado"));
        }

        return ResponseEntity.status(201).body(usuario);
    }

    // ATUALIZAR o nome de um usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody Usuario dados) {

        if (dados.getNome() == null || dados.getNome().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Nome inválido"));
        }

        boolean atualizado = usuarioService.atualizarNome(id, dados.getNome());

        if (!atualizado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarioService.buscar(id));
    }

    // EXCLUIR um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        boolean excluido = usuarioService.excluir(id);

        if (!excluido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
