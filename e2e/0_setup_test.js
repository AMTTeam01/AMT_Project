Feature('setup');

//setup file. Choose number of users to generate
Scenario('setup', (I) => {
    I.generateUsers(2)
});
