# point-of-sale
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

![1](https://github.com/pitzzahh/point-of-sale/blob/0962af88d0e7115b207245555a434670e69d273b/main_ui.png?raw=true)

# How to run the application

## Requirements

- JDK 17 LTS or JDK 18 (JDK 1.8 would probably work)
- PostgreSQL database named 'pos' (can change to any DBMS (h2 database doesn't seem to work)) 
- Change username and password in the application.properties (username and password of your DBMS).
- Change the URL of the database based on the URL of your DBMS.

### Functions

- Edit product stocks.
- Edit product price.
- Edit product discount.
- Can't purchase a product if a product is; EXPIRED, OUT OF STOCK, REMOVED.
- Stocks are subtracted based on the quantity of the product purchased.
- Remove expired products.
- Remove all expired products.
- View total revenue.

# Note

- Before running the application, execute the sql file stored in the resource folder. Without doing so the products will not be available, and the application will stuck at loading.
- The icons of the application will only appear when an internet connection is available (can't set icon with local files)
- If you use the maven dependency, you don't need to download some of the requirements. The dependencies of this project will also be added to your project.
