<h1 align="center" style="border-bottom: none;">BOOKIFY⛳</h1>
<p align="center">
A website which allows the users to search or create events to attend. The project follows the SOLID principles and it's designed in accordance to the MVC pattern with respect to security, user experiance, usability and testing.
</p>

The tech stack is solely [Java Spring](https://spring.io) for the backend while the frontend is mostly [Bootstrap](https://getbootstrap.com) and the rendering is powered by [Thymeleaf](https://www.thymeleaf.org). The website also relias on external dependencies for file storage and api documentaion (more details below).

## Table of Contents 

- [Features](#features)
- [Project structure overview](#project-structure-overview)
- [Getting started](#getting-started)
  - [Profiles setup](#profiles-setup)
     - [Development profile](#development-profile)
          - [Docker](#docker)
          - [Embedded Tomcat server](#embedded-tomcat-server)
     - [Production profile](#production-profile)
- [License](#license)


---

## Features

### User management 
The website support user managed via roles. The table summarizes the roles and their scope. The user groups in the table below are in descending order in regards to the permissions they have (for instance the _ADMIN_ role has all the permissions that are granted to the _GUEST_ and _USER_ roles). 

The first user to register in the website is granted a _ROOT_ permissions. There cannot be more than one root admin.

| Role        | Permissions           | 
| ------------- |:-------------:| 
| _GUEST_  | <ul><li>Browse the index page</li></ul> | 
| _USER_      | <ul><li>Access the user home page</li><li>Browse and create events</li> <li>Payment and cart checkouts</li><li>User profile settings</li></ul>  | 
| _ADMIN_      | <ul><li>Access to admin panel for easy users (both GUESTS and USERS), purchases and other services management</li><li>Access to the Swagger dashboard</li></ul>        | 
| _ROOT_ | <ul><li>Make modifications to the other admins via the admin panel</li></ul>      | 

### Cloud file storage
Every user can upload a profile picture which is later stored in external cloud based file storage provider - [Cloudinary](https://cloudinary.com).

### REST API Documentation
The project is integrated with [Swagger.io](https://swagger.io) and provids internal REST API documentation accessiable at [/swagger-ui.html](/swagger-ui.html) (for instance, if deployed locally you can check it at <http://localhost:8080/swagger-ui.html>). This dashboard is only available to users with _ADMIN_ or _ROOT_ roles.

### Tested
The service and the controller layers are extremelly well unit tested. The unit test coverage is above 80%.

---

## Project structure overview
```text
├── bookify        
|  ├── commons        // Contains common factories, constants and exceptions for the whole project 
|  ├── config         // Configuration beans 
|  ├── domain         // All the data models and entities
|  ├── repository	  // Interfaces for interacting with the data (domain) layer
|  ├── service		  // The business functionality. All from user managed, to payments and checkouts
|  ├── validation     // Useful validator utilities
|  ├── web   		  // The controllers and interceptors reside here
```

---

## Getting started

To begin with, start by building the project. Also let's assume that all the commands listed below are executed from the root of the cloned project.
```shell
$ cd bookify
$ mvn clean install
```
### Profiles setup
The approaches differ only in the configurations (more precisely the application.properties). The Development profile is using an in-memory H2 database while the Production profile relias on MongoDB provided by Amazon (requested via Heroku).
#### Development profile
##### Docker
```shell
$ docker build -f Dockerfile -t bookify .
$ docker run -p 8080:8080 bookify
```
Note that while using docker run you will not be able to access the H2 web console.In other words <http://localhost:8080/h2> will block remote connections will refuse access to it.
##### Embedded Tomcat server
```shell
$  mvn spring-boot:run
```
#### Production profile

```shell
$ java -jar -Dspring.profiles.active=prod ./target/bookify.war
```
---
## License

[MIT License](https://github.com/Nikola-Popov/Bookify/blob/master/LICENSE)
