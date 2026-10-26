package br.com.teste.acompanhamento_api;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SintomasService {

    private final Map<Integer, Sintomas> sintomas =
            new ConcurrentHashMap<>();

    // CADASTRAR um sintoma
    public boolean cadastrar(Sintomas sintoma) {
        return sintomas.putIfAbsent(
                sintoma.getIdSintomas(), sintoma
        ) == null;
    }

    // LISTAR todos os sintomas
    public List<Sintomas> listar() {
        return new ArrayList<>(sintomas.values());
    }

    // BUSCAR um sintoma pelo ID
    public Sintomas buscar(int id) {
        return sintomas.get(id);
    }

    // ATUALIZAR um sintoma
    public boolean atualizar(int id, Sintomas dados) {

        Sintomas sintoma = buscar(id);

        if (sintoma == null) {
            return false;
        }

        sintoma.setDescricao(dados.getDescricao());
        sintoma.setIntensidade(dados.getIntensidade());
        sintoma.setIdCiclo(dados.getIdCiclo());

        return true;
    }

    // EXCLUIR um sintoma
    public boolean excluir(int id) {
        return sintomas.remove(id) != null;
    }
}
