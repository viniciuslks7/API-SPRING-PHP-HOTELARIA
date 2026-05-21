<?php
$items  = $result['data'] ?? [];
$config = $result['config'];
$slug   = $result['slug'];
$error  = $result['error'] ?? null;
$fkMaps = $result['fkMaps'] ?? [];

$entityTitle = $config['title'] ?? 'registro';
$displayField = $config['display'] ?? $config['id_field'];
?>

<div class="list-page" data-list-root data-slug="<?= htmlspecialchars($slug) ?>">
    <div class="action-bar">
        <div class="action-bar-left">
            <div class="search-wrapper">
                <i class="fas fa-search search-icon" aria-hidden="true"></i>
                <input type="search"
                       class="search-input"
                       data-list-search
                       placeholder="Buscar em <?= htmlspecialchars(strtolower($entityTitle)) ?>..."
                       aria-label="Buscar em <?= htmlspecialchars($entityTitle) ?>"
                       autocomplete="off">
                <kbd class="search-hint" aria-hidden="true">/</kbd>
            </div>
            <span class="record-count" role="status" aria-live="polite">
                <i class="fas fa-database" aria-hidden="true"></i>
                <span data-record-count><?= count($items) ?></span> de <?= count($items) ?> registro(s)
            </span>
        </div>
        <a href="?page=<?= $slug ?>&action=create" class="btn btn-primary">
            <i class="fas fa-plus" aria-hidden="true"></i> Novo Registro
        </a>
    </div>

    <?php if ($error): ?>
    <div class="alert alert-error" role="alert">
        <i class="fas fa-exclamation-triangle" aria-hidden="true"></i>
        <span><?= htmlspecialchars($error) ?></span>
    </div>
    <?php endif; ?>

    <?php if (empty($items) && !$error): ?>
    <div class="empty-state">
        <i class="fas fa-inbox" aria-hidden="true"></i>
        <h3>Nenhum registro encontrado</h3>
        <p>Clique em "Novo Registro" para começar.</p>
    </div>
    <?php else: ?>

    <div class="table-container">
        <table class="data-table" data-list-table>
            <caption class="visually-hidden">Lista de <?= htmlspecialchars($entityTitle) ?></caption>
            <thead>
                <tr>
                    <th scope="col" class="th-id th-sortable" data-sort-key="__id" data-sort-type="number">
                        <button type="button" class="sort-btn" aria-label="Ordenar por ID">
                            ID <span class="sort-indicator" aria-hidden="true"></span>
                        </button>
                    </th>
                    <?php foreach ($config['fields'] as $field):
                        $sortType = (!empty($field['money']) || $field['type'] === 'number' || $field['type'] === 'date' || $field['type'] === 'datetime-local')
                            ? ($field['type'] === 'date' || $field['type'] === 'datetime-local' ? 'date' : 'number')
                            : 'string';
                        $isImage = !empty($field['image']);
                    ?>
                    <th scope="col" <?= $isImage ? '' : 'class="th-sortable"' ?>
                        <?= $isImage ? '' : 'data-sort-key="' . htmlspecialchars($field['name']) . '" data-sort-type="' . $sortType . '"' ?>>
                        <?php if ($isImage): ?>
                            <?= htmlspecialchars($field['label']) ?>
                        <?php else: ?>
                            <button type="button" class="sort-btn" aria-label="Ordenar por <?= htmlspecialchars($field['label']) ?>">
                                <?= htmlspecialchars($field['label']) ?>
                                <span class="sort-indicator" aria-hidden="true"></span>
                            </button>
                        <?php endif; ?>
                    </th>
                    <?php endforeach; ?>
                    <th scope="col" class="th-actions">Ações</th>
                </tr>
            </thead>
            <tbody data-list-tbody>
                <?php foreach ($items as $rowIdx => $item):
                    $itemId    = $item[$config['id_field']] ?? '';
                    $itemLabel = $item[$displayField] ?? $itemId;
                    $searchParts = [(string)$itemId];
                    foreach ($config['fields'] as $f) {
                        $v = $item[$f['name']] ?? null;
                        if ($v === null || $v === '') continue;
                        if (!empty($f['image'])) continue;
                        if (!empty($f['fk'])) {
                            $fkLabel = $fkMaps[$f['name']][$v] ?? null;
                            if ($fkLabel !== null) $searchParts[] = (string)$fkLabel;
                        }
                        $searchParts[] = (string)$v;
                    }
                    $searchBlob = mb_strtolower(implode(' ', $searchParts), 'UTF-8');
                ?>
                <tr style="--i: <?= $rowIdx ?>"
                    data-row
                    data-search="<?= htmlspecialchars($searchBlob) ?>"
                    data-sort-__id="<?= htmlspecialchars((string)$itemId) ?>"
                    <?php foreach ($config['fields'] as $f):
                        if (!empty($f['image'])) continue;
                        $sv = $item[$f['name']] ?? '';
                        if (!empty($f['fk'])) {
                            $sv = $fkMaps[$f['name']][$sv] ?? $sv;
                        }
                    ?>
                    data-sort-<?= htmlspecialchars($f['name']) ?>="<?= htmlspecialchars((string)$sv) ?>"
                    <?php endforeach; ?>>
                    <th scope="row" class="td-id"><?= htmlspecialchars((string)$itemId) ?></th>
                    <?php foreach ($config['fields'] as $field):
                        $name = $field['name'];
                        $val  = $item[$name] ?? null;
                        $tdClasses = [];
                        if (!empty($field['money'])) $tdClasses[] = 'td-money';
                        if (!empty($field['image'])) $tdClasses[] = 'td-thumb';
                        if (!empty($field['fk']))    $tdClasses[] = 'td-fk-name';
                        $tdClassAttr = $tdClasses ? ' class="' . implode(' ', $tdClasses) . '"' : '';
                    ?>
                    <td<?= $tdClassAttr ?>>
                        <?php
                        if ($val === null || $val === '') {
                            echo '—';
                        } elseif (!empty($field['fk'])) {
                            $label = $fkMaps[$name][$val] ?? null;
                            if ($label !== null) {
                                echo htmlspecialchars((string)$label);
                                echo '<small>#' . htmlspecialchars((string)$val) . '</small>';
                            } else {
                                echo '#' . htmlspecialchars((string)$val);
                            }
                        } elseif (!empty($field['image'])) {
                            $altDesc = 'Imagem de ' . $entityTitle . ' (id ' . $itemId . ')';
                            echo '<img src="' . htmlspecialchars((string)$val) . '" alt="' . htmlspecialchars($altDesc) . '" loading="lazy">';
                        } elseif (!empty($field['money'])) {
                            echo 'R$ ' . number_format((float)$val, 2, ',', '.');
                        } else {
                            echo htmlspecialchars((string)$val);
                        }
                        ?>
                    </td>
                    <?php endforeach; ?>
                    <td class="td-actions">
                        <a href="?page=<?= $slug ?>&action=edit&id=<?= $itemId ?>"
                           class="btn-icon btn-edit"
                           aria-label="Editar <?= htmlspecialchars((string)$itemLabel) ?>">
                            <i class="fas fa-pen" aria-hidden="true"></i>
                        </a>
                        <button type="button" class="btn-icon btn-delete"
                                aria-label="Excluir <?= htmlspecialchars((string)$itemLabel) ?>"
                                onclick="confirmDelete('<?= $slug ?>', <?= $itemId ?>)">
                            <i class="fas fa-trash" aria-hidden="true"></i>
                        </button>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

        <div class="empty-state empty-no-results" data-empty-search hidden>
            <i class="fas fa-search" aria-hidden="true"></i>
            <h3>Nenhum resultado</h3>
            <p>Tente outro termo de busca.</p>
        </div>

        <div class="pagination" data-pagination hidden>
            <div class="pagination-info">
                Página <span data-page-current>1</span> de <span data-page-total>1</span>
            </div>
            <div class="pagination-controls">
                <label class="pagination-size">
                    <span class="visually-hidden">Registros por página</span>
                    <select data-page-size aria-label="Registros por página">
                        <option value="10">10 / pág</option>
                        <option value="25" selected>25 / pág</option>
                        <option value="50">50 / pág</option>
                        <option value="100">100 / pág</option>
                    </select>
                </label>
                <button type="button" class="btn-icon btn-page" data-page-prev aria-label="Página anterior">
                    <i class="fas fa-chevron-left" aria-hidden="true"></i>
                </button>
                <button type="button" class="btn-icon btn-page" data-page-next aria-label="Próxima página">
                    <i class="fas fa-chevron-right" aria-hidden="true"></i>
                </button>
            </div>
        </div>
    </div>
    <?php endif; ?>
</div>
