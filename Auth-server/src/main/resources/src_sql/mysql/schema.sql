-- Роли
create table if not exists roles (
                       id bigint not null auto_increment primary key,
                       name varchar(50) not null unique,
                       description varchar(255)
);

create table if not exists scopes (
                        id bigint not null auto_increment primary key,
                        name varchar(50) not null unique,
                        description varchar(255)
);

-- Пользователи
create table if not exists users (
                       id bigint not null auto_increment primary key,
                       username varchar(100) not null unique,
                       password varchar(255) not null,
                       email varchar(100),
                       enabled boolean not null
);

-- Таблица связей пользователей и ролей
create table if not exists users_roles (
                             role_id bigint not null,
                             user_id bigint not null,
                             primary key (role_id, user_id),
                             constraint fk_users_roles_role foreign key (role_id) references roles (id) on delete cascade on update cascade,
                             constraint fk_users_roles_user foreign key (user_id) references users (id) on delete cascade on update cascade
);

-- Таблица связей пользователей и прав
create table if not exists users_scopes (
                              scope_id bigint not null,
                              user_id bigint not null,
                              primary key (scope_id, user_id),
                              constraint fk_users_scopes_scope foreign key (scope_id) references scopes (id) on delete cascade on update cascade,
                              constraint fk_users_scopes_user foreign key (user_id) references users (id) on delete cascade on update cascade
);

-- Таблица для информации о клиентах (GUI и мобильное приложение)
create table if not exists oauth2_registered_client (
                                          id varchar(100) primary key,
                                          client_id varchar(100) not null,
                                          client_id_issued_at timestamp default CURRENT_TIMESTAMP,
                                          client_secret varchar(200) default null,
                                          client_secret_expires_at timestamp default null,
                                          client_name varchar(200) not null,
                                          client_authentication_methods varchar(1000) not null,
                                          authorization_grant_types varchar(1000) not null,
                                          redirect_uris varchar(1000) not null,
                                          post_logout_redirect_uris varchar(255) default null,
                                          scopes varchar(1000) not null,
                                          client_settings varchar(2000) not null,
                                          token_settings varchar(2000) not null
);
