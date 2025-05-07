create table FUNDRAISING_EVENTS(
    id int auto_increment primary key,
    name varchar(255) not null,
    currency varchar(3) not null,
    money decimal(10,2) not null default 0.00
);

create table COLLECTION_BOXES(
    id int auto_increment primary key,
    event_id int not null,
    money decimal(10,2) not null default 0.00,
    foreign key (event_id) references FUNDRAISING_EVENTS(id)
)