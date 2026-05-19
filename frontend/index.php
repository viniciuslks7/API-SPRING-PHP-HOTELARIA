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
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Manager — Sistema de Gestão</title>
    <meta name="description" content="Sistema de gestão hoteleira — FATEC Jales">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Custom CSS -->
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

    <!-- SIDEBAR -->
    <?php include __DIR__ . '/views/partials/sidebar.php'; ?>

    <!-- MAIN CONTENT -->
    <main class="main-content">
        <!-- Top Bar -->
        <header class="top-bar">
            <button class="sidebar-toggle" onclick="toggleSidebar()">
                <i class="fas fa-bars"></i>
            </button>
            <div class="top-bar-info">
                <h1 class="page-title">
                    <?php
                    if ($page === 'dashboard') {
                        echo '<i class="fas fa-chart-line"></i> Dashboard';
                    } elseif (isset($ENTITIES[$page])) {
                        echo '<i class="fas ' . $ENTITIES[$page]['icon'] . '"></i> ' . $ENTITIES[$page]['title'];
                    }
                    ?>
                </h1>
            </div>
            <div class="top-bar-badge">
                <span class="badge-api"><i class="fas fa-plug"></i> API Spring Boot</span>
            </div>
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
    <script src="assets/js/app.js"></script>

    <?php if ($successMsg): ?>
    <script>
        Swal.fire({
            icon: 'success',
            title: <?= json_encode($successMsg === "created" ? "Criado!" : ($successMsg === "updated" ? "Atualizado!" : "Deletado!")) ?>,
            text: 'Operação realizada com sucesso.',
            timer: 2000,
            showConfirmButton: false,
            background: '#1e1e2e',
            color: '#cdd6f4',
        });
    </script>
    <?php endif; ?>

    <?php if ($errorMsg): ?>
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: <?= json_encode($errorMsg) ?>,
            background: '#1e1e2e',
            color: '#cdd6f4',
        });
    </script>
    <?php endif; ?>

</body>
</html>
