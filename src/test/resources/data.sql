DROP TABLE IF EXISTS EMPLOYEE;

CREATE TABLE EMPLOYEE (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  username VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  age INT NOT NULL
);
insert into EMPLOYEE(NAME, EMAIL, USERNAME, AGE) values('Employee', 'employee@gmail.com', 'employee', 25);
insert into EMPLOYEE(NAME, EMAIL, USERNAME, AGE) values('Employee1', 'employee1@gmail.com', 'employee1', 27);
insert into EMPLOYEE(NAME, EMAIL, USERNAME, AGE) values('Employee2', 'employee2@gmail.com', 'employee2', 23);
