// Testes da lógica pura do front-end.
// Executar na raiz do projeto:  npm test   (ou: node --test frontend/tests/calc.test.js)
const { test, describe } = require("node:test");
const assert = require("node:assert/strict");
const { calcularCiclo, validarCadastro, toISODate } = require("../script.js");

const HOJE = new Date(2026, 9, 26); // 26/10/2026 (mês é 0-based)

describe("calcularCiclo", () => {
  test("ciclo de 28 dias iniciado há 14 dias: dia 15, próxima em 14 dias", () => {
    const r = calcularCiclo({
      ultimaMenstruacao: "2026-10-12",
      duracaoCiclo: 28,
      duracaoMenstruacao: 5,
      hoje: HOJE
    });
    assert.equal(r.erro, undefined);
    assert.equal(r.diaAtual, 15);
    assert.equal(toISODate(r.proximaMenstruacao), "2026-11-09");
    assert.equal(toISODate(r.fimProximaMenstruacao), "2026-11-13");
    assert.equal(toISODate(r.ovulacao), "2026-10-26");
    assert.equal(toISODate(r.inicioFertil), "2026-10-21");
    assert.equal(toISODate(r.fimFertil), "2026-10-27");
  });

  test("última menstruação antiga: próxima menstruação nunca fica no passado", () => {
    const r = calcularCiclo({
      ultimaMenstruacao: "2026-01-03", // 296 dias antes de HOJE
      duracaoCiclo: 28,
      duracaoMenstruacao: 5,
      hoje: HOJE
    });
    assert.ok(r.proximaMenstruacao >= HOJE, "próxima menstruação deve ser hoje ou futura");
    assert.equal(toISODate(r.inicioCicloAtual), "2026-10-10"); // 10 ciclos completos depois
    assert.equal(r.diaAtual, 17);
    assert.equal(toISODate(r.proximaMenstruacao), "2026-11-07");
  });

  test("hoje é o primeiro dia do ciclo", () => {
    const r = calcularCiclo({
      ultimaMenstruacao: "2026-10-26",
      duracaoCiclo: 30,
      duracaoMenstruacao: 4,
      hoje: HOJE
    });
    assert.equal(r.diaAtual, 1);
    assert.equal(toISODate(r.proximaMenstruacao), "2026-11-25");
    assert.equal(toISODate(r.ovulacao), "2026-11-11");
  });

  test("exatamente um ciclo depois: volta ao dia 1 e próxima é daqui a um ciclo", () => {
    const r = calcularCiclo({
      ultimaMenstruacao: "2026-09-28", // 28 dias antes
      duracaoCiclo: 28,
      duracaoMenstruacao: 5,
      hoje: HOJE
    });
    assert.equal(r.diaAtual, 1);
    assert.equal(toISODate(r.inicioCicloAtual), "2026-10-26");
    assert.equal(toISODate(r.proximaMenstruacao), "2026-11-23");
  });

  test("aceita valores vindos como string (inputs do formulário)", () => {
    const r = calcularCiclo({
      ultimaMenstruacao: "2026-10-20",
      duracaoCiclo: "28",
      duracaoMenstruacao: "5",
      hoje: HOJE
    });
    assert.equal(r.erro, undefined);
    assert.equal(r.diaAtual, 7);
  });

  test("validações", () => {
    const base = { ultimaMenstruacao: "2026-10-20", duracaoCiclo: 28, duracaoMenstruacao: 5, hoje: HOJE };
    assert.equal(calcularCiclo({ ...base, ultimaMenstruacao: "" }).erro, "Informe a data da última menstruação.");
    assert.equal(calcularCiclo({ ...base, ultimaMenstruacao: "abc" }).erro, "Data inválida.");
    assert.equal(calcularCiclo({ ...base, ultimaMenstruacao: "2026-10-27" }).erro, "A data não pode ser futura.");
    assert.equal(calcularCiclo({ ...base, duracaoCiclo: 20 }).erro, "A duração do ciclo deve estar entre 21 e 45 dias.");
    assert.equal(calcularCiclo({ ...base, duracaoCiclo: 46 }).erro, "A duração do ciclo deve estar entre 21 e 45 dias.");
    assert.equal(calcularCiclo({ ...base, duracaoCiclo: "" }).erro, "A duração do ciclo deve estar entre 21 e 45 dias.");
    assert.equal(calcularCiclo({ ...base, duracaoMenstruacao: 0 }).erro, "A menstruação deve durar entre 1 e 10 dias.");
    assert.equal(calcularCiclo({ ...base, duracaoMenstruacao: 11 }).erro, "A menstruação deve durar entre 1 e 10 dias.");
  });
});

describe("validarCadastro", () => {
  const ok = {
    nome: "Ana",
    email: "ana@example.com",
    nascimento: "2000-01-01",
    senha: "12345678",
    confirmacao: "12345678",
    termos: true,
    hoje: HOJE
  };

  test("cadastro válido não retorna erro", () => {
    assert.equal(validarCadastro(ok), null);
  });

  test("cada campo inválido retorna a mensagem certa", () => {
    assert.equal(validarCadastro({ ...ok, nome: "A" }), "Informe seu nome.");
    assert.equal(validarCadastro({ ...ok, email: "ana@" }), "Informe um e-mail válido.");
    assert.equal(validarCadastro({ ...ok, nascimento: "" }), "Informe sua data de nascimento.");
    assert.equal(validarCadastro({ ...ok, nascimento: "2026-10-27" }), "Data de nascimento inválida.");
    assert.equal(validarCadastro({ ...ok, senha: "1234567", confirmacao: "1234567" }), "A senha deve ter pelo menos 8 caracteres.");
    assert.equal(validarCadastro({ ...ok, confirmacao: "diferente1" }), "As senhas não coincidem.");
    assert.equal(validarCadastro({ ...ok, termos: false }), "Aceite os termos para continuar.");
  });
});
