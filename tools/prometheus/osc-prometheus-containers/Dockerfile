FROM debian:bookworm-slim

# Установка curl, jq и утилит
RUN apt-get update && apt-get install -y curl jq tar gzip && rm -rf /var/lib/apt/lists/*

# Переменная версии Prometheus
ENV PROM_VERSION=2.52.0

# Загрузка и установка Prometheus
RUN curl -L https://github.com/prometheus/prometheus/releases/download/v${PROM_VERSION}/prometheus-${PROM_VERSION}.linux-amd64.tar.gz \
    | tar -xz -C /opt && mv /opt/prometheus-${PROM_VERSION}.linux-amd64 /opt/prometheus

# Копируем конфиг и скрипт
COPY prometheus.yml /etc/prometheus/prometheus.yml
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

WORKDIR /opt/prometheus
ENTRYPOINT ["/entrypoint.sh"]