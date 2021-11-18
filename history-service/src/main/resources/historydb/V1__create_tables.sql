create database history_db;
create table history(id serial primary key,base_currency varchar,base_quantity decimal, target_currency varchar,converted_quantity decimal,date date);
