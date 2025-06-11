insert into roles (name, description)
values ('ROLE_USER', 'для авторизированных пользователей'),
       ('ROLE_ADMIN', 'для админов'),
       ('ROLE_SERVICE', 'для микросервисов');

insert into scopes (name, description)
values ( 'read', null ),
       ('write', null),
       ('update', 'только для админов'),
       ('delete', 'только для админов');


insert into users (username, password, email, enabled)
values ('Operator', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'penzmash@mail.ru', true),
       ('Andrey', '$2a$12$cADt/zxBaBR4zA3uv9MBvOBbgj3g8hvHCAwCd3QKLN9JC46Ia6GL2', 'andnot@yandex.ru', true); -- pass: q

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 1), (2, 2);

insert into users_scopes (user_id, scope_id)
values (1, 1), (1, 2),
       (2, 1), (2, 2), (2, 3), (2, 4);
