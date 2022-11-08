-- INSERT INTO clients (id, name, lastname, email, created_at) VALUES (1,'Alex','Axes','alex@test.com','2022-10-21'), (2,'John','doe','john@test.com','2022-10-21'), (3,'Mafer','Eli','mafer@test.com','2022-10-21'), (4,'Edu','Ernest','edu@test.com','2022-10-21'), (5,'Sol','Doe','sol@test.com','2022-11-15'), (6,'Karen','Barr','karen@test.com','2022-12-12');

INSERT INTO clients (name, lastname, email, created_at, photo) VALUES ('Alex','Axes','alex@test.com','2022-10-21', ''), ('John','doe','john@test.com','2022-10-21', ''), ('Mafer','Eli','mafer@test.com','2022-10-21', ''), ('Edu','Ernest','edu@test.com','2022-10-21', ''), ('Sol','Doe','sol@test.com','2022-11-15', ''), ('Karen','Barr','karen@test.com','2022-12-12', ''), ('Andres', 'Guzman', 'profesor@bolsadeideas.com', '2020-08-01', ''), ('John', 'Doe', 'john.doe@gmail.com', '2020-08-02', ''), ('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2020-08-03', ''), ('Jane', 'Doe', 'jane.doe@gmail.com', '2020-08-04', ''), ('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2020-08-05', ''), ('Erich', 'Gamma', 'erich.gamma@gmail.com', '2020-08-06', ''), ('Richard', 'Helm', 'richard.helm@gmail.com', '2020-08-07', ''), ('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2020-08-08', ''), ('John', 'Vlissides', 'john.vlissides@gmail.com', '2020-08-09', ''), ('James', 'Gosling', 'james.gosling@gmail.com', '2020-08-010', ''), ('Bruce', 'Lee', 'bruce.lee@gmail.com', '2020-08-11', ''), ('Johnny', 'Doe', 'johnny.doe@gmail.com', '2020-08-12', ''), ('John', 'Roe', 'john.roe@gmail.com', '2020-08-13', ''), ('Jane', 'Roe', 'jane.roe@gmail.com', '2020-08-14', ''), ('Diana', 'Roe', 'diana.roe@gmail.com', '2020-09-14', '');


-- Populate products table
INSERT INTO products (name, price, created_at) VALUES('Panasonic Pantalla LCD', 1200, NOW());
INSERT INTO products (name, price, created_at) VALUES('Sony Camera digital DSC-W320', 450, NOW());
INSERT INTO products (name, price, created_at) VALUES('Apple iPod shuffle', 120, NOW());
INSERT INTO products (name, price, created_at) VALUES('Sony Notebook Z100', 990, NOW());
INSERT INTO products (name, price, created_at) VALUES('Hawei Mate 20 Pro', 912, NOW());
INSERT INTO products (name, price, created_at) VALUES('Bianchi Bicilceta Aro 26', 720, NOW());
INSERT INTO products (name, price, created_at) VALUES('Comoda 5 cajones', 321, NOW());
INSERT INTO products (name, price, created_at) VALUES('Acer Predator Hellios 300', 1422, NOW());
INSERT INTO products (name, price, created_at) VALUES('OMEON HP 17inch', 1110, NOW());

/* Creamos algunas facturas */
INSERT INTO invoices (description, observation, client_id, created_at) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO invoice_items (quantity, invoice_id, product_id) VALUES(1, 1, 1);
INSERT INTO invoice_items (quantity, invoice_id, product_id) VALUES(2, 1, 4);
INSERT INTO invoice_items (quantity, invoice_id, product_id) VALUES(1, 1, 5);
INSERT INTO invoice_items (quantity, invoice_id, product_id) VALUES(1, 1, 9);

INSERT INTO invoices (description, observation, client_id, created_at) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO invoice_items (quantity, invoice_id, product_id) VALUES(3, 2, 6);


/* Users and Roles */
INSERT INTO `users` (username, password, enabled) VALUES ('alex','$2a$10$pVgl6t9pbJY4ztb.OnoF0.PN1XDaaIHf26IaaW8xCfae8ajbuTGby',1);
INSERT INTO `users` (username, password, enabled) VALUES ('admin','$2a$10$S8lhZWiD3JEYkxl7SqH74uXKEEIbR9ra7eSgKIEN2K05edJTh9ubK',1);

INSERT INTO `authorities` (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO `authorities` (user_id, authority) VALUES (2,'ROLE_ADMIN');
INSERT INTO `authorities` (user_id, authority) VALUES (2,'ROLE_USER');