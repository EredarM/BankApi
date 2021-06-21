drop table if exists counterparty cascade;

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