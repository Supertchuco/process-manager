Insert into USER_TYPE (user_Type_Id, user_Type_Name) values (1,'ADMINISTRADORTest');
Insert into USER_TYPE (user_Type_Id, user_Type_Name) values (2,'USUARIO-TRIADORTest');
Insert into USER_TYPE (user_Type_Id, user_Type_Name) values (3,'USUARIO-FINALIZADORTest');


Insert into PROCESS_USER (user_Id, user_Name, password, create_Date, create_By, user_Type_Id) values (1, 'userAdminTest', 'passtest', to_timestamp('24/10/18 09:02:24,107000000','DD/MM/RR HH24:MI:SSXFF'), 'SystemTest', 1);
Insert into PROCESS_USER (user_Id, user_Name, password, create_Date, create_By, user_Type_Id) values (2, 'userTriadorTest', 'passtest', to_timestamp('24/10/18 09:02:24,107000000','DD/MM/RR HH24:MI:SSXFF'), 'SystemTest', 2);
Insert into PROCESS_USER (user_Id, user_Name, password, create_Date, create_By, user_Type_Id) values (3, 'userFinalizadorTest', 'passtest', to_timestamp('24/10/18 09:02:24,107000000','DD/MM/RR HH24:MI:SSXFF'), 'SystemTest', 3);