# Where Am I?

### NOTE:
*This is a Springboot backend for my 'Where am I?' project and requires access to the [frontend here](https://github.com/TimBroderick44/WhereAmI-Frontend).

This is a simple backend that allows for authenticated users to look up the postcodes of suburbs and vice versa. 

It also allows for admin users to perform a variety of other tasks such as:

- Create, update and delete suburbs and / or postcodes. 
- Create, read, update and delete roles and users for the application. 

The app features custom error handling and authentication. 

## Build Steps

To run this locally, you will need to do the following:

1. Required software and dependencies:
     - [ ] Java Development Kit: https://www.oracle.com/au/java/technologies/downloads/
     - [ ] Apache Maven: https://maven.apache.org/download.cgi
     - [ ] MySQL: https://dev.mysql.com/downloads/installer/

2. Clone to repository to your local machine and run the following commands:

```bash
git clone https://github.com/TimBroderick44/Postcode-API
```

```bash
cd Postcode-API
```

3. Set up access to your database:
   - Using the mySQL CLI (or the mySQL workbench) to login to your account:

```bash
mysql -u root -p
```

4. Create a new database for use with the app:

```sql
CREATE DATABASE your_database_name;
```

5. Configure the application properties:

- In the src/main/resources directory, create 'application.properties' and add the following:

```java
spring.application.name=Postcode-API
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

6. Build (the 'clean' keyword ensures that it is built from scratch):

```bash
mvn clean install
```

7. Using postman, go to 'Authorization' and change the type to 'Basic Auth'. 

  ```
  For admin, use:

  username :  admin1
  password :  admin

  For a regular user, use:

  username : user1
  password : userpassword
  ```

8. Make a GET request (either as admin or user) to： 
```   
http://localhost:8080/allSuburbsPostcodes (or http://127.0.0.1:8080/allSuburbsPostcodes)
```
9. Follow the steps [here](https://github.com/TimBroderick44/WhereAmI-Frontend) and set up the frontend. 

## What did I use to create it?

This project uses:

-   SpringBoot
-   Java
-   mySQL 

## Features of the Project:

-  Allows for CRUD operations on role, user and suburb/postcode dependant on the role
-  Implements authentication
-  Hashes passwords and does not expose them. 
-  Handles all errors

<h1 style="font-weight: 900"> Here are some of the lessons I learnt:</h1>

### Authentication:

-  This was a great project for learning more about authentication in SpringBoot. Through a simple security config file, I was able to make sure that certain endpoints were only available to users with the appropriate role. 
  
### Passwords:

- Similarly, by simply using the BCryptPasswordEncoder, I was able to have passwords hsahed and securely stored in the database. When admin made GET requests to see data on users, the passwords were not returned. 
  
### Error Handling:

In previous projects, I had included error handling; however, in this project, it came a lot more naturally to me. I was able to reference - even reuse - some of the previous code and was confidently able to create some new customs handlers too.

<h1 style="font-weight: 900"> Change Log:</h1>

07/06/2024 - Initial commit. Started on endpoints and general structure of the app. 

08/06/2024 - Implemented authentication and password hashing / generation. Learnt about using 'data.sql' and 'schema.sql' to seed data for testing. 

09/06/2024 - Manually tested endpoints and refactored existing code.

## What I still need to do:

-   [x] Implement proper testing
-   [x] Create a front end to show off its functionality
-   [ ] Allow for pagination of results 
-   [ ] Allow for dynamic filtering (i.e. allow for the user to decide what specific results they want back)
-   [ ] Allow for batch requests

## Thank You!

Thank you for taking the time to look at this project. I really hope you enjoy it.
Feel free to reach out and ask any questions.

[Tim Broderick]
