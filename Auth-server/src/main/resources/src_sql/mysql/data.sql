insert IGNORE into roles (name, description)
values ('ROLE_USER', 'для авторизированных пользователей'),
       ('ROLE_ADMIN', 'для админов'),
       ('ROLE_SERVICE', 'для микросервисов');

insert IGNORE into scopes (name, description)
values ( 'read', null ),
       ('write', null),
       ('update', 'только для админов'),
       ('delete', 'только для админов');


insert IGNORE into users (username, password, email, enabled)
values ('Operator', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'penzmash@mail.ru', true),
       ('Andrey', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'andnot@yandex.ru', true); -- pass: q

-- Вставка связей пользователей и ролей
insert IGNORE into users_roles (user_id, role_id)
values
    (1, 1), -- Operator -> ROLE_USER
    (2, 1), -- Andrey -> ROLE_USER
    (2, 2); -- Andrey -> ROLE_ADMIN

-- Вставка связей пользователей и прав
insert IGNORE into users_scopes (user_id, scope_id)
values
    (1, 1), -- Operator -> read
    (1, 2), -- Operator -> write
    (2, 1), -- Andrey -> read
    (2, 2), -- Andrey -> write
    (2, 3), -- Andrey -> update
    (2, 4); -- Andrey -> delete




