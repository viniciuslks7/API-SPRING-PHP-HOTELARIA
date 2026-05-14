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

    /**
     * Despacha a ação com base no parâmetro 'action' da query string.
     * Retorna um array com os dados necessários para a view renderizar.
     */
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

    /**
     * Lista todos os registros.
     */
    private function list(): array
    {
        $result = $this->api->get($this->entityConfig['endpoint']);

        return [
            'view'   => 'list',
            'data'   => $result['data'] ?? [],
            'error'  => $result['error'] ?? null,
            'config' => $this->entityConfig,
            'slug'   => $this->entitySlug,
        ];
    }

    /**
     * Cria um novo registro (exibe form ou processa POST).
     */
    private function create(): array
    {
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data   = $this->extractFormData();
            $result = $this->api->post($this->entityConfig['endpoint'], $data);

            if ($result['success']) {
                header("Location: ?page={$this->entitySlug}&success=created");
                exit;
            }

            return [
                'view'   => 'form',
                'mode'   => 'create',
                'error'  => $result['error'] ?? 'Erro ao criar registro.',
                'values' => $data,
                'config' => $this->entityConfig,
                'slug'   => $this->entitySlug,
            ];
        }

        return [
            'view'   => 'form',
            'mode'   => 'create',
            'error'  => null,
            'values' => [],
            'config' => $this->entityConfig,
            'slug'   => $this->entitySlug,
        ];
    }

    /**
     * Edita um registro existente (exibe form preenchido ou processa POST).
     */
    private function edit(): array
    {
        $id = $_GET['id'] ?? null;
        if (!$id) {
            header("Location: ?page={$this->entitySlug}");
            exit;
        }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data   = $this->extractFormData();
            $result = $this->api->put($this->entityConfig['endpoint'] . '/' . $id, $data);

            if ($result['success']) {
                header("Location: ?page={$this->entitySlug}&success=updated");
                exit;
            }

            return [
                'view'   => 'form',
                'mode'   => 'edit',
                'id'     => $id,
                'error'  => $result['error'] ?? 'Erro ao atualizar registro.',
                'values' => $data,
                'config' => $this->entityConfig,
                'slug'   => $this->entitySlug,
            ];
        }

        $result = $this->api->get($this->entityConfig['endpoint'] . '/' . $id);

        if (!$result['success']) {
            header("Location: ?page={$this->entitySlug}&error=not_found");
            exit;
        }

        return [
            'view'   => 'form',
            'mode'   => 'edit',
            'id'     => $id,
            'error'  => null,
            'values' => $result['data'],
            'config' => $this->entityConfig,
            'slug'   => $this->entitySlug,
        ];
    }

    /**
     * Deleta um registro via API e redireciona.
     */
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
     * Extrai os dados do formulário HTML e converte tipos numéricos.
     */
    private function extractFormData(): array
    {
        $data = [];
        foreach ($this->entityConfig['fields'] as $field) {
            $value = $_POST[$field['name']] ?? null;

            if ($value !== null && $value !== '') {
                if ($field['type'] === 'number') {
                    $data[$field['name']] = str_contains($value, '.') ? (float) $value : (int) $value;
                } else {
                    $data[$field['name']] = $value;
                }
            }
        }
        return $data;
    }
}
