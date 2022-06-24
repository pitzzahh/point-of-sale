# point-of-sale
![1](https://github.com/pitzzahh/point-of-sale/blob/f911914a475a02cc6e7450485fef70fec63f1d30/main_ui.png?raw=true)

### How to run the application
#### Requirements

- JDK 17 LTS or JDK 18 (JDK 1.8 would probably work)
- PostgreSQL database named 'pos' (can change to any DBMS (h2 database doesn't seem to work))
- Change username and password in application.properties (username and password of your DBMS).
- Change the url of the database, based on the url of your own DBMS.
## Note

- At start, a text file will be created and stored in ```C:\User\Public\check.txt``` 
- The file denotes that the products has been inserted to the table.
- If you delete the file and try to rerun the application, the products will be inserted again to the table in the database, which should not happen.
- The text file is harmless.

