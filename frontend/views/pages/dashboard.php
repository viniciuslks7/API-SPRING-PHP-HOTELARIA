<?php
$groups = [
    'Cadastros base'  => ['hoteis', 'tipos-quarto', 'canais-reserva', 'servicos-extras', 'nacionalidades', 'cargos'],
    'Operações'       => ['quartos', 'imagens-quarto', 'funcionarios', 'hospedes', 'reservas', 'checkins'],
    'Relações N:N'    => ['hospedes-reservas', 'reservas-servicos'],
];

$totalRecords = 0;
$totalErrors  = 0;
foreach ($counts as $info) {
    $totalRecords += (int)$info['count'];
    if ($info['error']) $totalErrors++;
}
?>
<div class="dashboard">
    <div class="dashboard-welcome">
        <div class="welcome-text">
            <h2>Bem-vindo ao Hotel<span>Manager</span></h2>
            <p>Painel de controle do ecossistema. <strong><?= $totalRecords ?></strong> registro(s) em <strong><?= count($counts) ?></strong> entidades.</p>
        </div>
        <div class="welcome-badges">
            <div class="welcome-badge" role="status" aria-live="polite">
                <i class="fas fa-server" aria-hidden="true"></i>
                <span><?= $totalErrors === 0 ? 'API Online' : "API com $totalErrors erro(s)" ?></span>
            </div>
        </div>
    </div>

    <?php foreach ($groups as $groupTitle => $slugs): ?>
    <section class="dashboard-section" aria-labelledby="grp-<?= md5($groupTitle) ?>">
        <header class="dashboard-section-header">
            <h2 id="grp-<?= md5($groupTitle) ?>"><?= htmlspecialchars($groupTitle) ?></h2>
            <span class="dashboard-section-count"><?= count($slugs) ?> entidade(s)</span>
        </header>
        <div class="stats-grid">
            <?php foreach ($slugs as $slug):
                if (!isset($counts[$slug])) continue;
                $info = $counts[$slug];
            ?>
            <a href="?page=<?= $slug ?>" class="stat-card <?= $info['error'] ? 'stat-error' : '' ?>"
               aria-label="<?= htmlspecialchars($info['title']) ?>: <?= $info['error'] ? 'erro ao carregar' : $info['count'] . ' registro(s)' ?>">
                <div class="stat-icon" aria-hidden="true">
                    <i class="fas <?= $info['icon'] ?>"></i>
                </div>
                <div class="stat-info">
                    <span class="stat-count"><?= $info['error'] ? '—' : $info['count'] ?></span>
                    <span class="stat-label"><?= $info['title'] ?></span>
                </div>
                <div class="stat-arrow" aria-hidden="true">
                    <i class="fas fa-arrow-right"></i>
                </div>
            </a>
            <?php endforeach; ?>
        </div>
    </section>
    <?php endforeach; ?>

    <div class="dashboard-info-cards">
        <div class="info-card">
            <div class="info-card-header">
                <i class="fas fa-layer-group" aria-hidden="true"></i>
                <h3>Arquitetura</h3>
            </div>
            <ul>
                <li><strong>Backend:</strong> Spring Boot 4 + JPA/Hibernate</li>
                <li><strong>Banco:</strong> PostgreSQL 18</li>
                <li><strong>Frontend:</strong> PHP Vanilla + cURL</li>
                <li><strong>Padrão:</strong> MVC + DTOs + Service Layer</li>
            </ul>
        </div>
        <div class="info-card">
            <div class="info-card-header">
                <i class="fas fa-keyboard" aria-hidden="true"></i>
                <h3>Atalhos</h3>
            </div>
            <ul>
                <li><kbd class="kbd-inline">Ctrl K</kbd> abrir paleta de comandos</li>
                <li><kbd class="kbd-inline">/</kbd> focar busca na listagem</li>
                <li><kbd class="kbd-inline">↑↓ Enter</kbd> navegar na paleta</li>
                <li><kbd class="kbd-inline">Esc</kbd> fechar paleta</li>
            </ul>
        </div>
    </div>
</div>
