Feature('sign up & login');

Scenario('test registration and login process', (I) => {
    //registering
    I.amOnPage('/register')

    //not matching passwords
    I.fillField('#register_username','123')
    I.fillField('#register_email','123@123')
    I.fillField('#register_password',secret('12345'))
    I.fillField('#register_cPassword',secret('123456'))
    I.click('Sign up')
    I.waitForText('Your password and your confirmation aren\'t the same')

    //matching passwords
    I.fillField('#register_username','123')
    I.fillField('#register_email','123@123')
    I.fillField('#register_password',secret('12345'))
    I.fillField('#register_cPassword',secret('12345'))
    I.click('Sign up')
    I.dontSeeInCurrentUrl('/register')

    //login
    I.amOnPage('/login')

    //wrong login
    I.fillField('#login_username','123')
    I.fillField('#login_password',secret('123456'))
    I.click('Login')
    I.waitForText('Your e-mail or your password is incorrect')

    //right login
    I.fillField('#login_username','123')
    I.fillField('#login_password',secret('12345'))
    I.click('Login')
    I.dontSeeInCurrentUrl('/login')
});
