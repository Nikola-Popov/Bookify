<h1 align="center" style="border-bottom: none;">BOOKIFY🎟️</h1>
<p align="center">
A website which allows the users to search or create events to attend. The project follows the SOLID principles and it's designed in accordance to the MVC pattern with respect to security, user experiance, usability and testing.
</p>

The tech stack is solely [Java Spring](https://spring.io) for the backend while the frontend is mostly [Bootstrap](https://getbootstrap.com) and the rendering is powered by [Thymeleaf](https://www.thymeleaf.org). The website also relias on external dependencies for file storage and api documentaion (more details are described in the features section below).

## Table of Contents 

- [Features](#features)
- [Project structure overview](#project-structure-overview)
- [Getting started](#getting-started)
  - [Profiles setup](#profiles-setup)
     - [Development profile](#development-profile)
          - [Docker](#docker)
          - [Embedded Tomcat server](#embedded-tomcat-server)
     - [Production profile](#production-profile)
- [Kubernetes Deployment](#kubernetes-deployment)
- [License](#license)


---

## Features
![](https://media.giphy.com/media/W2uXr7q0AvTaJgSa5J/giphy.gif) 

### Events browsing and publishing
Allows for the users to browse already created events or to publish new ones. Each user can inspect each event separately and in details. Later the user can decide to pay and purchase an available event (which is not expired and has vouchers which are not already sold).

### Payment and cart checkout
In order to finish the payment process the user must first add the events they want to purchase to their Cart. The cart is kept in the database that's why it's not lost upon session close thus allowing better user experience. When the user proceeds with the checkout they can decide to remove something from the cart or pay the required sum. Afterwards the successfully completed payments are listed in the "User Profile" panel under the "Purchases" tab. 

### User profile and customizations
Every signed in user can access and configure their profile anytime allowing to change email, usernames and other. In addition to this everyone can upload a profile picture which is later stored in external cloud based file storage provider - [Cloudinary](https://cloudinary.com). All this provide a sense of customization and also strengthens the security of the profile as the login password can be easily changed through the profile panel. 

### User management 
The website support user managed via roles. The table summarizes the roles and their scope. The user groups in the table below are in descending order in regards to the permissions they have (for instance the _ADMIN_ role has all the permissions that are granted to the _GUEST_ and _USER_ roles). 

The first user to register in the website is granted a _ROOT_ permissions. There cannot be more than one root admin.

| Role        | Permissions           | 
| ------------- |:-------------:| 
| _GUEST_  | <ul><li>Browse the index page</li></ul> | 
| _USER_      | <ul><li>Access the user home page</li><li>Browse and create events</li> <li>Payment and cart checkouts</li><li>User profile settings</li></ul>  | 
| _ADMIN_      | <ul><li>Access to admin panel for easy users (both GUESTS and USERS), purchases and other services management</li><li>Access to the Swagger dashboard</li></ul>        | 
| _ROOT_ | <ul><li>Make modifications to the other admins via the admin panel</li></ul>      | 

### Admin panel
There is a rich featurd admin panel that allows for the _ROOT_ and _ADMIN_ users to easily manage the content of the website including the registed users. Some of the available possibilities are: modification of user profiles and credentials, deleting purchases, deleting users and others.

### REST API Documentation
The project is integrated with [Swagger.io](https://swagger.io) and provids internal REST API documentation accessiable at [/swagger-ui.html](/swagger-ui.html) (for instance, if deployed locally you can check it at <http://localhost:8080/swagger-ui.html>). This dashboard is only available to users with _ADMIN_ or _ROOT_ roles.

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
Note that while using docker run you will not be able to access the H2 web console.In other words <http://localhost:8080/h2> will block remote connections thus refusing access to it.
##### Embedded Tomcat server
```shell
$  mvn spring-boot:run
```
#### Production profile

```shell
$ java -jar -Dspring.profiles.active=prod ./target/bookify.war
```

---

## Kubernetes Deployment
Deployment is available via Kubernetes using the local docker image mentioned above. As a prerequisited the minikube tool must be up-and-running.
```shell
$ docker build -f Dockerfile -t bookify .
$ kubectl apply -f k8s/bookify-application-deployment.yaml 
$ minikube service bookify-webapp-service
```

---
## License

[MIT License](https://github.com/Nikola-Popov/Bookify/blob/master/LICENSE)
