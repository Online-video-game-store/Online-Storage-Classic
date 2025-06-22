#!/bin/bash
set -e

echo "[Prometheus] Запрашиваем токен авторизации..."

TOKEN=$(curl -s -u prometheus:secret \
  -d "grant_type=client_credentials&scope=actuator:read" \
  http://192.168.1.64:8090/oauth2/token | jq -r .access_token)

if [ -z "$TOKEN" ]; then
  echo "[ERROR] Токен не получен!"
  exit 1
fi

echo "$TOKEN" > /etc/prometheus/token.jwt

echo "[Prometheus] Токен получен. Запускаем Prometheus..."

exec ./prometheus \
  --config.file=/etc/prometheus/prometheus.yml \
  --storage.tsdb.path=/prometheus \
  --web.console.libraries=console_libraries \
  --web.console.templates=consoles
