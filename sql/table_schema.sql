CREATE DATABASE IF NOT EXISTS bank;

USE bank;

CREATE TABLE Users(
	id int auto_increment primary key, -- autofill id column
    accountNumber char(10),
    fullName      varchar(100),
    passwordHash  varchar(100),
    balance       decimal(10, 2),
    gender        varchar(20)
);