# AMT_Project

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
