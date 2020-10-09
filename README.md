# AMT_Project

## Introduction

## Get Started

To get started and launch the database and the web app, run the command `./run-app.sh`. This will start both entities in seperate docker container.

## Access to the database

To get access to the database, you need to follow those steps : 

1. Connect to the mysql container: 
`docker exec -it help2000_db_container mysql -u dbDevHelp2000 -p`
2. Enter the root password : `devpass`
3. Enter the app's database by typing : `mysql> USE help2000`
4. Now you can display all tables with the command : `show tables;`

## How to automate the docker deployment

- Build the image with the script `build-image.sh`
- You can access to the site at the port `http://localhost:9000/home`

## How to run tests

- Go to `e2e/`
- Install npm modules via `npm install`
- Run the server
- Execute with `npx codeceptjs run --steps`
- Run a single test with `npx codeceptjs run my_test.js`

## How to write a new test

- Go to `e2e/`
- Create a new test via `npx codeceptjs gt`
- Then complete the file `<your test>_test.js`, doc found at https://codecept.io/basics/#architecture

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
