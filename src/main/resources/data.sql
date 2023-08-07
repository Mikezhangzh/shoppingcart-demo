INSERT INTO CUSTOMER (name, email) VALUES ('Mike', 'mike@email.com');
INSERT INTO CUSTOMER (name, email) VALUES ('Jennifer', 'jennifer@email.com');
INSERT INTO PRODUCT (name, price) VALUES ('shoe', 100);
INSERT INTO PRODUCT (name, price) VALUES ('jean', 40);
INSERT INTO PRODUCT (name, price) VALUES ('shirt', 30);
INSERT INTO PRODUCT (name, price) VALUES ('hat', 20);
INSERT INTO PRODUCT (name, price) VALUES ('suit', 180);
select * from product;
INSERT INTO PRODUCT_DISCOUNT (description, product_id, discount_value, discount_unit, order_quantity, apply_quantity, discount_scope) VALUES ('shoes on sale', 1, 20, 'percentage', 1, 1, 'shared');
INSERT INTO PRODUCT_DISCOUNT (description, product_id, discount_value, discount_unit, order_quantity, apply_quantity, discount_scope) VALUES ('jeans nuy 1 get 1 free', 2, 100, 'percentage', 2, 1, 'shared');
INSERT INTO PRODUCT_DISCOUNT (description, product_id, discount_value, discount_unit, order_quantity, apply_quantity, discount_scope) VALUES ('suits 50 Dollors off', 5, 50, 'currency', 1, 1, 'shared');





