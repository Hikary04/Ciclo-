package br.com.teste.acompanhamento_api;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UsuarioService {

    private final Map<Integer, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(0);

    // LISTAR todos os usuários
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    // BUSCAR um usuário pelo ID
    public Usuario buscar(int id) {
        return usuarios.get(id);
    }

    // Verifica se já existe usuário com o mesmo e-mail (RN02)
    public boolean emailJaCadastrado(String email) {
        if (email == null) {
            return false;
        }
        return usuarios.values().stream()
                .anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
    }

    // CADASTRAR um usuário.
    // Se o ID não for informado (<= 0), um ID sequencial é gerado.
    // Retorna false se o ID informado já existir.
    public synchronized boolean cadastrar(Usuario usuario) {
        if (usuario.getIdUsuario() <= 0) {
            int id;
            do {
                id = proximoId.incrementAndGet();
            } while (usuarios.containsKey(id));
            usuario.setIdUsuario(id);
        } else {
            proximoId.accumulateAndGet(usuario.getIdUsuario(), Math::max);
        }
        return usuarios.putIfAbsent(usuario.getIdUsuario(), usuario) == null;
    }

    // ATUALIZAR o nome de um usuário
    public boolean atualizarNome(int id, String nome) {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            return false;
        }
        usuario.setNome(nome);
        return true;
    }

    // EXCLUIR um usuário
    public boolean excluir(int id) {
        return usuarios.remove(id) != null;
    }
}
