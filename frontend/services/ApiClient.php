<?php
/**
 * ApiClient — Serviço centralizado de comunicação com a API REST.
 * Encapsula todas as chamadas cURL em métodos semânticos (get, post, put, delete).
 */
class ApiClient
{
    private string $baseUrl;

    public function __construct()
    {
        $this->baseUrl = API_BASE_URL;
    }

    /**
     * GET — Lista todos os registros ou busca por ID.
     */
    public function get(string $endpoint): array
    {
        return $this->request('GET', $endpoint);
    }

    /**
     * POST — Cria um novo registro.
     */
    public function post(string $endpoint, array $data): array
    {
        return $this->request('POST', $endpoint, $data);
    }

    /**
     * PUT — Atualiza um registro existente.
     */
    public function put(string $endpoint, array $data): array
    {
        return $this->request('PUT', $endpoint, $data);
    }

    /**
     * DELETE — Remove um registro.
     */
    public function delete(string $endpoint): array
    {
        return $this->request('DELETE', $endpoint);
    }

    /**
     * Motor de requisições cURL.
     * Retorna ['success' => bool, 'data' => mixed, 'status' => int, 'error' => string|null]
     */
    private function request(string $method, string $endpoint, ?array $data = null): array
    {
        $url = $this->baseUrl . $endpoint;

        $ch = curl_init();

        curl_setopt_array($ch, [
            CURLOPT_URL            => $url,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_TIMEOUT        => 15,
            CURLOPT_CUSTOMREQUEST  => $method,
            CURLOPT_HTTPHEADER     => [
                'Content-Type: application/json',
                'Accept: application/json',
            ],
        ]);

        if ($data !== null && in_array($method, ['POST', 'PUT'])) {
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        }

        $response   = curl_exec($ch);
        $statusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError  = curl_error($ch);

        curl_close($ch);

        if ($curlError) {
            return [
                'success' => false,
                'data'    => null,
                'status'  => 0,
                'error'   => "Erro de conexão: $curlError. Verifique se a API Spring Boot está rodando.",
            ];
        }

        $decoded = json_decode($response, true);

        $isSuccess = $statusCode >= 200 && $statusCode < 300;

        $errorMessage = null;
        if (!$isSuccess && $decoded) {
            if (isset($decoded['errors'])) {
                $errorMessage = implode(' | ', array_map(
                    fn($k, $v) => "$k: $v",
                    array_keys($decoded['errors']),
                    array_values($decoded['errors'])
                ));
            } elseif (isset($decoded['message'])) {
                $errorMessage = $decoded['message'];
            } elseif (isset($decoded['error'])) {
                $errorMessage = $decoded['error'];
            }
        }

        return [
            'success' => $isSuccess,
            'data'    => $decoded,
            'status'  => $statusCode,
            'error'   => $errorMessage,
        ];
    }
}
