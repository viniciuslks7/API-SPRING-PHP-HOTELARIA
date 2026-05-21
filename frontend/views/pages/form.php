<?php
$config  = $result['config'];
$slug    = $result['slug'];
$mode    = $result['mode'];
$values  = $result['values'] ?? [];
$error   = $result['error'] ?? null;
$id      = $result['id'] ?? null;
$fkLists = $result['fkLists'] ?? [];

$isEdit     = $mode === 'edit';
$formAction = $isEdit
    ? "?page={$slug}&action=edit&id={$id}"
    : "?page={$slug}&action=create";

$hasUpload  = false;
foreach ($config['fields'] as $f) {
    if (!empty($f['image'])) { $hasUpload = true; break; }
}
$enctype = $hasUpload ? ' enctype="multipart/form-data"' : '';

if (!function_exists('fmtMoneyInput')) {
    function fmtMoneyInput($v): string {
        if ($v === null || $v === '') return '';
        return number_format((float)$v, 2, '.', '');
    }
}
?>

<div class="form-page">
    <div class="form-header">
        <a href="?page=<?= $slug ?>" class="btn btn-ghost">
            <i class="fas fa-arrow-left" aria-hidden="true"></i> Voltar
        </a>
        <h2><?= $isEdit ? 'Editar' : 'Novo' ?> — <?= htmlspecialchars($config['title']) ?></h2>
    </div>

    <?php if ($error): ?>
    <div class="alert alert-error" role="alert">
        <i class="fas fa-exclamation-triangle" aria-hidden="true"></i>
        <span><?= htmlspecialchars($error) ?></span>
    </div>
    <?php endif; ?>

    <form method="POST" action="<?= $formAction ?>" class="crud-form"<?= $enctype ?>>
        <div class="form-grid">
            <?php foreach ($config['fields'] as $field):
                $name     = $field['name'];
                $label    = $field['label'];
                $required = $field['required'] ?? false;
                $val      = $values[$name] ?? '';
                $hint     = $field['hint'] ?? null;
                $hintId   = $hint ? $name . '-hint' : null;
                $describedBy = $hintId ? ' aria-describedby="' . $hintId . '"' : '';
            ?>
            <div class="form-group">
                <label for="<?= htmlspecialchars($name) ?>">
                    <?= htmlspecialchars($label) ?>
                    <?php if ($required): ?><span class="required" aria-hidden="true">*</span><span class="visually-hidden"> (obrigatório)</span><?php endif; ?>
                </label>

                <?php if (!empty($field['fk']) && isset($fkLists[$name])):
                    $list = $fkLists[$name];
                ?>
                    <select id="<?= htmlspecialchars($name) ?>" name="<?= htmlspecialchars($name) ?>"
                            class="form-select" <?= $required ? 'required' : '' ?><?= $describedBy ?>>
                        <option value="">— Selecione —</option>
                        <?php foreach ($list['options'] as $opt):
                            $optId   = $opt[$list['id_field']] ?? null;
                            $optName = $opt[$list['display']] ?? $optId;
                        ?>
                        <option value="<?= htmlspecialchars((string)$optId) ?>"
                                <?= ((string)$val === (string)$optId) ? 'selected' : '' ?>>
                            <?= htmlspecialchars((string)$optName) ?> (#<?= htmlspecialchars((string)$optId) ?>)
                        </option>
                        <?php endforeach; ?>
                    </select>

                <?php elseif (!empty($field['money'])): ?>
                    <div class="money-wrapper">
                        <span class="money-prefix">R$</span>
                        <input
                            type="text"
                            inputmode="decimal"
                            id="<?= htmlspecialchars($name) ?>"
                            name="<?= htmlspecialchars($name) ?>"
                            value="<?= htmlspecialchars(fmtMoneyInput($val)) ?>"
                            <?= $required ? 'required' : '' ?>
                            placeholder="0,00"
                            data-money="1"
                            class="form-input"
                            <?= $describedBy ?>
                        >
                    </div>

                <?php elseif (!empty($field['image'])): ?>
                    <div class="image-upload" data-image-field="<?= htmlspecialchars($name) ?>" role="group" aria-label="Selecionar imagem">
                        <div class="preview" aria-live="polite">
                            <?php if (!empty($val)): ?>
                                <img src="<?= htmlspecialchars($val) ?>" alt="Pré-visualização da imagem selecionada">
                            <?php else: ?>
                                <span>Pré-visualização da imagem</span>
                            <?php endif; ?>
                        </div>
                        <div class="upload-tabs" role="tablist" aria-label="Modo de envio da imagem">
                            <button type="button" class="tab-upload active" data-mode="upload" role="tab" aria-selected="true">
                                <i class="fas fa-upload" aria-hidden="true"></i> Upload
                            </button>
                            <button type="button" class="tab-url" data-mode="url" role="tab" aria-selected="false">
                                <i class="fas fa-link" aria-hidden="true"></i> URL
                            </button>
                        </div>
                        <input type="file" name="<?= htmlspecialchars($name) ?>_file"
                               id="<?= htmlspecialchars($name) ?>_file"
                               accept="image/png,image/jpeg,image/webp,image/gif"
                               class="input-file"
                               aria-label="Arquivo de imagem"<?= $describedBy ?>>
                        <input type="text" name="<?= htmlspecialchars($name) ?>" id="<?= htmlspecialchars($name) ?>"
                               value="<?= htmlspecialchars((string)$val) ?>"
                               class="form-input input-url" placeholder="https://..." style="display:none"
                               aria-label="URL da imagem"<?= $describedBy ?>>
                    </div>

                <?php else: ?>
                    <input
                        type="<?= htmlspecialchars($field['type']) ?>"
                        id="<?= htmlspecialchars($name) ?>"
                        name="<?= htmlspecialchars($name) ?>"
                        value="<?= htmlspecialchars((string)$val) ?>"
                        <?= $required ? 'required' : '' ?>
                        <?= isset($field['min']) ? 'min="' . htmlspecialchars((string)$field['min']) . '"' : '' ?>
                        <?= isset($field['max']) ? 'max="' . htmlspecialchars((string)$field['max']) . '"' : '' ?>
                        <?= isset($field['step']) ? 'step="' . htmlspecialchars((string)$field['step']) . '"' : '' ?>
                        <?= isset($field['pattern']) ? 'pattern="' . htmlspecialchars($field['pattern']) . '"' : '' ?>
                        class="form-input"
                        placeholder="<?= htmlspecialchars($label) ?>"
                        <?= $describedBy ?>
                    >
                <?php endif; ?>

                <?php if ($hint): ?>
                <small class="field-hint" id="<?= htmlspecialchars($hintId) ?>"><?= htmlspecialchars($hint) ?></small>
                <?php endif; ?>
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
