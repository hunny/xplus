-- DROP TABLE company;
CREATE TABLE company (
id bigint NOT NULL AUTO_INCREMENT, 
name varchar(254) NOT NULL, 
refname varchar(254), 
times bigint default 0,
UNIQUE KEY uk_1(name),
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- DROP TABLE contactor;
CREATE TABLE contactor (
id bigint NOT NULL AUTO_INCREMENT, 
company varchar(254) NOT NULL, 
type varchar(254), 
name varchar(254), 
caller varchar(254), 
title varchar(254), 
phone varchar(254), 
mobile varchar(254), 
email varchar(254), 
fax varchar(254), 
address varchar(254), 
remark varchar(1024), 
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

