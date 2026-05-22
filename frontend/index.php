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
        // Aplica tema e configurações de acessibilidade antes do paint pra evitar flash.
        (function() {
            var root = document.documentElement;
            root.setAttribute('data-theme', localStorage.getItem('hm-theme') || 'dark');

            var scale = localStorage.getItem('hm-a11y-font-scale');
            if (scale) root.style.setProperty('--a11y-font-scale', scale);

            ['contrast', 'underline', 'motion', 'guide'].forEach(function(key) {
                var v = localStorage.getItem('hm-a11y-' + key);
                if (v !== null) root.setAttribute('data-a11y-' + key, v);
            });
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

    <!-- Backdrop pra fechar sidebar no mobile -->
    <div class="sidebar-backdrop" data-sidebar-backdrop hidden></div>

    <!-- MAIN CONTENT -->
    <main class="main-content" id="main-content" tabindex="-1" role="main">
        <!-- Top Bar -->
        <header class="top-bar" role="banner">
            <button class="sidebar-toggle" onclick="toggleSidebar()" aria-label="Abrir/fechar menu lateral" aria-controls="sidebar">
                <i class="fas fa-bars" aria-hidden="true"></i>
            </button>
            <div class="top-bar-info">
                <?php
                $action = $_GET['action'] ?? null;
                $isEntity = isset($ENTITIES[$page]);
                ?>
                <nav class="breadcrumbs" aria-label="Trilha de navegação">
                    <a href="?page=dashboard" class="crumb">
                        <i class="fas fa-house" aria-hidden="true"></i>
                        <span>Início</span>
                    </a>
                    <?php if ($isEntity): ?>
                        <span class="crumb-sep" aria-hidden="true">/</span>
                        <a href="?page=<?= htmlspecialchars($page) ?>" class="crumb <?= !$action ? 'crumb-current' : '' ?>"
                           <?= !$action ? 'aria-current="page"' : '' ?>>
                            <i class="fas <?= htmlspecialchars($ENTITIES[$page]['icon']) ?>" aria-hidden="true"></i>
                            <span><?= htmlspecialchars($ENTITIES[$page]['title']) ?></span>
                        </a>
                        <?php if ($action === 'create'): ?>
                            <span class="crumb-sep" aria-hidden="true">/</span>
                            <span class="crumb crumb-current" aria-current="page">Novo</span>
                        <?php elseif ($action === 'edit'): ?>
                            <span class="crumb-sep" aria-hidden="true">/</span>
                            <span class="crumb crumb-current" aria-current="page">Editar #<?= htmlspecialchars($_GET['id'] ?? '') ?></span>
                        <?php endif; ?>
                    <?php endif; ?>
                </nav>
                <h1 class="page-title visually-hidden">
                    <?php
                    if ($page === 'dashboard') echo 'Dashboard';
                    elseif ($isEntity) echo $ENTITIES[$page]['title'];
                    ?>
                </h1>
            </div>
            <div class="top-bar-badge">
                <span class="badge-api"><i class="fas fa-plug" aria-hidden="true"></i> API Spring Boot</span>
            </div>
            <button type="button" class="cmdk-trigger" onclick="openCommandPalette()" aria-label="Abrir paleta de comandos" aria-haspopup="dialog">
                <i class="fas fa-search" aria-hidden="true"></i>
                <span>Buscar…</span>
                <kbd>Ctrl K</kbd>
            </button>
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

    <!-- Command Palette -->
    <div class="cmdk-overlay" id="cmdk-overlay" hidden role="dialog" aria-modal="true" aria-label="Paleta de comandos">
        <div class="cmdk-panel">
            <div class="cmdk-input-wrap">
                <i class="fas fa-search" aria-hidden="true"></i>
                <input type="text"
                       id="cmdk-input"
                       class="cmdk-input"
                       placeholder="Ir para... (digite o nome da entidade)"
                       autocomplete="off"
                       aria-label="Buscar comando">
                <kbd>ESC</kbd>
            </div>
            <ul class="cmdk-list" id="cmdk-list" role="listbox" aria-label="Resultados">
                <li class="cmdk-item" role="option" data-cmd="?page=dashboard">
                    <i class="fas fa-chart-line" aria-hidden="true"></i>
                    <span class="cmdk-label">Dashboard</span>
                    <span class="cmdk-hint">Principal</span>
                </li>
                <?php foreach ($ENTITIES as $cmdSlug => $cmdCfg): ?>
                <li class="cmdk-item" role="option" data-cmd="?page=<?= htmlspecialchars($cmdSlug) ?>">
                    <i class="fas <?= htmlspecialchars($cmdCfg['icon']) ?>" aria-hidden="true"></i>
                    <span class="cmdk-label"><?= htmlspecialchars($cmdCfg['title']) ?></span>
                    <span class="cmdk-hint"><?= htmlspecialchars($cmdSlug) ?></span>
                </li>
                <?php endforeach; ?>
                <li class="cmdk-item" role="option" data-cmd="__theme__">
                    <i class="fas fa-circle-half-stroke" aria-hidden="true"></i>
                    <span class="cmdk-label">Alternar tema claro/escuro</span>
                    <span class="cmdk-hint">Ação</span>
                </li>
            </ul>
            <div class="cmdk-empty" id="cmdk-empty" hidden>Nenhum comando encontrado.</div>
            <div class="cmdk-footer">
                <span><kbd>↑</kbd><kbd>↓</kbd> navegar</span>
                <span><kbd>Enter</kbd> abrir</span>
                <span><kbd>ESC</kbd> fechar</span>
            </div>
        </div>
    </div>

    <!-- Toast container (top-right) -->
    <div class="toast-container" id="toast-container" aria-live="polite" aria-atomic="false"></div>

    <!-- Menu de Acessibilidade (FAB + drawer) -->
    <div class="a11y-menu" id="a11y-menu">
        <button type="button"
                class="a11y-fab"
                id="a11y-fab"
                onclick="toggleA11yMenu()"
                aria-label="Abrir menu de acessibilidade"
                aria-expanded="false"
                aria-controls="a11y-drawer">
            <i class="fas fa-universal-access" aria-hidden="true"></i>
        </button>
        <div class="a11y-drawer" id="a11y-drawer" hidden role="dialog" aria-label="Opções de acessibilidade">
            <div class="a11y-drawer-header">
                <h2><i class="fas fa-universal-access" aria-hidden="true"></i> Acessibilidade</h2>
                <button type="button" class="a11y-close" onclick="toggleA11yMenu()" aria-label="Fechar menu">
                    <i class="fas fa-xmark" aria-hidden="true"></i>
                </button>
            </div>
            <div class="a11y-drawer-body">
                <fieldset class="a11y-group">
                    <legend>Tamanho do texto</legend>
                    <div class="a11y-font-controls">
                        <button type="button" data-a11y-font="decrease" aria-label="Diminuir fonte">A−</button>
                        <button type="button" data-a11y-font="reset" aria-label="Tamanho padrão">A</button>
                        <button type="button" data-a11y-font="increase" aria-label="Aumentar fonte">A+</button>
                    </div>
                </fieldset>
                <fieldset class="a11y-group">
                    <legend>Visualização</legend>
                    <label class="a11y-toggle">
                        <input type="checkbox" data-a11y-toggle="contrast">
                        <span><i class="fas fa-circle-half-stroke" aria-hidden="true"></i> Alto contraste</span>
                    </label>
                    <label class="a11y-toggle">
                        <input type="checkbox" data-a11y-toggle="underline-links">
                        <span><i class="fas fa-underline" aria-hidden="true"></i> Sublinhar links</span>
                    </label>
                    <label class="a11y-toggle">
                        <input type="checkbox" data-a11y-toggle="reduce-motion">
                        <span><i class="fas fa-pause" aria-hidden="true"></i> Pausar animações</span>
                    </label>
                    <label class="a11y-toggle">
                        <input type="checkbox" data-a11y-toggle="reading-guide">
                        <span><i class="fas fa-grip-lines" aria-hidden="true"></i> Régua de leitura</span>
                    </label>
                </fieldset>
                <button type="button" class="a11y-reset" onclick="resetA11ySettings()">
                    <i class="fas fa-rotate-left" aria-hidden="true"></i> Restaurar padrões
                </button>
            </div>
        </div>
    </div>

    <!-- VLibras (Tradutor Libras oficial gov.br) -->
    <div vw class="enabled">
        <div vw-access-button class="active"></div>
        <div vw-plugin-wrapper>
            <div class="vw-plugin-top-wrapper"></div>
        </div>
    </div>
    <script src="https://vlibras.gov.br/app/vlibras-plugin.js"></script>
    <script>
        new window.VLibras.Widget('https://vlibras.gov.br/app');
    </script>

    <!-- Custom JS -->
    <script src="assets/js/app.js?v=<?= @filemtime(__DIR__ . '/assets/js/app.js') ?: time() ?>"></script>

    <?php if ($successMsg): ?>
    <script>
        const __msg = <?= json_encode($successMsg === "created" ? "Registro criado." : ($successMsg === "updated" ? "Registro atualizado." : "Registro removido.")) ?>;
        document.addEventListener('DOMContentLoaded', () => showToast('success', __msg));
    </script>
    <?php endif; ?>

    <?php if ($errorMsg): ?>
    <script>
        const __err = <?= json_encode($errorMsg) ?>;
        document.addEventListener('DOMContentLoaded', () => showToast('error', __err));
    </script>
    <?php endif; ?>

</body>
</html>
