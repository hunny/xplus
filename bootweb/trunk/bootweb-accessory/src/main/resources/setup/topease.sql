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

-- DROP TABLE freess;
CREATE TABLE freess (
id bigint NOT NULL AUTO_INCREMENT, 
ip varchar(16) NOT NULL, 
port varchar(10) NOT NULL, 
method varchar(100) NOT NULL, 
password varchar(250) NOT NULL, 
vtm varchar(10) NOT NULL, -- V表示Vultr US线路，T表示China Telecom线路，M表示China Mobile线路。
country varchar(10) NOT NULL,
dateCreated datetime NOT NULL,
lastUpdated datetime NOT NULL,
INDEX index_1(ip),
UNIQUE KEY uk_1(ip, port),
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- DROP TABLE video;
CREATE TABLE video (
  uuid varchar(38) NOT NULL, 
  url varchar(254) NOT NULL, 
  downloaded varchar(100), 
  type varchar(250), 
  dateCreated datetime NOT NULL,
  lastUpdated datetime NOT NULL,
  INDEX index_1(downloaded),
  UNIQUE KEY uk_1(url),
  PRIMARY KEY (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

