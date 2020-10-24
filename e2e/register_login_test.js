Feature('sign up & login');

function makeid(length) {
    var result           = '';
    var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for ( var i = 0; i < length; i++ ) {
       result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

function makeEmail(){
    return makeid(4).concat("@").concat(makeid(4))
}

class User {
     constructor(username,email,password,wrongPassword){
        this.username = username;
        this.email = email;
        this.password = password;
        this.wrongPassword = wrongPassword;
    }
}

let user1 = new User(makeid(8),makeEmail(),'123','1234')

Scenario('test registration and login process', (I) => {

    //login not found account
    I.amOnPage('/login')
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_password',secret(user1.password))
    I.click('Login')
    I.waitForText('The user hasn\'t been found')

    //not matching passwords
    I.amOnPage('/register')
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_email',user1.email)
    I.fillField('#txt_password',secret(user1.password))
    I.fillField('#txt_cpassword',secret(user1.wrongPassword))
    I.click('Sign up')
    I.waitForText('The password and the confirmation of the password aren\'t the same')

    //matching passwords
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_email',user1.email)
    I.fillField('#txt_password',secret(user1.password))
    I.fillField('#txt_cpassword',secret(user1.password))
    I.click('Sign up')
    I.dontSeeInCurrentUrl('/register')

    //trying to register again
    I.amOnPage('/register')
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_email',user1.email)
    I.fillField('#txt_password',secret(user1.password))
    I.fillField('#txt_cpassword',secret(user1.password))
    I.click('Sign up')
    I.waitForText('Username is already used')

    //trying to register again, with another username, same email
    I.amOnPage('/register')
    I.fillField('#txt_username', makeid(5))
    I.fillField('#txt_email',user1.email)
    I.fillField('#txt_password',secret(user1.password))
    I.fillField('#txt_cpassword',secret(user1.password))
    I.click('Sign up')
    I.waitForText('Email is already used')

    //login
    I.amOnPage('/login')

    //wrong login
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_password',secret(user1.wrongPassword))
    I.click('Login')
    I.waitForText('Check of credentials failed')

    //right login
    I.fillField('#txt_username', user1.username)
    I.fillField('#txt_password',secret(user1.password))
    I.click('Login')
    I.dontSeeInCurrentUrl('/login')
});
