const tipoBusca = document.getElementById("tipoBusca");
const campoTexto = document.getElementById("valorBuscaTexto");
const campoPeriodo = document.getElementById("valorBuscaPeriodo");

function atualizarCampoBusca() {
    const periodoSelecionado = tipoBusca.value === "3";

    campoTexto.style.display = periodoSelecionado ? "none" : "block";
    campoTexto.disabled = periodoSelecionado;

    campoPeriodo.style.display = periodoSelecionado ? "block" : "none";
    campoPeriodo.disabled = !periodoSelecionado;
}

tipoBusca.addEventListener("change", atualizarCampoBusca);

atualizarCampoBusca();