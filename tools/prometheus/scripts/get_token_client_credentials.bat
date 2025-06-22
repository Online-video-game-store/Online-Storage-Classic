rem Получение Jwt-Токена (ROLE_SERVICE, SCOPE_actuator:read)
curl -u prometheus:secret -d "grant_type=client_credentials&scope=actuator:read" http://localhost:8090/oauth2/token
pause
