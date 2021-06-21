drop schema if exists BANK cascade;

create schema if not exists BANK;
set schema BANK;

-- создание клиента
create table if not exists client
(
    id int auto_increment,
    name varchar(20) not null,
    constraint CLIENT_PK
        primary key (id)
);


-- создание счёта
create table if not exists BankAccount
(
    id int auto_increment,
    clientId int not null,
    accountNumber varchar(30) not null,
    amount long default 0 not null,
    foreign key (clientId) references client (id) on delete cascade,
    constraint BANKACCOUNT_PK
        primary key (id)
);

create unique index BANKACCOUNT_ACCOUNTNUMBER_UINDEX
    on BankAccount (accountNumber);

create unique index BANKACCOUNT_CLIENTID_UINDEX
    on BankAccount (clientId);



-- создание карты
create table if not exists Card
(
    id int auto_increment,
    cardNumber varchar(30),
    accountNumber varchar(30) not null,
    foreign key (accountNumber) references BankAccount (accountNumber) on delete cascade,
    constraint CARD_PK
        primary key (id)
);

create unique index CARD_CARDNUMBER_UINDEX
    on Card (cardNumber);


insert into client (name) values ('Максим');
insert into client (name) values ('Степан');
insert into BankAccount (clientId, accountNumber, amount) values (1, '1234', 1111);
insert into BankAccount (clientId, accountNumber, amount) values (2, '5678', 2222);
insert into Card (cardNumber, accountNumber) values ('32323232', '1234');
insert into Card (cardNumber, accountNumber) values ('87667678', '5678');


-- добавление контрагентов
create table if not exists counterparty (
    id int auto_increment,
    name varchar(30) not null,
    accountnumber varchar(30),
    amount long default 0 not null,
    constraint COUNTERPARTY_PK
        primary key (id)
);

create unique index COUNTERPARTY_NAME_UINDEX
    on counterparty (name);

insert into counterparty(name, accountnumber, amount) values ('first', '33333', 50000);
insert into counterparty(name, accountnumber, amount) values ('second', '55555', 80000);




