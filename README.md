# AMT_Project

## How to automate the docker deployment

- Build the image with the script `build-image.sh`
- You can access to the site at the port `http://localhost:9000/home`

## How to run tests

- Go to `e2e/`
- Install npm modules via `npm install`
- Run the server
- Execute with `npx codeceptjs run --steps`
- Run a single test with `codeceptjs run . my_test.js`

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
