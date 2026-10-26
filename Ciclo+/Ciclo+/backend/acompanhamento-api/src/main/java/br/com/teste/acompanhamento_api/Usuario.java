package br.com.teste.acompanhamento_api;

import java.time.LocalDate;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private int duracaoCiclo;
    private int duracaoMenstruacao;
    private LocalDate dataNascimento;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDuracaoCiclo() {
        return duracaoCiclo;
    }

    public void setDuracaoCiclo(int duracaoCiclo) {
        this.duracaoCiclo = duracaoCiclo;
    }

    public int getDuracaoMenstruacao() {
        return duracaoMenstruacao;
    }

    public void setDuracaoMenstruacao(int duracaoMenstruacao) {
        this.duracaoMenstruacao = duracaoMenstruacao;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
