drop table if exists Card cascade;

create table if not exists Card
(
    id int auto_increment,
    cardNumber varchar(30),
    accountNumber varchar(30) not null,
    constraint CARD_PK
        primary key (id)
);