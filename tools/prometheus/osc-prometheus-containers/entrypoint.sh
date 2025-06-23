#!/bin/bash
set -e

AUTH_SERVER_URL=${AUTH_SERVER_URL:-http://host.docker.internal:8090}

# Если host.docker.internal не работает — fallback на localhost
if ! curl -s "$AUTH_SERVER_URL/oauth2/token" > /dev/null; then
  AUTH_SERVER_URL=http://localhost:8090
fi

echo "[Prometheus] Запрашиваем токен авторизации с $AUTH_SERVER_URL..."

TOKEN=$(curl -s -u prometheus:secret \
  -d "grant_type=client_credentials&scope=actuator:read" \
  "$AUTH_SERVER_URL/oauth2/token" | jq -r .access_token)

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
