# AMT : Help2000

## Introduction

For the AMT Course at HEIG-VD, we will be developping a stackoverflow-copycat called **Help2000**. This website will enable its users to browse, post questions and comments.

Alongside, a gamification API will interact with the **Help2000** interface in order to challenge users to try and win badges and points.

## Features

### Sign up / login

The user can sign up and then login to have a **Help2000** profile. After a profile has been created, it can be accessed on the profile page. The user will be able to change its personal data.
 
### Questions

A logged in user can create a question with a title and a description. Other users will then be able to comment on the questions and give answers as well.

### Feed

The user can also browse and filter questions. A question can be opened by simply clicking on it.

### Gamification enhancement 

The gamification API will give our users the ability to gain badges and points. A user can see its badges on the profile page. The leaderboard page shows the top 10 users based on their total points in a certain category.

## Features not working & Issues

- JMeter load tests are missing
- Upvote and down vote
- Add tags to a question
- Connectivity issue between the API and Help2000 : some requests (randomly) sent by the web-app aren't received by the API but are send by the web-app (checked with Wireshark). There is no error from Open Liberty, but the server is waiting in an endless loop for an answer.

## Get Started

This project is dependant to our [REST API project](https://github.com/amtteamheig/amt_project_api). Before launching this project, run the REST API following the guidelines in the README and retreive the API key after running the `populate-api.sh` script. Once you have the key, put it in the file `src/main/resources/META-INF/microprofile-config.properties` under the variable `apikey`.

Don't forget to set the correct link to the API, `localhost` if you're running with `mvn liberty:dev` or `gamification-container` if you're using docker. You can set this value in the file`src/main/resources/META-INF/microprofile-config.properties` .

To get started and launch the database and the web app, run the command `./run-app.sh`. This will start both entities in seperate docker containers.

## Implementation

### API Access

To access the gamification API, we have the class ```APIUtils.java```. This class enables a direct access to the API's endpoint. In order to be able to use those endpoints our application has to be registered and with an access token. This token is then taken to authenticate the application from an environnement variable*. 

We use the apache HttpClient library to access the api's endpoints and gson for deserializing the api's responses.

*If you want to launch the docker web-app you need to go to the ```microprofile-config.properties``` and uncomment the address to ```gamification-container``` and comment the one with ```localhost```.

## Access to the database

To get access to the database, you need to follow those steps : 

1. Connect to the mysql container: 
`docker exec -it help2000_db_container mysql -u dbDevHelp2000 -p`
2. Enter the root password : `devpass`
3. Enter the app's database by typing : `mysql> USE help2000`
4. Now you can display all tables with the command : `show tables;`

### DB schema

![](images/db_help2000.png)

## How to automate the docker deployment

- Build the image with the script `build-image.sh`
- You can access to the at `http://localhost:9000/home`

## Package structure

```
src/
└── main
    ├── java
    │   └── ch
    │       └── heigvd
    │           └── amt
    │               └── mvcProject
    │                   ├── application
    │                   │   └── user
    │                   ├── domain
    │                   │   └── user
    │                   ├── infrastructure
    │                   │   └── persistence
    │                   │       └── memory
    │                   └── ui
    │                       └── web
    │                           └── user
    │                               └── handler
    └── webapp
        ├── assets
        │   ├── css
        │   ├── imgs
        │   └── js
        └── WEB-INF
            └── views
                └── fragments

```

- `application` : All class that describe how the user can interact with the
 domain
- `domain` : Business object, can not interect with outside => Independent !
- `insfratructure`
    - `persistence` : The connection with the data
- `ui` : Interaction with the UI
    - `web` : servlet, dependance, framework

## Downloading the package
The package can be downloaded from the organisation's packages, since the organisation is private you need to ask for privileges to one of the administrators. Then you can run this command with docker to get the container : `docker pull ghcr.io/amtteamheig/amt_project:latest`

