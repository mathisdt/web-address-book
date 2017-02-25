CREATE TABLE IF NOT EXISTS family (
id int(11) NOT NULL,
  last_name varchar(255) DEFAULT NULL,
  street varchar(255) DEFAULT NULL,
  postal_code varchar(255) DEFAULT NULL,
  city varchar(255) DEFAULT NULL,
  contact1 varchar(255) DEFAULT NULL,
  contact2 varchar(255) DEFAULT NULL,
  contact3 varchar(255) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  last_update datetime DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=213 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS person (
id int(11) NOT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  birthday varchar(255) DEFAULT NULL,
  contact1 varchar(255) DEFAULT NULL,
  contact2 varchar(255) DEFAULT NULL,
  contact3 varchar(255) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  family_id int(11) DEFAULT NULL,
  last_update datetime DEFAULT NULL,
  ordering int(11) DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=325 DEFAULT CHARSET=utf8;


ALTER TABLE family ADD PRIMARY KEY (id);

ALTER TABLE person ADD PRIMARY KEY (id);
ALTER TABLE person ADD FOREIGN KEY (family_id) REFERENCES FAMILY;
