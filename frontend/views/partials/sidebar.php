<?php $navIdx = 0; ?>
<aside class="sidebar" id="sidebar" aria-label="Navegação principal">
    <div class="sidebar-brand">
        <div class="brand-icon" aria-hidden="true">
            <i class="fas fa-hotel"></i>
        </div>
        <div class="brand-text">
            <h2>Hotel<span>Manager</span></h2>
            <small>Sistema de Gestão</small>
        </div>
    </div>

    <nav class="sidebar-nav" aria-label="Menu de seções">
        <div class="nav-section">
            <span class="nav-section-title" id="nav-principal">Principal</span>
            <a href="?page=dashboard" class="nav-link <?= ($page ?? '') === 'dashboard' ? 'active' : '' ?>"
               <?= ($page ?? '') === 'dashboard' ? 'aria-current="page"' : '' ?>
               style="--i: <?= $navIdx++ ?>">
                <i class="fas fa-chart-line" aria-hidden="true"></i>
                <span>Dashboard</span>
            </a>
        </div>

        <div class="nav-section" aria-labelledby="nav-base">
            <span class="nav-section-title" id="nav-base">Cadastros Base</span>
            <?php
            $baseEntities = ['hoteis', 'tipos-quarto', 'canais-reserva', 'servicos-extras', 'nacionalidades', 'cargos'];
            foreach ($baseEntities as $slug):
                $e = $ENTITIES[$slug];
                $isActive = ($page ?? '') === $slug;
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= $isActive ? 'active' : '' ?>"
               <?= $isActive ? 'aria-current="page"' : '' ?>
               style="--i: <?= $navIdx++ ?>">
                <i class="fas <?= $e['icon'] ?>" aria-hidden="true"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>

        <div class="nav-section" aria-labelledby="nav-ops">
            <span class="nav-section-title" id="nav-ops">Operações</span>
            <?php
            $opEntities = ['quartos', 'imagens-quarto', 'funcionarios', 'hospedes', 'reservas', 'checkins'];
            foreach ($opEntities as $slug):
                $e = $ENTITIES[$slug];
                $isActive = ($page ?? '') === $slug;
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= $isActive ? 'active' : '' ?>"
               <?= $isActive ? 'aria-current="page"' : '' ?>
               style="--i: <?= $navIdx++ ?>">
                <i class="fas <?= $e['icon'] ?>" aria-hidden="true"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>

        <div class="nav-section" aria-labelledby="nav-nn">
            <span class="nav-section-title" id="nav-nn">Relações N:N</span>
            <?php
            $nnEntities = ['hospedes-reservas', 'reservas-servicos'];
            foreach ($nnEntities as $slug):
                $e = $ENTITIES[$slug];
                $isActive = ($page ?? '') === $slug;
            ?>
            <a href="?page=<?= $slug ?>" class="nav-link <?= $isActive ? 'active' : '' ?>"
               <?= $isActive ? 'aria-current="page"' : '' ?>
               style="--i: <?= $navIdx++ ?>">
                <i class="fas <?= $e['icon'] ?>" aria-hidden="true"></i>
                <span><?= $e['title'] ?></span>
            </a>
            <?php endforeach; ?>
        </div>
    </nav>

    <div class="sidebar-footer">
        <div class="sidebar-footer-info">
            <small>FATEC Jales — 2026</small>
            <small>Vinicius &amp; Yuri</small>
        </div>
    </div>
</aside>
