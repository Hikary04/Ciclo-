/* =====================================================================
   Ciclo Certo – script principal
   Parte 1: lógica pura da calculadora (sem DOM) – testada em tests/calc.test.js
   Parte 2: integração com a página (menu, calculadora, cadastro via API)
   ===================================================================== */

const API_URL = "http://localhost:8080";
const DIA_MS = 86400000;

const addDays = (date, numberOfDays) => {
  const result = new Date(date);
  result.setDate(result.getDate() + numberOfDays);
  return result;
};

// Data local (meia-noite) a partir de "AAAA-MM-DD"
const parseISODate = (iso) => new Date(iso + "T00:00:00");

// "AAAA-MM-DD" da data local (sem o deslocamento do fuso que toISOString aplica)
const toISODate = (date) => {
  const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
  return local.toISOString().split("T")[0];
};

/**
 * Calcula as fases do ciclo.
 * @param {object} p
 * @param {string} p.ultimaMenstruacao  "AAAA-MM-DD" do primeiro dia da última menstruação
 * @param {number} p.duracaoCiclo       dias (21 a 45)
 * @param {number} p.duracaoMenstruacao dias (1 a 10)
 * @param {Date}   [p.hoje]             data de referência (padrão: hoje)
 * @returns {{erro: string} | {
 *   inicioCicloAtual: Date, diaAtual: number, proximaMenstruacao: Date,
 *   fimProximaMenstruacao: Date, ovulacao: Date, inicioFertil: Date, fimFertil: Date }}
 */
function calcularCiclo({ ultimaMenstruacao, duracaoCiclo, duracaoMenstruacao, hoje }) {
  const cycle = Number.parseInt(duracaoCiclo, 10);
  const period = Number.parseInt(duracaoMenstruacao, 10);

  if (!ultimaMenstruacao) {
    return { erro: "Informe a data da última menstruação." };
  }

  const start = parseISODate(ultimaMenstruacao);
  if (Number.isNaN(start.getTime())) {
    return { erro: "Data inválida." };
  }

  const today = hoje ? new Date(hoje) : new Date();
  today.setHours(0, 0, 0, 0);

  if (start > today) {
    return { erro: "A data não pode ser futura." };
  }

  if (Number.isNaN(cycle) || cycle < 21 || cycle > 45) {
    return { erro: "A duração do ciclo deve estar entre 21 e 45 dias." };
  }

  if (Number.isNaN(period) || period < 1 || period > 10) {
    return { erro: "A menstruação deve durar entre 1 e 10 dias." };
  }

  // Quantos ciclos completos já se passaram desde a data informada.
  // Isso garante que a "próxima menstruação" seja sempre uma data futura
  // (ou hoje), mesmo que a última menstruação informada seja antiga.
  const diffDays = Math.floor((today - start) / DIA_MS);
  const ciclosCompletos = Math.floor(diffDays / cycle);
  const inicioCicloAtual = addDays(start, ciclosCompletos * cycle);
  const diaAtual = diffDays - ciclosCompletos * cycle + 1;

  const proximaMenstruacao = addDays(inicioCicloAtual, cycle);
  const fimProximaMenstruacao = addDays(proximaMenstruacao, period - 1);
  const ovulacao = addDays(proximaMenstruacao, -14);
  const inicioFertil = addDays(ovulacao, -5);
  const fimFertil = addDays(ovulacao, 1);

  return {
    inicioCicloAtual,
    diaAtual,
    proximaMenstruacao,
    fimProximaMenstruacao,
    ovulacao,
    inicioFertil,
    fimFertil
  };
}

// Validação do cadastro (sem DOM) – também testada em tests/calc.test.js
function validarCadastro({ nome, email, nascimento, senha, confirmacao, termos, hoje }) {
  if (!nome || nome.trim().length < 2) {
    return "Informe seu nome.";
  }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email || "")) {
    return "Informe um e-mail válido.";
  }
  if (!nascimento) {
    return "Informe sua data de nascimento.";
  }
  const nasc = parseISODate(nascimento);
  const today = hoje ? new Date(hoje) : new Date();
  today.setHours(0, 0, 0, 0);
  if (Number.isNaN(nasc.getTime()) || nasc > today) {
    return "Data de nascimento inválida.";
  }
  if (!senha || senha.length < 8) {
    return "A senha deve ter pelo menos 8 caracteres.";
  }
  if (senha !== confirmacao) {
    return "As senhas não coincidem.";
  }
  if (!termos) {
    return "Aceite os termos para continuar.";
  }
  return null;
}

// Exporta para os testes em Node (ignorado pelo navegador)
if (typeof module !== "undefined" && module.exports) {
  module.exports = { calcularCiclo, validarCadastro, addDays, toISODate, parseISODate };
}

/* =====================================================================
   Parte 2 – integração com a página
   ===================================================================== */
if (typeof document !== "undefined") {
  // ---------- Menu para dispositivos móveis ----------
  const menuBtn = document.getElementById("menuBtn");
  const menu = document.getElementById("menu");

  menuBtn.addEventListener("click", () => {
    const aberto = menu.classList.toggle("open");
    menuBtn.setAttribute("aria-expanded", String(aberto));
  });

  menu.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => {
      menu.classList.remove("open");
      menuBtn.setAttribute("aria-expanded", "false");
    });
  });

  // ---------- Dia demonstrativo no desenho do celular ----------
  const dayDemo = document.getElementById("dayDemo");
  let demoDay = 14;

  setInterval(() => {
    demoDay = demoDay >= 28 ? 1 : demoDay + 1;
    dayDemo.textContent = demoDay;
  }, 2000);

  // ---------- Calculadora do ciclo ----------
  const form = document.getElementById("calcForm");
  const errorEl = document.getElementById("error");
  const results = document.getElementById("results");
  const lastPeriodInput = document.getElementById("lastPeriod");

  const hojeISO = toISODate(new Date());
  lastPeriodInput.max = hojeISO; // evita selecionar data futura

  const fmt = (date) =>
    date.toLocaleDateString("pt-BR", { day: "2-digit", month: "short", year: "numeric" });

  const fmtCurto = (date) =>
    date.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit" });

  form.addEventListener("submit", (event) => {
    event.preventDefault();
    errorEl.textContent = "";

    const r = calcularCiclo({
      ultimaMenstruacao: lastPeriodInput.value,
      duracaoCiclo: document.getElementById("cycleLength").value,
      duracaoMenstruacao: document.getElementById("periodLength").value
    });

    if (r.erro) {
      errorEl.textContent = r.erro;
      results.classList.remove("show");
      return;
    }

    document.getElementById("nextPeriod").textContent = fmt(r.proximaMenstruacao);
    document.getElementById("nextPeriodSub").textContent =
      `Próxima menstruação (até ${fmtCurto(r.fimProximaMenstruacao)})`;
    document.getElementById("ovulation").textContent = fmt(r.ovulacao);
    document.getElementById("fertile").textContent =
      `${fmt(r.inicioFertil)} a ${fmt(r.fimFertil)}`;
    document.getElementById("currentDay").textContent = `Dia ${r.diaAtual}`;

    results.classList.add("show");
  });

  // ---------- Cadastro conectado à API Java (backend/acompanhamento-api) ----------
  const signupForm = document.getElementById("signupForm");
  const signupError = document.getElementById("signupError");
  const signupSuccess = document.getElementById("signupSuccess");
  const signupBirth = document.getElementById("signupBirth");
  const signupButton = signupForm.querySelector("button[type=submit]");

  signupBirth.max = hojeISO;

  signupForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    signupError.textContent = "";
    signupSuccess.hidden = true;

    const nome = document.getElementById("signupName").value.trim();
    const email = document.getElementById("signupEmail").value.trim();
    const nascimento = signupBirth.value;

    const erro = validarCadastro({
      nome,
      email,
      nascimento,
      senha: document.getElementById("signupPass").value,
      confirmacao: document.getElementById("signupPass2").value,
      termos: document.getElementById("signupTerms").checked
    });

    if (erro) {
      signupError.textContent = erro;
      return;
    }

    signupButton.disabled = true;
    try {
      // A senha não é enviada: a API ainda não implementa autenticação.
      const response = await fetch(`${API_URL}/usuarios`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nome, email, dataNascimento: nascimento })
      });

      if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        throw new Error(body.erro || body.message || `A API recusou o cadastro (HTTP ${response.status}).`);
      }

      const usuario = await response.json();
      signupSuccess.textContent =
        `Conta criada com sucesso! Bem-vinda ao Ciclo Certo 💖 (usuário nº ${usuario.idUsuario})`;
      signupSuccess.hidden = false;
      signupForm.reset();
    } catch (err) {
      signupError.textContent =
        err instanceof TypeError
          ? "Não foi possível falar com a API. Inicie o backend (localhost:8080) e tente novamente."
          : err.message;
    } finally {
      signupButton.disabled = false;
    }
  });
}
