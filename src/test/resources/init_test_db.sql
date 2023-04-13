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