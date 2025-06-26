## Метрики.

### Общее.

Все метрики, кроме `health` и `info` доступны только с ролью `ADMIN`,
поэтому вызываем их примерно так:
```shell
curl -H "Authorization: Bearer $токен$" http://localhost:9010/CATALOG-SERVICE/actuator/metrics
```
То есть через `Gateway` и с действующим Jwt-токеном админа (о нем ниже).

В ответ получим json-список эндпоинтов доступных метрик.


### Создаем контейнер Prometheus.

Для удобного мониторинга подключим `Прометей`, через который можно задействовать удобные средства просмотра.

В папке `tools/prometheus/osc-prometheus-containers`:

Сначала в `.env.prometheus` задаем адрес сервера авторизации. И затем собираем контейнер:

```shell
docker-compose up -d --build
```
При запуске контейнера `entrypoint.sh` автоматически получает Jwt-токен от OAuth2-сервера, 
который и будет использовать `Prometheus`. Токен годен 60 минут, после чего контейнер
придется перезапустить.

Или поменять скрипт, сделав получение Jwt-токена через какие-то интервалы времени, например
с помощью `cron`.


### Просмотр метрик.

По адресу http://localhost:9090 будет доступен интерфейс Прометея. Альтернативно его доступность
можно проверить так:
```shell
curl -H "токен" http://localhost:9010/CATALOG-SERVICE/actuator/prometheus/
```
Токен можно получить через скрипт `get_token_client_credentials.http` или `get_token_client_credentials.bat`.

Скрипт `update-token.ps1` также получает токен, но сохраняет его в файл.

Но для удобного просмотра лучше подключить `Grafana`.


### Grafana.

Заходим в UI по адресу http://localhost:3000 (login: admin, password: admin).

Добавляем Prometheus как источник данных:
```text
    Configuration → Data sources → Add data source → Prometheus
    URL: http://prometheus:9090
```
Поскольку они в одном контейнере, то к Прометею нужно обращаться через имя сервиса (prometheus).

### Полезные метрики.
Полезн
- jvm_memory_used_bytes{area="heap", job=~".*-metrics"} - `Используемая куча.`
- jvm_memory_max_bytes{area="heap", job=~".*-metrics"} - `Максимально доступная куча.`
- jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} * 100 - `Процент использования памяти.`
- jvm_memory_used_bytes{area="nonheap", id="Metaspace"} - `Использование Metaspace.`
- jvm_gc_pause_seconds_count - `Количество пауз Garbage Collection (сборок мусора).`
- rate(jvm_gc_pause_seconds_sum[1m]) - `Время последней сборки мусора.`

Прим.: Add query (и справа переключатель "Code").