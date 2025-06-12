-- drop table if exists products;
-- drop table if exists blocked_products;

create table if not exists categories (
    id bigint auto_increment,
    name varchar(128) not null,
    description varchar(512),
    primary key (id)
);

create table if not exists products (
    id bigint generated always as identity,
    name varchar(255) not null,
    price decimal(10,2) not null,
    stock int not null,
    category_id bigint not null,
    description varchar(2048),
    primary key (id),
    foreign key (category_id) references categories (id) on delete restrict
);

create table if not exists product_images (
    product_id bigint not null,
    image_file varchar(255) not null,
    foreign key (product_id) references products (id) on delete cascade
);

create table if not exists blocked_products (
    id bigint generated always as identity,
    order_id VARCHAR(36),
    product_id bigint not null,
    quantity int not null,
    primary key (id)
);

