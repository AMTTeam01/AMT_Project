Feature('landing page');

Scenario('landing page tests', (I) => {
    I.amOnPage('/')
    I.see('Question about programming ?')
    I.seeElement('#home_signUp')
    I.seeElement('#home_login')
});
