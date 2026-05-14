<?php
/**
 * Configuração centralizada da API REST (Spring Boot).
 * Altere apenas o BASE_URL caso a porta ou host mude.
 */
define('API_BASE_URL', 'http://localhost:8080');

/**
 * Mapeamento de todas as entidades do sistema.
 * Cada chave é o slug usado na URL do PHP (ex: /hotel/?page=hoteis).
 * Contém: endpoint da API, título, ícone e definição dos campos.
 */
$ENTITIES = [
    'hoteis' => [
        'endpoint' => '/hoteis',
        'title'    => 'Hotéis',
        'icon'     => 'fa-hotel',
        'fields'   => [
            ['name' => 'nome',     'label' => 'Nome',     'type' => 'text',   'required' => true],
            ['name' => 'cnpj',     'label' => 'CNPJ',     'type' => 'text',   'required' => true],
            ['name' => 'estrelas', 'label' => 'Estrelas',  'type' => 'number', 'required' => true, 'min' => 1, 'max' => 5],
        ],
        'id_field' => 'codHotel',
    ],
    'tipos-quarto' => [
        'endpoint' => '/tipos-quarto',
        'title'    => 'Tipos de Quarto',
        'icon'     => 'fa-bed',
        'fields'   => [
            ['name' => 'nome',      'label' => 'Nome (Luxo, Standard...)', 'type' => 'text',   'required' => true],
            ['name' => 'precoBase', 'label' => 'Preço Base (R$)',          'type' => 'number', 'required' => true, 'step' => '0.01'],
        ],
        'id_field' => 'codTipo',
    ],
    'canais-reserva' => [
        'endpoint' => '/canais-reserva',
        'title'    => 'Canais de Reserva',
        'icon'     => 'fa-globe',
        'fields'   => [
            ['name' => 'nome', 'label' => 'Nome (Booking, Site, Balcão)', 'type' => 'text', 'required' => true],
        ],
        'id_field' => 'codCanais',
    ],
    'servicos-extras' => [
        'endpoint' => '/servicos-extras',
        'title'    => 'Serviços Extras',
        'icon'     => 'fa-concierge-bell',
        'fields'   => [
            ['name' => 'nome',  'label' => 'Nome (Spa, Frigobar...)', 'type' => 'text',   'required' => true],
            ['name' => 'preco', 'label' => 'Preço (R$)',              'type' => 'number', 'required' => true, 'step' => '0.01'],
        ],
        'id_field' => 'codServicos',
    ],
    'nacionalidades' => [
        'endpoint' => '/nacionalidades',
        'title'    => 'Nacionalidades',
        'icon'     => 'fa-flag',
        'fields'   => [
            ['name' => 'nomePais', 'label' => 'País', 'type' => 'text', 'required' => true],
        ],
        'id_field' => 'codNacionalidade',
    ],
    'cargos' => [
        'endpoint' => '/cargos',
        'title'    => 'Cargos',
        'icon'     => 'fa-id-badge',
        'fields'   => [
            ['name' => 'nomeCargo',   'label' => 'Nome do Cargo',     'type' => 'text',   'required' => true],
            ['name' => 'salarioBase', 'label' => 'Salário Base (R$)', 'type' => 'number', 'required' => true, 'step' => '0.01'],
        ],
        'id_field' => 'codCargo',
    ],
    'quartos' => [
        'endpoint' => '/quartos',
        'title'    => 'Quartos',
        'icon'     => 'fa-door-open',
        'fields'   => [
            ['name' => 'numero',    'label' => 'Número',          'type' => 'number', 'required' => true],
            ['name' => 'andar',     'label' => 'Andar',           'type' => 'number', 'required' => true],
            ['name' => 'codHotelFk','label' => 'Cód. Hotel (FK)', 'type' => 'number', 'required' => true],
            ['name' => 'codTipoFk', 'label' => 'Cód. Tipo (FK)', 'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codQuarto',
    ],
    'imagens-quarto' => [
        'endpoint' => '/imagens-quarto',
        'title'    => 'Imagens de Quarto',
        'icon'     => 'fa-image',
        'fields'   => [
            ['name' => 'url',         'label' => 'URL da Imagem',     'type' => 'text',   'required' => true],
            ['name' => 'codQuartoFk', 'label' => 'Cód. Quarto (FK)', 'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codImagem',
    ],
    'funcionarios' => [
        'endpoint' => '/funcionarios',
        'title'    => 'Funcionários',
        'icon'     => 'fa-user-tie',
        'fields'   => [
            ['name' => 'nome',       'label' => 'Nome',            'type' => 'text',   'required' => true],
            ['name' => 'codCargoFk', 'label' => 'Cód. Cargo (FK)','type' => 'number', 'required' => true],
        ],
        'id_field' => 'codFuncionario',
    ],
    'hospedes' => [
        'endpoint' => '/hospedes',
        'title'    => 'Hóspedes',
        'icon'     => 'fa-users',
        'fields'   => [
            ['name' => 'nome',                'label' => 'Nome',                     'type' => 'text', 'required' => true],
            ['name' => 'documento',           'label' => 'Documento (CPF/RG)',       'type' => 'text', 'required' => true],
            ['name' => 'codNacionalidadeFk',  'label' => 'Cód. Nacionalidade (FK)', 'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codHospede',
    ],
    'reservas' => [
        'endpoint' => '/reservas',
        'title'    => 'Reservas',
        'icon'     => 'fa-calendar-check',
        'fields'   => [
            ['name' => 'dataCheckin',  'label' => 'Data Check-in',       'type' => 'date',   'required' => true],
            ['name' => 'dataCheckout', 'label' => 'Data Check-out',      'type' => 'date',   'required' => true],
            ['name' => 'valorTotal',   'label' => 'Valor Total (R$)',    'type' => 'number', 'required' => true, 'step' => '0.01'],
            ['name' => 'codCanalFk',   'label' => 'Cód. Canal (FK)',     'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codReserva',
    ],
    'checkins' => [
        'endpoint' => '/checkins',
        'title'    => 'Check-ins',
        'icon'     => 'fa-sign-in-alt',
        'fields'   => [
            ['name' => 'dataHoraReal',     'label' => 'Data/Hora Real',          'type' => 'datetime-local', 'required' => true],
            ['name' => 'codReservaFk',     'label' => 'Cód. Reserva (FK)',       'type' => 'number', 'required' => true],
            ['name' => 'codFuncionarioFk', 'label' => 'Cód. Funcionário (FK)',   'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codCheckin',
    ],
    'hospedes-reservas' => [
        'endpoint' => '/hospedes-reservas',
        'title'    => 'Hóspedes ↔ Reservas',
        'icon'     => 'fa-link',
        'fields'   => [
            ['name' => 'codHospedeFk', 'label' => 'Cód. Hóspede (FK)', 'type' => 'number', 'required' => true],
            ['name' => 'codReservaFk', 'label' => 'Cód. Reserva (FK)', 'type' => 'number', 'required' => true],
        ],
        'id_field' => 'codHospedeReserva',
    ],
    'reservas-servicos' => [
        'endpoint' => '/reservas-servicos',
        'title'    => 'Reservas ↔ Serviços',
        'icon'     => 'fa-link',
        'fields'   => [
            ['name' => 'codReservaFk',  'label' => 'Cód. Reserva (FK)',  'type' => 'number', 'required' => true],
            ['name' => 'codServicoFk',  'label' => 'Cód. Serviço (FK)',  'type' => 'number', 'required' => true],
            ['name' => 'quantidade',    'label' => 'Quantidade',         'type' => 'number', 'required' => true, 'min' => 1],
        ],
        'id_field' => 'codReservaServico',
    ],
];
