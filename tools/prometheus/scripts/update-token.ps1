# Скрипт PowerShell для получения Jwt-токена и сохранения его в файл.
# Токен нужен Prometheus, для доступа к метрикам микросервисов.
#
# Проверить можно запросом, эмулирующим Prometheus:
# curl -H "Authorization: Bearer $(type C:\Prometheus\token.jwt)" http://localhost:9010/CATALOG-SERVICE/actuator/prometheus
#
$clientId = "prometheus"
$clientSecret = "secret"
$scope = "actuator:read"
$authServerUrl = "http://localhost:8090/oauth2/token"

$response = Invoke-RestMethod -Uri $authServerUrl -Method Post -Headers @{
    Authorization = ("Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("$clientId`:$clientSecret")))
} -Body @{
    grant_type = "client_credentials"
    scope = $scope
}

# $response.access_token | Out-File -Encoding ASCII -NoNewline "C:\Prometheus\token.jwt"
$response.access_token | Out-File -Encoding ASCII -NoNewline "token.jwt"