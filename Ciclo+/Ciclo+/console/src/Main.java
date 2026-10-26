import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Usuario> usuarios = new ArrayList<>();
    static ArrayList<Ciclo> ciclos = new ArrayList<>();
    static ArrayList<Sintomas> sintomas = new ArrayList<>();
    static ArrayList<CicloMenstrual> ciclosMenstruais = new ArrayList<>();

    public static void main(String[] args) {

        int op;

        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1 - Usuário");
            System.out.println("2 - Ciclo Mensal");
            System.out.println("3 - Sintomas");
            System.out.println("4 - Ciclo Menstrual");
            System.out.println("0 - Sair");

            op = lerInt();

            switch (op) {
                case 1: menuUsuario(); break;
                case 2: menuCiclo(); break;
                case 3: menuSintomas(); break;
                case 4: menuCicloMenstrual(); break;
                default:
                    if (op != 0) System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    static void menuUsuario() {

        int op;

        do {
            System.out.println("\n--- USUÁRIO ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            op = lerInt();

            switch (op) {

                case 1:

                    Usuario u = new Usuario();

                    System.out.print("ID: ");
                    u.setIdUsuario(lerInt());

                    System.out.print("Nome: ");
                    u.setNome(sc.nextLine());

                    System.out.print("Nascimento: ");
                    u.setDataNascimento(lerData());

                    usuarios.add(u);
                    break;

                case 2:

                    usuarios.forEach(user ->
                            System.out.println(
                                    "ID: " + user.getIdUsuario() +
                                            " | Nome: " + user.getNome() +
                                            " | Nascimento: " + user.getDataNascimento()
                            ));
                    break;

                case 3:

                    System.out.print("ID: ");
                    int id = lerInt();

                    for (Usuario user : usuarios) {

                        if (user.getIdUsuario() == id) {

                            System.out.print("Novo nome: ");
                            user.setNome(sc.nextLine());
                        }
                    }

                    break;

                case 4:

                    System.out.print("ID: ");
                    int del = lerInt();

                    System.out.println(usuarios.removeIf(u1 -> u1.getIdUsuario() == del) ? "Excluído!" : "ID não encontrado.");
                    break;

                default:
                    if (op != 0) System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    static void menuCiclo() {

        int op;

        do {
            System.out.println("\n--- CICLO MENSAL ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            op = lerInt();

            switch (op) {

                case 1:

                    Ciclo c = new Ciclo();

                    System.out.print("ID: ");
                    c.setIdCiclo(lerInt());

                    System.out.print("ID usuário: ");
                    c.setIdUsuario(lerInt());

                    System.out.print("Início: ");
                    c.setDataInicio(lerData());

                    System.out.print("Fim: ");
                    c.setDataFinal(lerData());

                    ciclos.add(c);
                    break;

                case 2:

                    ciclos.forEach(c1 ->
                            System.out.println(
                                    "ID: " + c1.getIdCiclo() +
                                            " | Usuário: " + c1.getIdUsuario() +
                                            " | Início: " + c1.getDataInicio() +
                                            " | Fim: " + c1.getDataFinal()
                            ));
                    break;

                case 3:

                    System.out.print("ID: ");
                    int id = lerInt();

                    for (Ciclo c1 : ciclos) {

                        if (c1.getIdCiclo() == id) {

                            System.out.print("Nova data início: ");
                            c1.setDataInicio(lerData());

                            System.out.print("Nova data final: ");
                            c1.setDataFinal(lerData());
                        }
                    }

                    break;

                case 4:

                    System.out.print("ID: ");
                    int del = lerInt();

                    System.out.println(ciclos.removeIf(c1 -> c1.getIdCiclo() == del) ? "Excluído!" : "ID não encontrado.");
                    break;

                default:
                    if (op != 0) System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    static void menuSintomas() {

        int op;

        do {
            System.out.println("\n--- SINTOMAS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            op = lerInt();

            switch (op) {

                case 1:

                    Sintomas s = new Sintomas();

                    System.out.print("ID: ");
                    s.setIdSintomas(lerInt());

                    System.out.print("ID ciclo: ");
                    s.setIdCiclo(lerInt());

                    System.out.print("Descrição: ");
                    s.setDescricao(sc.nextLine());

                    System.out.print("Intensidade: ");
                    s.setIntensidade(lerInt());

                    sintomas.add(s);
                    break;

                case 2:

                    sintomas.forEach(s1 ->
                            System.out.println(
                                    "ID: " + s1.getIdSintomas() +
                                            " | Descrição: " + s1.getDescricao() +
                                            " | Intensidade: " + s1.getIntensidade()
                            ));
                    break;

                case 3:

                    System.out.print("ID: ");
                    int id = lerInt();

                    for (Sintomas s1 : sintomas) {

                        if (s1.getIdSintomas() == id) {

                            System.out.print("Nova descrição: ");
                            s1.setDescricao(sc.nextLine());
                        }
                    }

                    break;

                case 4:

                    System.out.print("ID: ");
                    int del = lerInt();

                    System.out.println(sintomas.removeIf(s1 -> s1.getIdSintomas() == del) ? "Excluído!" : "ID não encontrado.");
                    break;

                default:
                    if (op != 0) System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    static void menuCicloMenstrual() {

        int op;

        do {
            System.out.println("\n--- CICLO MENSTRUAL ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            op = lerInt();

            switch (op) {

                case 1:

                    CicloMenstrual cm = new CicloMenstrual();

                    System.out.print("ID: ");
                    cm.setIdCicloMenstrual(lerInt());

                    System.out.print("ID ciclo: ");
                    cm.setIdCiclo(lerInt());

                    System.out.print("Início: ");
                    cm.setDataInicio(lerData());

                    System.out.print("Fim: ");
                    cm.setDataFinal(lerData());

                    System.out.print("Fluxo: ");
                    cm.setIntensidadeFluxo(lerInt());

                    ciclosMenstruais.add(cm);
                    break;

                case 2:

                    ciclosMenstruais.forEach(cm1 ->
                            System.out.println(
                                    "ID: " + cm1.getIdCicloMenstrual() +
                                            " | Início: " + cm1.getDataInicio() +
                                            " | Fim: " + cm1.getDataFinal() +
                                            " | Fluxo: " + cm1.getIntensidadeFluxo()
                            ));
                    break;

                case 3:

                    System.out.print("ID: ");
                    int id = lerInt();

                    for (CicloMenstrual cm1 : ciclosMenstruais) {

                        if (cm1.getIdCicloMenstrual() == id) {

                            System.out.print("Novo fluxo: ");
                            cm1.setIntensidadeFluxo(lerInt());
                        }
                    }

                    break;

                case 4:

                    System.out.print("ID: ");
                    int del = lerInt();

                    System.out.println(ciclosMenstruais.removeIf(cm1 -> cm1.getIdCicloMenstrual() == del) ? "Excluído!" : "ID não encontrado.");
                    break;

                default:
                    if (op != 0) System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    // Lê um inteiro, repetindo o pedido até receber um valor válido.
    static int lerInt() {
        while (true) {
            String linha = sc.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido. Digite um número inteiro: ");
            }
        }
    }

    // Lê uma data no formato AAAA-MM-DD, repetindo o pedido até receber uma válida.
    static LocalDate lerData() {
        while (true) {
            String linha = sc.nextLine().trim();
            try {
                return LocalDate.parse(linha);
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. Use o formato AAAA-MM-DD: ");
            }
        }
    }
}
