
$(document).ready(function() {
    // Configuração base do Select2
    const select2BaseConfig = {
        language: 'pt',
        allowClear: true,
        closeOnSelect: false,
        width: '100%',
        dropdownAutoWidth: true,
        escapeMarkup: function(markup) {
            return markup;
        }
    };


    $('#cnaes').select2({
        ...select2BaseConfig,
        placeholder: "Digite para buscar CNAEs...",
        ajax: {
            url: `${appConfig.backendUrlApi}/cnaes`,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    busca: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(item => ({
                        id: item.codigo,
                        text: `${item.codigo} - ${item.descricao}`
                    }))
                };
            }
        },
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            return $('<span class="cnae-tag">' + selection.text + '</span>');
        }
    });
    $('#cnaes').on('select2:select', function (e) {
        $(this).select2('close');
    });


    $('#naturezas').select2({
        ...select2BaseConfig,
        placeholder: "Digite para buscar Naturezas...",
        ajax: {
            url: `${appConfig.backendUrlApi}/naturezas`,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    busca: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(item => ({
                        id: item.codigo,
                        text: `${item.codigo} - ${item.descricao}`
                    }))
                };
            }
        },
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            return $('<span class="natureza-tag">' + selection.text + '</span>');
        }
    });
    $('#naturezas').on('select2:select', function (e) {
        $(this).select2('close');
    });


    $('#estados').select2({
        ...select2BaseConfig,
        placeholder: "Digite para buscar Estados...",
        ajax: {
            url: `${appConfig.backendUrlApi}/estados`,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    busca: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(item => ({
                        id: item.uf,
                        text: `${item.uf}`
                    }))
                };
            }
        },
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            return $('<span class="estado-tag">' + selection.text + '</span>');
        }
    });
    $('#estados').on('select2:select', function (e) {
        $(this).select2('close');
    });

    $('#municipios').select2({
        ...select2BaseConfig,
        placeholder: "Digite para buscar Municípios...",
        ajax: {
            url: `${appConfig.backendUrlApi}/municipios`,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    busca: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(item => ({
                        id: item.siafi_id,
                        text: `${item.nome} - ${item.uf}`
                    }))
                };
            }
        },
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            return $('<span class="municipio-tag">' + selection.text + '</span>');
        }
    });
    $('#municipios').on('select2:select', function (e) {
        $(this).select2('close');
    });


    const situacoes = [
        { value: "02", label: "ATIVA" },
        { value: "01", label: "NULA" },
        { value: "03", label: "SUSPENSA" },
        { value: "04", label: "INAPTA" },
        { value: "08", label: "BAIXADA" }
    ];

    $('#situacao').select2({
        ...select2BaseConfig,
        placeholder: "Selecione Situações...",
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            let className = 'situacao-' + selection.text.toLowerCase();
            return $('<span class="' + className + '">' + selection.text + '</span>');
        }
    });

    situacoes.forEach(option => {
        const newOption = new Option(option.label, option.value, false, false);
        $('#situacao').append(newOption);
    });


    $('#situacao').val('02').trigger('change');


    const mei = [
        { value: "S", label: "SIM" },
        { value: "N", label: "NÃO" }
    ];

    $('#mei').select2({
        ...select2BaseConfig,
        placeholder: "Selecione Se é MEI...",
        templateSelection: function(selection) {
            if (!selection.id) return selection.text;
            let className = selection.id === 'S' ? 'mei-sim' : 'mei-nao';
            return $('<span class="' + className + '">' + selection.text + '</span>');
        }
    });

    mei.forEach(option => {
        const newOption = new Option(option.label, option.value, false, false);
        $('#mei').append(newOption);
    });

    $('#mei').val('S').trigger('change');


    function clearAllFilters() {
        $('select').val(null).trigger('change');
    }


    $('.btn-container-clear').prepend('<button type="button" class="btn-clear" style="background: #6c757d; margin-right: 10px;" onclick="clearAllFilters()">Limpar Filtros</button>');


    $('.select2-container').on('select2:open', function() {
        $(this).addClass('select2-container--focus');
    });

    $('.select2-container').on('select2:close', function() {
        $(this).removeClass('select2-container--focus');
    });


    window.clearAllFilters = clearAllFilters;
});