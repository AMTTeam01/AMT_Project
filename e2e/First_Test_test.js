Feature('First_Test');

Scenario('test something', (I) => {
    I.amOnPage('https://codecept.io/quickstart/')
    I.see('CodeceptJS')
});
