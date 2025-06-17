http://127.0.0.1:9000/pk8000/catalog/index

{"timestamp":"2025-06-17T17:16:38.7121468","status":400,"error":"Bad Request","message":"Пилот [требуется: 29, в наличии: 8]","path":"/pk8000/api/catalog/reservation/reserve"}


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