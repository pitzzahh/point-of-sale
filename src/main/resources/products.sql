CREATE TABLE IF NOT EXISTS products (
	product_id SERIAL NOT NULL PRIMARY KEY,
	name TEXT NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	category TEXT NOT NULL,
	expiration_date DATE NOT NULL,
	stocks_left INT NOT NULL,
	discount DOUBLE PRECISION NOT NULL
);

-- CLEANING PRODUCTS CATEGORY
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('CLEAN FIRST', 25.69, 'CLEANING_PRODUCT', '2023-01-01', 321, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SPIN MOP', 40.41, 'CLEANING_PRODUCT', '2025-03-02', 50, 2.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('WINDEX', 34.12, 'CLEANING_PRODUCT', '2025-04-02', 345, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('CLOROX', 50.12, 'CLEANING_PRODUCT', '2022-07-12', 127, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('DYSON HAND VACUUM CLEANER', 3000.99, 'CLEANING_PRODUCT', '2027-02-05', 30, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('ROOMBA ROBOT VACUUM CLEANER', 5099.99, 'CLEANING_PRODUCT', '2027-02-05', 20, 500.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('CLEAN CUT', 260.12, 'CLEANING_PRODUCT', '2022-07-01', 242, 4.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SURE CLEAN', 25.69, 'CLEANING_PRODUCT', '2023-01-01', 500, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('ARIEL', 50.69, 'CLEANING_PRODUCT', '2022-08-24', 142, 5.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('JOY', 10.00, 'CLEANING_PRODUCT', '2023-01-01', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SMART DISHWASHING PASTE', 26.00, 'CLEANING_PRODUCT', '2023-01-01', 150, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('DOMEX', 54.88, 'CLEANING_PRODUCT', '2023-04-21', 500, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('MR MUSCLE', 69.69, 'CLEANING_PRODUCT', '2023-01-01', 50, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('LYSOL', 45.99, 'CLEANING_PRODUCT', '2023-01-01', 70, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SURF', 60.12, 'CLEANING_PRODUCT', '2023-01-01', 100, 0.0);
-- CHOCOLATES CATEGORY
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('HERSHEY''S', 22.75, 'CHOCOLATE', '2022-07-03', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SNICKERS', 22.75, 'CHOCOLATE', '2022-08-04', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('FERRERO ROCHER', 120.00, 'CHOCOLATE', '2022-08-05', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('ESTHECHOC', 800.00, 'CHOCOLATE', '2022-08-06', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('FLYING NOIR', 1500.00, 'CHOCOLATE', '2022-08-07', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('DROSTE', 800.00, 'CHOCOLATE', '2022-08-21', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('WHITTAKER''S', 999.99, 'CHOCOLATE', '2022-08-08', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('AMEDEI', 150.75, 'CHOCOLATE', '2022-08-09', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('JACQUES GENIN', 220.75, 'CHOCOLATE', '2022-08-10', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('RICHART', 350.12, 'CHOCOLATE', '2022-07-11', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('PATCHI', 480.99, 'CHOCOLATE', '2022-07-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('TEUSCHER', 120.12, 'CHOCOLATE', '2022-07-13', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('VALRHONA', 370.69, 'CHOCOLATE', '2022-08-03', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('DOVE', 280.14, 'CHOCOLATE', '2022-08-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('RUSSEL STOVER', 110.00, 'CHOCOLATE', '2022-08-24', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('RITTER SPORT', 170.00, 'CHOCOLATE', '2022-08-13', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('GUYLIAN', 150.75, 'CHOCOLATE', '2022-09-06', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('KINDER', 70.45, 'CHOCOLATE', '2022-09-07', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('MARS', 22.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('TOBLERONE', 80.00, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('NESTLE', 56.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('MILKA', 120.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('GHIRARDELLI', 310.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('CADBURY', 45.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('GODIVA', 670.75, 'CHOCOLATE', '2022-09-12', 100, 0.0);
-- BEVERAGES CATEGORY
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('COCA-COLA', 75.00, 'BEVERAGE', '2022-12-12', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('PEPSI', 55.00, 'BEVERAGE', '2022-12-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('RED BULL', 67.12, 'BEVERAGE', '2022-12-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('BUD WISER', 90.00, 'BEVERAGE', '2022-8-29', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('HEINEKEN', 45.00, 'BEVERAGE', '2022-8-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('GATORADE', 35.00, 'BEVERAGE', '2022-7-13', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SPRITE', 55.00, 'BEVERAGE', '2023-01-10', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('MINUTE MAID', 25.00, 'BEVERAGE', '2022-6-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('TROPICANA', 32.00, 'BEVERAGE', '2022-12-20', 40, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('DOLE', 42.00, 'BEVERAGE', '2022-12-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('KOOL AID', 67.00, 'BEVERAGE', '2022-12-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('7 UP', 25.00, 'BEVERAGE', '2022-08-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('LIPTON', 46.00, 'BEVERAGE', '2022-09-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('SUNKIST', 76.00, 'BEVERAGE', '2022-09-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('LIPTON', 46.00, 'BEVERAGE', '2022-09-21', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('APPLE JUICE', 25.00, 'BEVERAGE', '2022-09-22', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('PINEAPPLE JUICE', 25.00, 'BEVERAGE', '2022-09-23', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('BLACK CHERRY', 50.00, 'BEVERAGE', '2022-09-23', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('TEQUILA', 2200.69, 'BEVERAGE', '2022-09-25', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('BEER', 190.00, 'BEVERAGE', '2022-09-26', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('WINE', 506.00, 'BEVERAGE', '2022-09-26', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('HARD CIDER', 780.99, 'BEVERAGE', '2022-9-27', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('MEAD', 460.00, 'BEVERAGE', '2022-08-27', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('GIN', 120.00, 'BEVERAGE', '2022-09-20', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('BRANDY', 746.00, 'BEVERAGE', '2022-10-27', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('WHISKY', 406.00, 'BEVERAGE', '2022-11-27', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('RUM', 321.00, 'BEVERAGE', '2022-11-27', 100, 0.0);
INSERT INTO products(name, price, category, expiration_date, stocks_left, discount) VALUES('VODKA', 426.00, 'BEVERAGE', '2022-12-27', 100, 0.0);


