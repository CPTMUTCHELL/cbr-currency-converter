create table cbr(id_pk serial primary key ,id varchar, num_code int,char_code varchar,nominal int, name varchar,value decimal,date date);
create table history(id serial primary key,base_currency varchar,base_quantity decimal, target_currency varchar,converted_quantity decimal,date date);
create table users(id serial primary key, username varchar,password varchar);
create table roles(id serial primary key,name varchar);
create table user_role(user_id int references users(id),role_id int references roles(id));