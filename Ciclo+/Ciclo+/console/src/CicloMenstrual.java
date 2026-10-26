import java.time.LocalDate;

public class CicloMenstrual {

    private int idCicloMenstrual;
    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private int intensidadeFluxo;
    private int idCiclo;

    public int getIdCicloMenstrual() {
        return idCicloMenstrual;
    }

    public void setIdCicloMenstrual(int idCicloMenstrual) {
        this.idCicloMenstrual = idCicloMenstrual;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public int getIntensidadeFluxo() {
        return intensidadeFluxo;
    }

    public void setIntensidadeFluxo(int intensidadeFluxo) {
        this.intensidadeFluxo = intensidadeFluxo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }
}