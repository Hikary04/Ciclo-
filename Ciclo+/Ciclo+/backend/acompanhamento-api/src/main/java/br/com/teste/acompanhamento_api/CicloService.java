package br.com.teste.acompanhamento_api;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CicloService {

    private final List<Ciclo> ciclos = new ArrayList<>();

    // CADASTRAR um ciclo
    public void cadastrar(Ciclo ciclo) {
        ciclos.add(ciclo);
    }

    // LISTAR todos os ciclos
    public List<Ciclo> listar() {
        return List.copyOf(ciclos);
    }

    // BUSCAR um ciclo pelo ID
    public Ciclo buscar(int id) {
        for (Ciclo ciclo : ciclos) {
            if (ciclo.getIdCiclo() == id) {
                return ciclo;
            }
        }
        return null;
    }

    // ATUALIZAR um ciclo
    public boolean atualizar(int id,
                             LocalDate inicio,
                             LocalDate fim) {

        Ciclo ciclo = buscar(id);

        if (ciclo == null) {
            return false;
        }

        ciclo.setDataInicio(inicio);
        ciclo.setDataFinal(fim);
        return true;
    }

    // EXCLUIR um ciclo
    public boolean excluir(int id) {
        return ciclos.removeIf(
                ciclo -> ciclo.getIdCiclo() == id
        );
    }
}

