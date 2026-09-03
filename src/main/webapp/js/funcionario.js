const modalExclusao = document.getElementById("confirmarExclusao");

modalExclusao.addEventListener("show.bs.modal", function (event) {

    const btnQueAbriuModal = event.relatedTarget;

    const urlExclusao = btnQueAbriuModal.getAttribute("data-url");

    console.log(urlExclusao);

    const btnSim = document.getElementById("excluir");

    btnSim.setAttribute("href", urlExclusao);
});