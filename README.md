## Магазин игрушек.

Простой pet-проект, выполненный на микросервисной архитектуре.

- Auth-server - OAuth2-сервер аутентификации и авторизации.
- Eureka-server - устаревший, но пока еще используемый service discovery, для обнаружения и регистрации микросервисов.
- Gateway-server - шлюз для доступа к микросервисам из веб-клиента.
- Service-Cart - микросервис корзины пользователя.
- Service-Catalog - микросервис товаров.
- Service-Payment - микросервис для проведения платежей.
- Service-Order - микросервис для оформления заказов.
- Web-client - микросервис веб-клиента.
- OSC-Commons - модуль с общими классами dto.

Взаимодействие между микросервисами происходит через Feign. Также используется RabbitMQ
и WebSocket для уведомлений пользователей (хотя в данном случае эта нужда притянута за уши).




http://127.0.0.1:9000/pk8000/catalog/index

https://myaccount.google.com/apppasswords


Header:
{
"kid": "3917c7b1-0451-4fb6-ae94-f9bd540b7805",
"alg": "RS256"
}
Body:
{
"sub": "Andrey",
"iss": "http://localhost:8090",
"nonce": "uxFEAn1M_J47A8tEADYxpJdjJjlBKEwWSE0CahS87wE",
"authorities": [
"ROLE_USER",
"ROLE_ADMIN"
],
"sid": "_MNmXmBbH-L22FQ6LBGMzaBMZI7ANUKfBwBWeyv-IOQ",
"aud": "client",
"user_id": "fc7559f3-7b92-4cc3-8051-2267b77fb6e5",
"azp": "client",
"auth_time": 1749974719,
"scope": [
"openid",
"read",
"write",
"update",
"delete"
],
"exp": 1749976520,
"iat": 1749974720,
"jti": "34ff487d-493c-4986-b680-f525b6c0bb28",
"email": "andnot@yandex.ru"
}