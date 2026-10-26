
import java.time.LocalDate;

public class TesteValidadorCiclo {

    public static void main(String[] args) {
        boolean resultadoValido = ValidadorCiclo.validarDatas(
            LocalDate.of(2026, 10, 1),
            LocalDate.of(2026, 10, 5)
        );

        boolean resultadoInvalido = ValidadorCiclo.validarDatas(
            LocalDate.of(2026, 10, 5),
            LocalDate.of(2026, 10, 1)
        );

        System.out.println("Datas válidas: " + resultadoValido);
        System.out.println("Datas inválidas: " + resultadoInvalido);
    }
}
