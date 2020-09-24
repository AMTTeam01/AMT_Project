# AMT_Project

## How to automate the docker deployment

- Build the image with the script `build-image.sh`
- Run `docker-compose up --build` in `docker/topologies` to build and run the image
- You can access to the site at the port 8080

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
