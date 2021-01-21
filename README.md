# AMT : Help2000

## Introduction

For the AMT Course at HEIG-VD, we will be developping a stackoverflow-copycat called **Help2000**. This website will enable its users to browse, post questions and comments.

Alongside, a gamification API will interact with the **Help2000** interface in order to challenge users to try and win badges and points.

## Features

### Sign up / login

The user can sign up and then login to have a **help2000** profile. After a profile has been created, it can be accessed on the profile page. The user will be able to change its personal data.
 
### Questions

A logged in user can create a question with a title and a description. Other users will then be able to comment on the questions and give answers as well.

### Feed

The user can also browse and filter questions. A question can be opened by simply clicking on it.

### Gamification enhancement 

The gamification API will give our users the ability to gain badges and points. A user can see its badges on the profile page. The leaderboard page shows the top 10 users based on their total points in a certain category.

## Features not working

- JMeter load tests are missing
- Upvote and down vote
- Add tags to a question

## Get Started

This project is dependant to our [REST API project](https://github.com/amtteamheig/amt_project_api). Before launging this project, run the REST API first and retreive the API key after running the `populate-api.sh` and past the key in the file `src/main/resources/META-INF/microprofile-config.properties` under the propertie `apikey`.

Don't forget to set the correct link to the API, `localhost` if you're running with `mvn liberty:dev` or `gamification-container` if you're using docker. You can set this value in the file`src/main/resources/META-INF/microprofile-config.properties` .

To get started and launch the database and the web app, run the command `./run-app.sh`. This will start both entities in seperate docker container.

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
- You can access to the site at the port `http://localhost:9000/home`

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

