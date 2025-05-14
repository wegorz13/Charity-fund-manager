create table CURRENCIES(
    id int auto_increment primary key,
    code varchar(3) not null
);

create table FUNDRAISING_EVENTS(
    id int auto_increment primary key,
    name varchar(255) not null,
    money decimal(10,2) not null default 0.00,
    currency_id int not null,
    foreign key (currency_id) references CURRENCIES(id)
);

create table COLLECTION_BOXES(
    id int auto_increment primary key,
    event_id int,
    foreign key (event_id) references FUNDRAISING_EVENTS(id)
);

create table COLLECTION_BOX_BALANCES(
    box_id      int not null,
    currency_id int not null,
    amount      decimal(10,2) not null default 0.00,
    primary key (box_id, currency_id),
    foreign key (box_id) references COLLECTION_BOXES(id) on delete cascade,
    foreign key (currency_id) references CURRENCIES(id)
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
