# AMT_Project

## Features working

- Sign up / login
- Create question
- Comment/Answer
- Upvote questions
- Browse and filter questions
- My profile view

## Features not working

- Edit profile works, but no checks on if the user is already taken, new password does not work
- Upvotes on answers
- JMeter load tests missing a bunch

## Get Started

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
