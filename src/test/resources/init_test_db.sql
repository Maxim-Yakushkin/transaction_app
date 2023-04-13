-- DDL
drop table if exists user;
drop table if exists account;
drop table if exists transactions;

CREATE TABLE user
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT(50) not null ,
    address TEXT(255)
);

CREATE TABLE account
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id  INTEGER(10) REFERENCES user (id),
    balance  INTEGER(15),
    currency TEXT(10),
    UNIQUE (user_id, currency)
);

CREATE TABLE transactions
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER(10) REFERENCES account (id),
    amount     INTEGER(15)
);

-- DML
-- INSERT INTO user (name, address)
-- VALUES ('Petya', 'Mira 12, 97'),
--        ('Vasya', 'Tesla 1, 5'),
--        ('Valera', 'Pobedy 2, 32'),
--        ('Sveta', 'Lubimova 66, 11'),
--        ('Ira', 'Narodov 50, 99'),
--        ('Alla', 'Oktabrskaya 24, 156');
--
-- INSERT INTO account (user_id, balance, currency)
-- VALUES (1, 100, 'BYN'),
--        (2, 200, 'EUR'),
--        (3, 300, 'USD'),
--        (3, 330, 'RUB'),
--        (3, 333, 'BYN'),
--        (4, 400, 'BYN'),
--        (5, 500, 'RUB'),
--        (5, 550, 'BYN'),
--        (6, 600, 'BYN'),
--        (6, 660, 'USD');
--
-- INSERT INTO transactions (account_id, amount)
-- values (1, 200),
--        (2, 100),
--        (3, 500),
--        (6, 1000),
--        (7, 250),
--        (9, 700);