DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  username VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);

insert into USERS(NAME, USERNAME, EMAIL) values('Gokula Sakthi', 'Sakthi', 'sgsakthi1992@gmail.com');

insert into USERS(NAME, USERNAME, EMAIL) values('Mallikarjun', 'Malli', 'mallikarjun.bandi@gmail.com');
