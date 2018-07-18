PRAGMA foreign_keys = ON;

create table customers(
customerid integer primary key autoincrement,
name text not null,
address text,
address2 text,
nip text not null unique,
phone text);

create table invoices(
invoiceid integer primary key autoincrement,
number text not null unique,
customerid integer,
creationdate date not null,
selldate date not null,
paymentdate date not null,
payment text not null,
bankname text,
accountnr text,
currency text not null,
comments text,
foreign key(customerid) references customers(customerid));

create table products(
productid integer primary key autoincrement,
invoicenumber text,
name text not null,
count integer not null,
unit text,
netprice integer not null,
vatrate integer not null,
foreign key(invoicenumber) references invoices(number));

create table carevidence(
evidenceid integer primary key autoincrement,
source text not null,
destination text not null,
goal text not null,
date date not null,
distance integer not null);

CREATE TABLE configuration(
configurationid INTEGER PRIMARY KEY autoincrement,
paramname TEXT not null unique,
paramvalue TEXT not null);

CREATE TABLE costs(
costid INTEGER PRIMARY KEY autoincrement,
date DATE not null,
description TEXT not null,
netvalue INTEGER not null,
vatvalue INTEGER not null,

CREATE TABLE otherincomes(
otherincomeid integer primary key autoincrement,
number text not null unique,
description text not null,
date date not null,
value integer not null);

insert into customers values(null,'Google LLC','Mountain View','0009-4532 Mountain View','2345615432','77898908723');
insert into customers values(null,'Youtube','California 12','0098-7896 California','2345142634','6534534211');
insert into customers values(null,'Instagram LLC','Berlin 56','559-12 Berlin','15243265478','6152434352');

insert into invoices values(null,'7',3,'2014-12-12','2014-12-12','2014-12-12','CASH','','','PLN','');
insert into invoices values(null,'8',3,'2018-08-12','2018-08-12','2018-08-12','transfer','UBS','12342392013','USD','');
insert into invoices values(null,'9',2,'2018-07-31','2018-07-30','2018-07-31','cash','','','PLN','Payment done.');
insert into invoices values(null,'10',1,'2018-07-01','2018-07-01','2018-07-01','in nature','','','h','');

insert into products values(null,'7','algorytm',1,'',10099,23);
insert into products values(null,'7','code',1,'',100050,23);
insert into products values(null,'8','pictures',10,'',5000,23);
insert into products values(null,'9','Book of codemaster',123,'szt.',3599,23);
insert into products values(null,'9','Book of no codemaster',123,'szt.',3599,23);
insert into products values(null,'10','whatever',1,'kg',100010,50);

insert into carevidence values(null,'Krakow','Brzesko','57','2018-07-10',57);
insert into carevidence values(null,'Brzesko','Krakow','Buissnes visit','2018-07-10',57);

insert into configuration values(null, 'ownername', 'Dawid Wawryka Uslugi IT');
insert into configuration values(null, 'owneraddress', 'ul. Kosciuszki 72/10');
insert into configuration values(null, 'owneraddress2', '32-800 Brzesko');
insert into configuration values(null, 'ownernip', '8691900399');
insert into configuration values(null, 'ownerphone', '48696643434');
insert into configuration values(null, 'pagelimit', '10');