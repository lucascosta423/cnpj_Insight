$('#formulario').on('submit', async function (e) {
    e.preventDefault();

    const cnaes = $('#cnaes').val();
    const naturezas = $('#naturezas').val();
    const estados = $('#estados').val();
    const municipios = $('#municipios').val();
    const situacao = $('#situacao').val();
    const mei = $('#mei').val();
    const limite = $('#limite').val();

    const baseUrl = `${appConfig.backendUrlExport}/start`;
    const params = new URLSearchParams({
        uf: estados,
        cnae: cnaes,
        natureza: naturezas,
        municipio: municipios,
        situacao: situacao,
        mei: mei,
        limite: limite
    });

    try {
        const response = await fetch(baseUrl + '?' + params);

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }

        const data = await response.json();
        console.log('Resposta: ', data);

        mostrarNotificacao('Exportação iniciada com sucesso!', true);
        return data;
    } catch (error) {
        console.error('Erro ao exportar:', error);
        mostrarNotificacao('Erro ao iniciar exportação: ' + error.message, false);
    }
});



function mostrarNotificacao(mensagem, sucesso = true) {
    const modalBody = document.getElementById('notificationModalBody');
    const modalHeader = document.querySelector('#notificationModal .modal-header');
    const modalTitle = document.getElementById('notificationModalLabel');

    modalBody.textContent = mensagem;
    modalHeader.classList.toggle('bg-success', sucesso);
    modalHeader.classList.toggle('bg-danger', !sucesso);
    modalTitle.textContent = sucesso ? 'Sucesso' : 'Erro';

    const modal = new bootstrap.Modal(document.getElementById('notificationModal'));
    modal.show();
}



