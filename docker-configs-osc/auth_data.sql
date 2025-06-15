USE user_auth_db;
SET NAMES utf8mb4;


insert IGNORE into roles (name, description)
values ('ROLE_USER', 'для авторизированных пользователей'),
       ('ROLE_ADMIN', 'для админов'),
       ('ROLE_SERVICE', 'для микросервисов');

insert IGNORE into scopes (name, description)
values ( 'read', null ),
       ('write', null),
       ('update', 'только для админов'),
       ('delete', 'только для админов');


insert IGNORE into users (user_id, username, password, email, enabled)
values (UNHEX('f4a89cf05a704f558e6915f7ae8547d3'), 'Operator', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'penzmash@mail.ru', true),
       (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 'Andrey', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'andnot@yandex.ru', true); -- pass: q

-- Вставка связей пользователей и ролей
insert IGNORE into users_roles (user_id, role_id)
values
    (UNHEX('f4a89cf05a704f558e6915f7ae8547d3'), 1), -- Operator -> ROLE_USER
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 1), -- Andrey -> ROLE_USER
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 2); -- Andrey -> ROLE_ADMIN


-- Вставка связей пользователей и прав
insert IGNORE into users_scopes (user_id, scope_id)
values
    (UNHEX('f4a89cf05a704f558e6915f7ae8547d3'), 1), -- Operator -> read
    (UNHEX('f4a89cf05a704f558e6915f7ae8547d3'), 2), -- Operator -> write
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 1), -- Andrey -> read
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 2), -- Andrey -> write
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 3), -- Andrey -> update
    (UNHEX('fc7559f37b924cc380512267b77fb6e5'), 4); -- Andrey -> delete
