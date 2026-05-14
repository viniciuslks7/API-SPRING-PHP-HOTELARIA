<?php
$config = $result['config'];
$slug   = $result['slug'];
$mode   = $result['mode'];
$values = $result['values'] ?? [];
$error  = $result['error'] ?? null;
$id     = $result['id'] ?? null;

$isEdit     = $mode === 'edit';
$formAction = $isEdit
    ? "?page={$slug}&action=edit&id={$id}"
    : "?page={$slug}&action=create";
?>

<div class="form-page">
    <div class="form-header">
        <a href="?page=<?= $slug ?>" class="btn btn-ghost">
            <i class="fas fa-arrow-left"></i> Voltar
        </a>
        <h2><?= $isEdit ? 'Editar' : 'Novo' ?> — <?= $config['title'] ?></h2>
    </div>

    <?php if ($error): ?>
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i>
        <span><?= htmlspecialchars($error) ?></span>
    </div>
    <?php endif; ?>

    <form method="POST" action="<?= $formAction ?>" class="crud-form">
        <div class="form-grid">
            <?php foreach ($config['fields'] as $field): ?>
            <div class="form-group">
                <label for="<?= $field['name'] ?>">
                    <?= $field['label'] ?>
                    <?php if ($field['required'] ?? false): ?>
                    <span class="required">*</span>
                    <?php endif; ?>
                </label>
                <input
                    type="<?= $field['type'] ?>"
                    id="<?= $field['name'] ?>"
                    name="<?= $field['name'] ?>"
                    value="<?= htmlspecialchars($values[$field['name']] ?? '') ?>"
                    <?= ($field['required'] ?? false) ? 'required' : '' ?>
                    <?= isset($field['min']) ? 'min="' . $field['min'] . '"' : '' ?>
                    <?= isset($field['max']) ? 'max="' . $field['max'] . '"' : '' ?>
                    <?= isset($field['step']) ? 'step="' . $field['step'] . '"' : '' ?>
                    class="form-input"
                    placeholder="<?= $field['label'] ?>"
                >
            </div>
            <?php endforeach; ?>
        </div>

        <div class="form-actions">
            <a href="?page=<?= $slug ?>" class="btn btn-ghost">Cancelar</a>
            <button type="submit" class="btn btn-primary">
                <i class="fas <?= $isEdit ? 'fa-save' : 'fa-plus' ?>"></i>
                <?= $isEdit ? 'Salvar Alterações' : 'Criar Registro' ?>
            </button>
        </div>
    </form>
</div>
