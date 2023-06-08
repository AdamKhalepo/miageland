INSERT INTO PARK (ID,JAUGE_MAX,NB_MAX_JOURNALIER) VALUES (1,1000,0);

-- Roles : 0 = employee, 1 = admin, 2 = manager
INSERT INTO EMPLOYEE (FIRST_NAME,NAME,EMAIL,ROLE) VALUES ('Tchouk','Tchouk','tchoukdu12@gmail.com','2');
INSERT INTO EMPLOYEE (FIRST_NAME,NAME,EMAIL,ROLE) VALUES ('Adam','Khalepo','adam@gmail.com','0');
INSERT INTO EMPLOYEE (FIRST_NAME,NAME,EMAIL,ROLE) VALUES ('Lydia','Boulfrad','lydia@gmail.com','0');
INSERT INTO EMPLOYEE (FIRST_NAME,NAME,EMAIL,ROLE) VALUES ('Maneva','Rasolondraibe','maneva@gmail.com','1');

INSERT INTO VISITOR (FIRST_NAME,NAME,EMAIL) VALUES ('Rémi','Saurel','remi@gmail.com');
INSERT INTO VISITOR (FIRST_NAME,NAME,EMAIL) VALUES ('Chaimae','Khamma','chaimae@gmail.com');

INSERT INTO TICKET (VISIT_DATE,VISITOR_ID,PRICE,STATE) VALUES ('2023-05-25',1,30,0);
INSERT INTO TICKET (VISIT_DATE,VISITOR_ID,PRICE,STATE) VALUES ('2023-05-25',1,30,1);
INSERT INTO TICKET (VISIT_DATE,VISITOR_ID,PRICE,STATE) VALUES ('2023-05-25',1,30,2);

INSERT INTO ATTRACTION (NAME,IS_OPEN) VALUES ('Le grand huit',1);
INSERT INTO ATTRACTION (NAME,IS_OPEN) VALUES ('Tour de la terreur',1);
INSERT INTO ATTRACTION (NAME,IS_OPEN) VALUES ('Space moutain',1);
INSERT INTO ATTRACTION (NAME,IS_OPEN) VALUES ('Manoir hantée',0);

