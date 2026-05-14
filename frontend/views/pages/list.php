<?php
$items  = $result['data'] ?? [];
$config = $result['config'];
$slug   = $result['slug'];
$error  = $result['error'] ?? null;
?>

<div class="list-page">
    <!-- Action Bar -->
    <div class="action-bar">
        <div class="action-bar-left">
            <span class="record-count">
                <i class="fas fa-database"></i>
                <?= count($items) ?> registro(s)
            </span>
        </div>
        <a href="?page=<?= $slug ?>&action=create" class="btn btn-primary">
            <i class="fas fa-plus"></i> Novo Registro
        </a>
    </div>

    <?php if ($error): ?>
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i>
        <span><?= htmlspecialchars($error) ?></span>
    </div>
    <?php endif; ?>

    <?php if (empty($items) && !$error): ?>
    <div class="empty-state">
        <i class="fas fa-inbox"></i>
        <h3>Nenhum registro encontrado</h3>
        <p>Clique em "Novo Registro" para começar.</p>
    </div>
    <?php else: ?>

    <!-- Data Table -->
    <div class="table-container">
        <table class="data-table">
            <thead>
                <tr>
                    <th class="th-id">ID</th>
                    <?php foreach ($config['fields'] as $field): ?>
                    <th><?= $field['label'] ?></th>
                    <?php endforeach; ?>
                    <th class="th-actions">Ações</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($items as $item): ?>
                <tr>
                    <td class="td-id"><?= $item[$config['id_field']] ?? '—' ?></td>
                    <?php foreach ($config['fields'] as $field): ?>
                    <td>
                        <?php
                        $val = $item[$field['name']] ?? '—';
                        if ($field['type'] === 'number' && isset($field['step']) && $field['step'] === '0.01') {
                            echo 'R$ ' . number_format((float)$val, 2, ',', '.');
                        } else {
                            echo htmlspecialchars((string)$val);
                        }
                        ?>
                    </td>
                    <?php endforeach; ?>
                    <td class="td-actions">
                        <a href="?page=<?= $slug ?>&action=edit&id=<?= $item[$config['id_field']] ?>"
                           class="btn-icon btn-edit" title="Editar">
                            <i class="fas fa-pen"></i>
                        </a>
                        <button class="btn-icon btn-delete" title="Excluir"
                                onclick="confirmDelete('<?= $slug ?>', <?= $item[$config['id_field']] ?>)">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </div>
    <?php endif; ?>
</div>
