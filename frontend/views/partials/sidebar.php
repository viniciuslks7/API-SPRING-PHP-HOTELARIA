<aside class="sidebar" id="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon">
            <i class="fas fa-hotel"></i>
        </div>
        <div class="brand-text">
            <h2>Hotel<span>Manager</span></h2>
            <small>Sistema de Gestão</small>
        </div>
    </div>

    <nav class="sidebar-nav">
        <div class="nav-section">
            <span class="nav-section-title">Principal</span>
            <a href="?page=dashboard" class="nav-link <?= ($page ?? '') === 'dashboard' ? 'active' : '' ?>">
                <i class="fas fa-chart-line"></i>
                <span>Dashboard</span>
            </a>
        </div>

        <div class="nav-section">
            <span class="nav-section-title">Cadastros Base</span>
            <?php
            $baseEntities = ['hoteis', 'tipos-quarto', 'canais-reserva', 'servicos-extras', 'nacionalidades', 'cargos'];
            foreach ($baseEntities as $slug):
                $e = $ENTITIES[$slug];
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= ($page ?? '') === $slug ? 'active' : '' ?>">
                <i class="fas <?= $e['icon'] ?>"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>

        <div class="nav-section">
            <span class="nav-section-title">Operações</span>
            <?php
            $opEntities = ['quartos', 'imagens-quarto', 'funcionarios', 'hospedes', 'reservas', 'checkins'];
            foreach ($opEntities as $slug):
                $e = $ENTITIES[$slug];
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= ($page ?? '') === $slug ? 'active' : '' ?>">
                <i class="fas <?= $e['icon'] ?>"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>

        <div class="nav-section">
            <span class="nav-section-title">Relações N:N</span>
            <?php
            $nnEntities = ['hospedes-reservas', 'reservas-servicos'];
            foreach ($nnEntities as $slug):
                $e = $ENTITIES[$slug];
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= ($page ?? '') === $slug ? 'active' : '' ?>">
                <i class="fas <?= $e['icon'] ?>"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>
    </nav>

    <div class="sidebar-footer">
        <div class="sidebar-footer-info">
            <small>FATEC Jales — 2026</small>
            <small>Vinicius & Yuri</small>
        </div>
    </div>
</aside>
