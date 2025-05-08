create table FUNDRAISING_EVENTS(
    id int auto_increment primary key,
    name varchar(255) not null,
    money decimal(10,2) not null default 0.00,
    currency_id int not null,
    foreign key (currency_id) references CURRENCIES(id)
);

create table COLLECTION_BOXES(
    id int auto_increment primary key,
    money decimal(10,2) not null default 0.00,
    event_id int not null,
    currency_id int not null,
    foreign key (event_id) references FUNDRAISING_EVENTS(id),
    foreign key (currency_id) references CURRENCIES(id)
);

create table CURRENCIES(
    id int auto_increment primary key,
    name varchar(3) not null
);

create table EXCHANGE_RATES (
    id int auto_increment primary key,
    from_currency_id int not null,
    to_currency_id int not null,
    rate decimal(10,6) not null,
    foreign key (from_currency_id) references CURRENCIES(id),
    foreign key (to_currency_id) references CURRENCIES(id),
    constraint unique_exchange unique (from_currency_id, to_currency_id)
);
