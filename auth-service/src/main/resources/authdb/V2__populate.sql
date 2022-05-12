insert into roles(name)values ('OWNER');
insert into roles(name)values ('ADMIN');
insert into roles(name)values ('USER');
insert into roles(name)values ('BANNED');
-- admin:admin with owner role
insert into users(username,password)values ('admin','$2a$10$G9aJb5CUFyjytVNMasmx5eYqycOgHIhwkBuXebS7UHLa8B9bnuDU.');
insert into user_role(user_id,role_id)values (1,1);