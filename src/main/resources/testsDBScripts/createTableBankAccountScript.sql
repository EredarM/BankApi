drop table if exists BankAccount cascade;

create table if not exists BankAccount
(
    id int auto_increment,
    clientId int not null,
    accountNumber varchar(30) not null,
    amount long default 0 not null,
    constraint BANKACCOUNT_PK
        primary key (id)
);