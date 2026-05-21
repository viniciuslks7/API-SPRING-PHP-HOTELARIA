<?php
/**
 * ╔══════════════════════════════════════════════════════════════╗
 *   Hotel Management System — Frontend PHP
 *   Consome a API REST Spring Boot via cURL
 *   FATEC Jales — Arquitetura Orientada a Serviços (2026)
 *   Autores: Vinicius Siqueira & Yuri
 * ╚══════════════════════════════════════════════════════════════╝
 */

// --- Bootstrap ---
require_once __DIR__ . '/config/api.php';
require_once __DIR__ . '/services/ApiClient.php';
require_once __DIR__ . '/controllers/CrudController.php';

$api = new ApiClient();

// --- Roteamento ---
$page   = $_GET['page'] ?? 'dashboard';
$result = null;

if ($page === 'dashboard') {
    // Dashboard: busca contagem de todas as entidades
    $counts = [];
    foreach ($ENTITIES as $slug => $config) {
        $response = $api->get($config['endpoint']);
        $counts[$slug] = [
            'title' => $config['title'],
            'icon'  => $config['icon'],
            'count' => is_array($response['data']) ? count($response['data']) : 0,
            'error' => !$response['success'],
        ];
    }
} elseif (isset($ENTITIES[$page])) {
    $controller = new CrudController($api, $page, $ENTITIES[$page]);
    $result     = $controller->handle();
}

// --- Flash messages ---
$successMsg = $_GET['success'] ?? null;
$errorMsg   = $_GET['error'] ?? null;
?>
<!DOCTYPE html>
<html lang="pt-BR" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Manager — Sistema de Gestão</title>
    <script>
        // Aplica o tema antes do paint para evitar flash.
        (function() {
            var t = localStorage.getItem('hm-theme') || 'dark';
            document.documentElement.setAttribute('data-theme', t);
        })();
    </script>
    <meta name="description" content="Sistema de gestão hoteleira — FATEC Jales">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Fraunces:opsz,wght@9..144,500;9..144,600;9..144,700&display=swap" rel="stylesheet">

    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Custom CSS -->
    <link rel="stylesheet" href="assets/css/style.css?v=<?= @filemtime(__DIR__ . '/assets/css/style.css') ?: time() ?>">
</head>
<body>

    <a href="#main-content" class="skip-link">Pular para o conteúdo</a>

    <!-- SIDEBAR -->
    <?php include __DIR__ . '/views/partials/sidebar.php'; ?>

    <!-- MAIN CONTENT -->
    <main class="main-content" id="main-content" tabindex="-1">
        <!-- Top Bar -->
        <header class="top-bar">
            <button class="sidebar-toggle" onclick="toggleSidebar()" aria-label="Abrir/fechar menu lateral" aria-controls="sidebar">
                <i class="fas fa-bars" aria-hidden="true"></i>
            </button>
            <div class="top-bar-info">
                <h1 class="page-title">
                    <?php
                    if ($page === 'dashboard') {
                        echo '<i class="fas fa-chart-line" aria-hidden="true"></i> Dashboard';
                    } elseif (isset($ENTITIES[$page])) {
                        echo '<i class="fas ' . $ENTITIES[$page]['icon'] . '" aria-hidden="true"></i> ' . $ENTITIES[$page]['title'];
                    }
                    ?>
                </h1>
            </div>
            <div class="top-bar-badge">
                <span class="badge-api"><i class="fas fa-plug" aria-hidden="true"></i> API Spring Boot</span>
            </div>
            <button class="theme-toggle" onclick="toggleTheme()" aria-label="Alternar entre tema claro e escuro" aria-pressed="false" id="theme-toggle-btn">
                <i class="fas fa-moon icon-dark" aria-hidden="true"></i>
                <i class="fas fa-sun icon-light" aria-hidden="true"></i>
            </button>
        </header>

        <!-- Content Area -->
        <div class="content-area">
            <?php
            if ($page === 'dashboard') {
                include __DIR__ . '/views/pages/dashboard.php';
            } elseif (isset($ENTITIES[$page]) && $result) {
                if ($result['view'] === 'list') {
                    include __DIR__ . '/views/pages/list.php';
                } elseif ($result['view'] === 'form') {
                    include __DIR__ . '/views/pages/form.php';
                }
            } else {
                echo '<div class="empty-state"><i class="fas fa-compass"></i><p>Página não encontrada</p></div>';
            }
            ?>
        </div>
    </main>

    <!-- Custom JS -->
    <script src="assets/js/app.js?v=<?= @filemtime(__DIR__ . '/assets/js/app.js') ?: time() ?>"></script>

    <?php if ($successMsg): ?>
    <script>
        Swal.fire({
            ...swalTheme(),
            icon: 'success',
            title: <?= json_encode($successMsg === "created" ? "Criado!" : ($successMsg === "updated" ? "Atualizado!" : "Deletado!")) ?>,
            text: 'Operação realizada com sucesso.',
            timer: 2000,
            showConfirmButton: false,
        });
    </script>
    <?php endif; ?>

    <?php if ($errorMsg): ?>
    <script>
        Swal.fire({
            ...swalTheme(),
            icon: 'error',
            title: 'Erro',
            text: <?= json_encode($errorMsg) ?>,
        });
    </script>
    <?php endif; ?>

</body>
</html>
