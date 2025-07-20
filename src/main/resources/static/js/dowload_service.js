function getAllTask() {
    $.ajax({
        url: `${appConfig.backendUrlExport}/tasks`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            const $tbody = $('#task_list');
            $tbody.empty(); // limpa o conteúdo anterior

            $.each(data, function(key, value) {

                const $tr = $(`
                    <tr>
                        <td class="bi bi-hash">${key}</td>
                        <td colspan="1">${value === 'EXECUTANDO'? `
                            <span class="badge bg-success d-inline-flex align-items-center gap-1">
                              <span>EXECUTANDO</span>
                              <span class="spinner-border spinner-border-sm text-light" role="status" aria-hidden="true"></span>
                            </span>
                              `
                            : `<span>${value}</span>`
                            }
                        </td>
                        <td colspan="1">                       
                            <button class="action-btn btn-delete ms-2 btn-delete-task"  data-key="${key}">
                                <i class="bi bi-trash"></i> Excluir
                            </button>
                        </td>
                    </tr>

                `);

                $tbody.append($tr);

            });
        },
        error: function(xhr, status, error) {
            console.error('Erro ao carregar tarefas:', error);
        }
    });
}

function getAllFiles() {
    $.ajax({
        url: `${appConfig.backendUrlExport}/files`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            const $tbody = $('#files_list');
            $tbody.empty(); // limpa o conteúdo anterior

            $.each(data, function(index, item) {
                const $tr = $(`
                    <tr>
                        <td colspan="1" ><span class="id-badge">F${index+1}</span></td>
                        <td class="file-name" colspan="1">${item.fileName}</td>
                        <td class="file-date" colspan="1">${formatDate(item.createdDate)}</td>
                        <td class="file-size" colspan="1">${item.formattedSize}</td>
                        <td>
                            <a class="action-btn btn-download" href="${appConfig.backendUrlExport}/download/file/${item.fileName}" class="btn btn-sm btn-success" download>
                                <i class="bi bi-download"></i> Download
                            </a>
                            
                            <button class="action-btn btn-delete btn-delete-file ms-2" data-filename="${item.fileName}">
                                <i class="bi bi-trash"></i> Excluir
                            </button>
                        </td>
                    </tr>
                `);

                $tbody.append($tr);
            });
        },
        error: function(xhr, status, error) {
            console.error('Erro ao carregar tarefas:', error);
        }
    });
}

function formatDate(dateString) {
    const date = new Date(dateString); // ignora nanossegundos
    const pad = n => n.toString().padStart(2, '0');

    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1);     // mês começa em 0
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}



$(document).ready(function() {
    // Executa imediatamente ao carregar a página
    getAllTask();
    getAllFiles()

    // Define execução a cada 1 minuto (60000 ms)
    setInterval(getAllTask, 6000);
});


// Evento para deletar file
$(document).on('click', '.btn-delete-file', function () {
    const filename = $(this).data('filename');

    // if (!confirm(`Tem certeza que deseja excluir o arquivo "${filename}"?`)) return;

    $.ajax({
        url: `${appConfig.backendUrlExport}/file/delete/${filename}`,
        method: 'DELETE',
        success: function () {
            mostrarNotificacao('Arquivo excluído com sucesso!');
            getAllFiles()
        },
        error: function (xhr) {
            mostrarNotificacao('Erro ao excluir o arquivo.', false);
            console.error(xhr.responseText);
        }
    });
});


// Evento para deletar task
$(document).on('click', '.btn-delete-task', function () {
    const key = $(this).data('key');

    // if (!confirm(`Tem certeza que deseja excluir o arquivo "${filename}"?`)) return;

    $.ajax({
        url: `${appConfig.backendUrlExport}/tasks/delete/${key}`,
        method: 'DELETE',
        success: function () {
            mostrarNotificacao('Arquivo excluído com sucesso!');
            getAllTask()
        },
        error: function (xhr) {
            mostrarNotificacao('Erro ao excluir o arquivo.', false);
            console.error(xhr.responseText);
        }
    });
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
