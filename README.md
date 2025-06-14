TODO: Заменить ID пользователей на UUID


Header:
{
"kid": "7168b03c-9b1a-41e1-87a0-c46a799d797c",
"alg": "RS256"
}
Body:
{
"sub": "Andrey",
"iss": "http://localhost:8090",
"nonce": "fBbX_DVx2-2z86BvpF2z58yjTUtZamybL_NKhio6iME",
"authorities": [
    "ROLE_USER",
    "ROLE_ADMIN"
],
"sid": "1owTZXG9gzx6_UwaVlEGr9s1nwtKLOvhGBUGCZGhPEI",
"aud": "client",
"user_id": "2",
"azp": "client",
"auth_time": 1749934891,
"scope": [
    "openid",
    "read",
    "write",
    "update",
    "delete"
],
"exp": 1749936691,
"iat": 1749934891,
"jti": "7d50bf03-618f-4cb4-8d45-30802d3229ae",
"email": "andnot@yandex.ru"
}
