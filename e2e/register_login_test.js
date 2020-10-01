Feature('sign up & login');

Scenario('test registration and login process', (I) => {
    //registering
    I.amOnPage('/register')

    //not matching passwords
    I.fillField('#txt_username','123')
    I.fillField('#txt_email','123@123')
    I.fillField('#txt_password',secret('12345'))
    I.fillField('#txt_cpassword',secret('123456'))
    I.click('Sign up')
    I.waitForText('The password and the confirmation of the password aren\'t the same')

    //matching passwords
    I.fillField('#txt_username','123')
    I.fillField('#txt_email','123@123')
    I.fillField('#txt_password',secret('12345'))
    I.fillField('#txt_cpassword',secret('12345'))
    I.click('Sign up')
    I.dontSeeInCurrentUrl('/register')

    //login
    I.amOnPage('/login')

    //wrong login
    I.fillField('#txt_username','123')
    I.fillField('#txt_password',secret('123456'))
    I.click('Login')
    I.waitForText('Check of credentials failed')

    //right login
    I.fillField('#txt_username','123')
    I.fillField('#txt_password',secret('12345'))
    I.click('Login')
    I.dontSeeInCurrentUrl('/login')
});
