<div class="dashboard">
    <div class="dashboard-welcome">
        <div class="welcome-text">
            <h2>Bem-vindo ao Hotel<span>Manager</span></h2>
            <p>Painel de controle do ecossistema de gestão hoteleira. Monitore todas as entidades da API em tempo real.</p>
        </div>
        <div class="welcome-badge" role="status" aria-live="polite">
            <i class="fas fa-server" aria-hidden="true"></i>
            <span>API Online</span>
        </div>
    </div>

    <div class="stats-grid">
        <?php foreach ($counts as $slug => $info): ?>
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
                <i class="fas fa-database" aria-hidden="true"></i>
                <h3>Entidades</h3>
            </div>
            <ul>
                <li><strong>Tabelas de apoio:</strong> 6 (sem FK)</li>
                <li><strong>Tabelas 1:N:</strong> 6 (com FK)</li>
                <li><strong>Tabelas N:N:</strong> 2 (associativas)</li>
                <li><strong>Total:</strong> 14 tabelas</li>
            </ul>
        </div>
    </div>
</div>
