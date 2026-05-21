<?php
/**
 * Configuração centralizada da API REST (Spring Boot).
 * Altere apenas o BASE_URL caso a porta ou host mude.
 */
define('API_BASE_URL', 'http://localhost:8080');

/**
 * Diretório onde uploads de imagem são gravados (relativo ao frontend/).
 * Servido em /uploads/<arquivo>.
 */
define('UPLOAD_DIR', __DIR__ . '/../uploads');
define('UPLOAD_URL_PREFIX', 'uploads/');

/**
 * Padrões regex reutilizáveis (também validados no client via attr `pattern`).
 */
$REGEX = [
    'cnpj'      => '^\d{2}\.?\d{3}\.?\d{3}\/?\d{4}-?\d{2}$',
    'cpf'       => '^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$',
    'cpf_rg'    => '^([0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}|[0-9]{1,2}\.?[0-9]{3}\.?[0-9]{3}-?[0-9A-Za-z])$',
    'nome'      => "^[A-Za-zÀ-ÖØ-öø-ÿ' .-]{2,80}$",
    'pais'      => "^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,60}$",
    'palavras'  => "^[A-Za-zÀ-ÖØ-öø-ÿ0-9 .,'-]{2,80}$",
    'money'     => '^\d{1,9}([.,]\d{1,2})?$',
];

/**
 * Mapeamento de todas as entidades do sistema.
 *
 * Cada campo pode ter:
 *  - name, label, type, required, min, max, step (HTML padrão)
 *  - pattern    : regex (string) validada no client e no server
 *  - hint       : texto auxiliar mostrado abaixo do input
 *  - money      : true → input com prefixo R$, formatação BR e validação 2 casas
 *  - fk         : ['entity' => slug, 'display' => 'campoExibido']
 *                  → renderiza <select> populado via API e mostra `display` na listagem
 *  - image      : true → input file de imagem com preview (campo armazena URL/caminho)
 */
$ENTITIES = [
    'hoteis' => [
        'endpoint' => '/hoteis',
        'title'    => 'Hotéis',
        'icon'     => 'fa-hotel',
        'fields'   => [
            ['name' => 'nome',     'label' => 'Nome',     'type' => 'text',   'required' => true,
             'pattern' => $REGEX['nome'], 'hint' => '2 a 80 letras.'],
            ['name' => 'cnpj',     'label' => 'CNPJ',     'type' => 'text',   'required' => true,
             'pattern' => $REGEX['cnpj'], 'hint' => 'Ex.: 12.345.678/0001-90'],
            ['name' => 'estrelas', 'label' => 'Estrelas',  'type' => 'number', 'required' => true, 'min' => 1, 'max' => 5],
        ],
        'id_field' => 'codHotel',
        'display'  => 'nome',
    ],
    'tipos-quarto' => [
        'endpoint' => '/tipos-quarto',
        'title'    => 'Tipos de Quarto',
        'icon'     => 'fa-bed',
        'fields'   => [
            ['name' => 'nome',      'label' => 'Nome (Luxo, Standard...)', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['palavras']],
            ['name' => 'precoBase', 'label' => 'Preço Base', 'type' => 'text', 'required' => true,
             'money' => true, 'pattern' => $REGEX['money']],
        ],
        'id_field' => 'codTipo',
        'display'  => 'nome',
    ],
    'canais-reserva' => [
        'endpoint' => '/canais-reserva',
        'title'    => 'Canais de Reserva',
        'icon'     => 'fa-globe',
        'fields'   => [
            ['name' => 'nome', 'label' => 'Nome (Booking, Site, Balcão)', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['palavras']],
        ],
        'id_field' => 'codCanais',
        'display'  => 'nome',
    ],
    'servicos-extras' => [
        'endpoint' => '/servicos-extras',
        'title'    => 'Serviços Extras',
        'icon'     => 'fa-concierge-bell',
        'fields'   => [
            ['name' => 'nome',  'label' => 'Nome (Spa, Frigobar...)', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['palavras']],
            ['name' => 'preco', 'label' => 'Preço', 'type' => 'text', 'required' => true,
             'money' => true, 'pattern' => $REGEX['money']],
        ],
        'id_field' => 'codServicos',
        'display'  => 'nome',
    ],
    'nacionalidades' => [
        'endpoint' => '/nacionalidades',
        'title'    => 'Nacionalidades',
        'icon'     => 'fa-flag',
        'fields'   => [
            ['name' => 'nomePais', 'label' => 'País', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['pais']],
        ],
        'id_field' => 'codNacionalidade',
        'display'  => 'nomePais',
    ],
    'cargos' => [
        'endpoint' => '/cargos',
        'title'    => 'Cargos',
        'icon'     => 'fa-id-badge',
        'fields'   => [
            ['name' => 'nomeCargo',   'label' => 'Nome do Cargo', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['palavras']],
            ['name' => 'salarioBase', 'label' => 'Salário Base', 'type' => 'text', 'required' => true,
             'money' => true, 'pattern' => $REGEX['money']],
        ],
        'id_field' => 'codCargo',
        'display'  => 'nomeCargo',
    ],
    'quartos' => [
        'endpoint' => '/quartos',
        'title'    => 'Quartos',
        'icon'     => 'fa-door-open',
        'fields'   => [
            ['name' => 'numero',    'label' => 'Número',  'type' => 'number', 'required' => true, 'min' => 1],
            ['name' => 'andar',     'label' => 'Andar',   'type' => 'number', 'required' => true, 'min' => 0],
            ['name' => 'codHotelFk','label' => 'Hotel',   'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'hoteis', 'display' => 'nome']],
            ['name' => 'codTipoFk', 'label' => 'Tipo',    'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'tipos-quarto', 'display' => 'nome']],
        ],
        'id_field' => 'codQuarto',
        'display'  => 'numero',
    ],
    'imagens-quarto' => [
        'endpoint' => '/imagens-quarto',
        'title'    => 'Imagens de Quarto',
        'icon'     => 'fa-image',
        'fields'   => [
            ['name' => 'url',         'label' => 'Imagem',  'type' => 'text', 'required' => true,
             'image' => true, 'hint' => 'Faça upload de um arquivo ou cole uma URL.'],
            ['name' => 'codQuartoFk', 'label' => 'Quarto',  'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'quartos', 'display' => 'numero']],
        ],
        'id_field' => 'codImagem',
    ],
    'funcionarios' => [
        'endpoint' => '/funcionarios',
        'title'    => 'Funcionários',
        'icon'     => 'fa-user-tie',
        'fields'   => [
            ['name' => 'nome',       'label' => 'Nome', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['nome']],
            ['name' => 'codCargoFk', 'label' => 'Cargo', 'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'cargos', 'display' => 'nomeCargo']],
        ],
        'id_field' => 'codFuncionario',
        'display'  => 'nome',
    ],
    'hospedes' => [
        'endpoint' => '/hospedes',
        'title'    => 'Hóspedes',
        'icon'     => 'fa-users',
        'fields'   => [
            ['name' => 'nome',               'label' => 'Nome', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['nome']],
            ['name' => 'documento',          'label' => 'Documento (CPF/RG)', 'type' => 'text', 'required' => true,
             'pattern' => $REGEX['cpf_rg'], 'hint' => 'CPF (000.000.000-00) ou RG.'],
            ['name' => 'codNacionalidadeFk', 'label' => 'Nacionalidade', 'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'nacionalidades', 'display' => 'nomePais']],
        ],
        'id_field' => 'codHospede',
        'display'  => 'nome',
    ],
    'reservas' => [
        'endpoint' => '/reservas',
        'title'    => 'Reservas',
        'icon'     => 'fa-calendar-check',
        'fields'   => [
            ['name' => 'dataCheckin',  'label' => 'Data Check-in',  'type' => 'date', 'required' => true],
            ['name' => 'dataCheckout', 'label' => 'Data Check-out', 'type' => 'date', 'required' => true],
            ['name' => 'valorTotal',   'label' => 'Valor Total',    'type' => 'text', 'required' => true,
             'money' => true, 'pattern' => $REGEX['money']],
            ['name' => 'codCanalFk',   'label' => 'Canal',          'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'canais-reserva', 'display' => 'nome']],
        ],
        'id_field' => 'codReserva',
    ],
    'checkins' => [
        'endpoint' => '/checkins',
        'title'    => 'Check-ins',
        'icon'     => 'fa-sign-in-alt',
        'fields'   => [
            ['name' => 'dataHoraReal',     'label' => 'Data/Hora Real', 'type' => 'datetime-local', 'required' => true],
            ['name' => 'codReservaFk',     'label' => 'Reserva',        'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'reservas', 'display' => 'codReserva']],
            ['name' => 'codFuncionarioFk', 'label' => 'Funcionário',    'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'funcionarios', 'display' => 'nome']],
        ],
        'id_field' => 'codCheckin',
    ],
    'hospedes-reservas' => [
        'endpoint' => '/hospedes-reservas',
        'title'    => 'Hóspedes ↔ Reservas',
        'icon'     => 'fa-link',
        'fields'   => [
            ['name' => 'codHospedeFk', 'label' => 'Hóspede', 'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'hospedes', 'display' => 'nome']],
            ['name' => 'codReservaFk', 'label' => 'Reserva', 'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'reservas', 'display' => 'codReserva']],
        ],
        'id_field' => 'codHospedeReserva',
    ],
    'reservas-servicos' => [
        'endpoint' => '/reservas-servicos',
        'title'    => 'Reservas ↔ Serviços',
        'icon'     => 'fa-link',
        'fields'   => [
            ['name' => 'codReservaFk',  'label' => 'Reserva',  'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'reservas', 'display' => 'codReserva']],
            ['name' => 'codServicoFk',  'label' => 'Serviço',  'type' => 'number', 'required' => true,
             'fk' => ['entity' => 'servicos-extras', 'display' => 'nome']],
            ['name' => 'quantidade',    'label' => 'Quantidade', 'type' => 'number', 'required' => true, 'min' => 1],
        ],
        'id_field' => 'codReservaServico',
    ],
];
