
import java.time.LocalDate;

public class ValidadorCiclo {

    public static boolean validarDatas(LocalDate dataInicio, LocalDate dataFinal) {
        if (dataInicio == null || dataFinal == null) {
            return false;
        }

        return !dataFinal.isBefore(dataInicio);
    }
}
