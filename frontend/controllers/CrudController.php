<?php
/**
 * CrudController — Controller genérico que processa CRUD para QUALQUER entidade.
 * Recebe a configuração da entidade do config/api.php e executa a ação solicitada.
 */
class CrudController
{
    private ApiClient $api;
    private array $entityConfig;
    private string $entitySlug;

    public function __construct(ApiClient $api, string $entitySlug, array $entityConfig)
    {
        $this->api          = $api;
        $this->entitySlug   = $entitySlug;
        $this->entityConfig = $entityConfig;
    }

    public function handle(): array
    {
        $action = $_GET['action'] ?? 'list';

        return match ($action) {
            'list'   => $this->list(),
            'create' => $this->create(),
            'edit'   => $this->edit(),
            'delete' => $this->delete(),
            default  => $this->list(),
        };
    }

    private function list(): array
    {
        $result = $this->api->get($this->entityConfig['endpoint']);

        return [
            'view'       => 'list',
            'data'       => $result['data'] ?? [],
            'error'      => $result['error'] ?? null,
            'config'     => $this->entityConfig,
            'slug'       => $this->entitySlug,
            'fkMaps'     => $this->loadFkMaps(),
        ];
    }

    private function create(): array
    {
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = $this->extractFormData();
            $validationError = $this->validate($data);

            if ($validationError === null) {
                $result = $this->api->post($this->entityConfig['endpoint'], $data);
                if ($result['success']) {
                    header("Location: ?page={$this->entitySlug}&success=created");
                    exit;
                }
                $error = $result['error'] ?? 'Erro ao criar registro.';
            } else {
                $error = $validationError;
            }

            return [
                'view'    => 'form',
                'mode'    => 'create',
                'error'   => $error,
                'values'  => $data,
                'config'  => $this->entityConfig,
                'slug'    => $this->entitySlug,
                'fkLists' => $this->loadFkLists(),
            ];
        }

        return [
            'view'    => 'form',
            'mode'    => 'create',
            'error'   => null,
            'values'  => [],
            'config'  => $this->entityConfig,
            'slug'    => $this->entitySlug,
            'fkLists' => $this->loadFkLists(),
        ];
    }

    private function edit(): array
    {
        $id = $_GET['id'] ?? null;
        if (!$id) {
            header("Location: ?page={$this->entitySlug}");
            exit;
        }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = $this->extractFormData();
            $validationError = $this->validate($data);

            if ($validationError === null) {
                $result = $this->api->put($this->entityConfig['endpoint'] . '/' . $id, $data);
                if ($result['success']) {
                    header("Location: ?page={$this->entitySlug}&success=updated");
                    exit;
                }
                $error = $result['error'] ?? 'Erro ao atualizar registro.';
            } else {
                $error = $validationError;
            }

            return [
                'view'    => 'form',
                'mode'    => 'edit',
                'id'      => $id,
                'error'   => $error,
                'values'  => $data,
                'config'  => $this->entityConfig,
                'slug'    => $this->entitySlug,
                'fkLists' => $this->loadFkLists(),
            ];
        }

        $result = $this->api->get($this->entityConfig['endpoint'] . '/' . $id);

        if (!$result['success']) {
            header("Location: ?page={$this->entitySlug}&error=not_found");
            exit;
        }

        return [
            'view'    => 'form',
            'mode'    => 'edit',
            'id'      => $id,
            'error'   => null,
            'values'  => $result['data'],
            'config'  => $this->entityConfig,
            'slug'    => $this->entitySlug,
            'fkLists' => $this->loadFkLists(),
        ];
    }

    private function delete(): array
    {
        $id = $_GET['id'] ?? null;
        if ($id) {
            $result = $this->api->delete($this->entityConfig['endpoint'] . '/' . $id);
            if ($result['success']) {
                header("Location: ?page={$this->entitySlug}&success=deleted");
                exit;
            }
            header("Location: ?page={$this->entitySlug}&error=" . urlencode($result['error'] ?? 'Erro ao deletar'));
            exit;
        }
        header("Location: ?page={$this->entitySlug}");
        exit;
    }

    /**
     * Extrai dados do POST, processa upload de imagem e converte tipos.
     */
    private function extractFormData(): array
    {
        $data = [];
        foreach ($this->entityConfig['fields'] as $field) {
            $name  = $field['name'];
            $value = $_POST[$name] ?? null;

            // Upload de imagem sobrescreve o valor (URL) se um arquivo foi enviado.
            if (!empty($field['image']) && isset($_FILES[$name . '_file']) && $_FILES[$name . '_file']['error'] === UPLOAD_ERR_OK) {
                $uploaded = $this->handleImageUpload($_FILES[$name . '_file']);
                if ($uploaded !== null) {
                    $value = $uploaded;
                }
            }

            if ($value === null || $value === '') {
                continue;
            }

            if (!empty($field['money'])) {
                $data[$name] = $this->parseMoney($value);
            } elseif ($field['type'] === 'number') {
                $data[$name] = str_contains((string)$value, '.') ? (float)$value : (int)$value;
            } else {
                $data[$name] = $value;
            }
        }
        return $data;
    }

    /**
     * Validação server-side: regex (pattern) e formato monetário.
     * Retorna null se tudo OK, ou mensagem de erro.
     */
    private function validate(array $data): ?string
    {
        foreach ($this->entityConfig['fields'] as $field) {
            $name  = $field['name'];
            $value = $data[$name] ?? null;

            if (($field['required'] ?? false) && ($value === null || $value === '')) {
                return "Campo \"{$field['label']}\" é obrigatório.";
            }
            if ($value === null || $value === '') {
                continue;
            }

            if (!empty($field['money'])) {
                if (!is_numeric($value) || (float)$value < 0) {
                    return "\"{$field['label']}\" deve ser um valor monetário válido (≥ 0).";
                }
                continue;
            }

            if (!empty($field['pattern']) && !preg_match('/' . $field['pattern'] . '/u', (string)$value)) {
                return "\"{$field['label']}\" está em formato inválido.";
            }

            if (isset($field['min']) && is_numeric($value) && $value < $field['min']) {
                return "\"{$field['label']}\" deve ser ≥ {$field['min']}.";
            }
            if (isset($field['max']) && is_numeric($value) && $value > $field['max']) {
                return "\"{$field['label']}\" deve ser ≤ {$field['max']}.";
            }
        }
        return null;
    }

    /**
     * Converte "1.234,56" / "1234.56" / "1,234.56" para float.
     */
    private function parseMoney(string $value): float
    {
        $value = trim($value);
        if (str_contains($value, ',') && str_contains($value, '.')) {
            $value = str_replace('.', '', $value);
            $value = str_replace(',', '.', $value);
        } elseif (str_contains($value, ',')) {
            $value = str_replace(',', '.', $value);
        }
        return (float)$value;
    }

    /**
     * Salva o arquivo enviado e retorna a URL relativa servida pelo PHP built-in server.
     */
    private function handleImageUpload(array $file): ?string
    {
        $allowed = ['image/jpeg' => 'jpg', 'image/png' => 'png', 'image/webp' => 'webp', 'image/gif' => 'gif'];
        $mime = mime_content_type($file['tmp_name']);
        if (!isset($allowed[$mime])) {
            return null;
        }

        if (!is_dir(UPLOAD_DIR)) {
            mkdir(UPLOAD_DIR, 0775, true);
        }

        $ext  = $allowed[$mime];
        $name = bin2hex(random_bytes(8)) . '.' . $ext;
        $dest = UPLOAD_DIR . DIRECTORY_SEPARATOR . $name;

        if (!move_uploaded_file($file['tmp_name'], $dest)) {
            return null;
        }

        return UPLOAD_URL_PREFIX . $name;
    }

    /**
     * Para cada campo com `fk`, busca a lista do recurso relacionado.
     * Usado no form (popular o <select>).
     */
    private function loadFkLists(): array
    {
        global $ENTITIES;
        $lists = [];

        foreach ($this->entityConfig['fields'] as $field) {
            if (empty($field['fk'])) continue;

            $targetSlug = $field['fk']['entity'];
            $targetCfg  = $ENTITIES[$targetSlug] ?? null;
            if (!$targetCfg) continue;

            $response = $this->api->get($targetCfg['endpoint']);
            $lists[$field['name']] = [
                'options'  => is_array($response['data']) ? $response['data'] : [],
                'id_field' => $targetCfg['id_field'],
                'display'  => $field['fk']['display'],
            ];
        }
        return $lists;
    }

    /**
     * Para cada FK, monta dicionário id => display para uso na listagem.
     */
    private function loadFkMaps(): array
    {
        global $ENTITIES;
        $maps = [];

        foreach ($this->entityConfig['fields'] as $field) {
            if (empty($field['fk'])) continue;

            $targetSlug = $field['fk']['entity'];
            $targetCfg  = $ENTITIES[$targetSlug] ?? null;
            if (!$targetCfg) continue;

            $response = $this->api->get($targetCfg['endpoint']);
            $map = [];
            if (is_array($response['data'])) {
                foreach ($response['data'] as $row) {
                    $id = $row[$targetCfg['id_field']] ?? null;
                    if ($id !== null) {
                        $map[$id] = $row[$field['fk']['display']] ?? $id;
                    }
                }
            }
            $maps[$field['name']] = $map;
        }
        return $maps;
    }
}
