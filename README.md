# Introduction
A simple application to parse log files.

# Database
When running tests embedded h2 db is used. Connection details are set in ```src/test/resources/application.properties```.

When running "on production" MySql db is used. Connection details are defined in ```src/main/resources/application.properties```. Before running application outside of tests, you need to create the schema. Schema is automatically generated in file ```target/create.sql``` in root directory when running the application.

# Building
Simply run script mvnw. This will download the newest maven for you if needed. Just like that:
```bash
./mvnw package
```
The executable jar can be then found in the ```target``` directory.

# Running
This is a Spring Boot application. On production logging is enabled only for application package "com.ef".

Therefore, to run the application invoke the following command:
```bash
java -jar parser-0.0.1-SNAPSHOT.jar --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
```
To import the access log file into db invoke the following command:
```bash
java -jar parser-0.0.1-SNAPSHOT.jar --accesslog=/tmp/test-access.log
```
You can combine commands:
```bash
java -jar parser-0.0.1-SNAPSHOT.jar --accesslog=/tmp/test-access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
```

Note that importing access log can take a while.

# SQL queries
You can run the following SQL query directly in the db shell to find the results just like the app does. This example query will find all ips above 200 threshold and within an hour starting from 2017-01-01 15:00:00:
```sql
SELECT ip FROM log_entry 
WHERE date BETWEEN "2017-01-01.15:00:00" AND "2017-01-01.16:00:00" 
GROUP BY ip 
HAVING COUNT(ip) > 200;
```
