create table users(id serial primary key, username varchar,password varchar);
create table roles(id serial primary key,name varchar);
create table user_role(user_id int references users(id),role_id int references roles(id));