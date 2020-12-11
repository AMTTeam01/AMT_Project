Feature('landing_page')

Scenario('landing page tests', ({ I }) => {
    //landing page setup
    I.amOnPage('/')
    I.see('Question about programming ?')
    I.seeElement('#home_signUp')
    I.seeElement('#home_login')

    //sign up button
    I.click('Sign up')
    I.seeInCurrentUrl('/register')

    //login button
    I.amOnPage('/')
    I.click('Login')
    I.seeInCurrentUrl('/login')
});