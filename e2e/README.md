## How to run tests

- Install npm modules via `npm install`
- Run the server
- Execute with `npx codeceptjs run --steps`
- Run a single test with `npx codeceptjs run my_test.js --steps` Make sure you have ran `0_setup_test.js` before

## How to write a new test

- Create a new test via `npx codeceptjs gt`
- Then complete the file `<your test>_test.js`, doc found at https://codecept.io/basics/#architecture