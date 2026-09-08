const tipoBusca = document.getElementById("tipoBusca");
const campoBusca = document.getElementById("valorBuscaTexto");

function atualizarCampoBusca() {

    switch (tipoBusca.value) {
        case "6":
            campoBusca.type = "date";
            break;

        case "7":
            campoBusca.type = "time";
            break;

        default:
            campoBusca.type = "text";
            break;
    }
}

tipoBusca.addEventListener("change", atualizarCampoBusca);

atualizarCampoBusca();