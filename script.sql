create table Customer
(
    id      varchar(100) not null
        primary key,
    name    varchar(100) not null,
    address varchar(100) not null
);

create table Item
(
    code        varchar(10)   not null
        primary key,
    description varchar(30)   null,
    unitPrice   decimal(6, 2) null,
    qtyOnHand   int           null
);

create table `Order`
(
    id         varchar(10) not null
        primary key,
    date       date        null,
    customerId varchar(10) null,
    constraint Order_ibfk_1
        foreign key (customerId) references Customer (id)
);

create index customerId
    on `Order` (customerId);

create table OrderDetail
(
    orderId   varchar(10)   not null,
    itemCode  varchar(10)   not null,
    qty       int           null,
    unitPrice decimal(6, 2) null,
    primary key (orderId, itemCode),
    constraint OrderDetail_ibfk_1
        foreign key (orderId) references `Order` (id),
    constraint OrderDetail_ibfk_2
        foreign key (itemCode) references Item (code)
);

create index itemCode
    on OrderDetail (itemCode);


