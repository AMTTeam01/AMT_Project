// in this file you can append custom step methods to 'I' object

function makeRandomString(length) {
  var result           = '';
  var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  var charactersLength = characters.length;
  for ( var i = 0; i < length; i++ ) {
     result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}

function makeEmail(){
  return makeRandomString(4).concat("@").concat(makeRandomString(4))
}

class User {
  constructor(){
    this.username = makeRandomString(5);
    this.email = makeEmail();
    this.password = makeRandomString(5);
    this.wrongPassword = makeRandomString(6);
  }
}

let users = [];

module.exports = function() {
  return actor({

    // Define custom steps here, use 'this' to access default methods of this.
    // It is recommended to place a general 'login' function here.

    registerUsers(amount){

      users = []
      for(i = 0; i < amount; i++){
        users.push(new User())
      }

      for(let user of users){
        this.amOnPage('/register')
        this.fillField('#txt_username', user.username)
        this.fillField('#txt_email',user.email)
        this.fillField('#txt_password',secret(user.password))
        this.fillField('#txt_cpassword',secret(user.password))
        this.click('Sign up')
      }

      return users
    },

    login(i) {

      if(i > users.length || i < 1)
        throw new Error("Selection out of range. Generate new users or select an existing one")

      user = users[i-1];
      this.amOnPage('/login')
      this.fillField('#txt_username', user.username)
      this.fillField('#txt_password',secret(user.password))
      this.click('Login')
    },

    testLogin(){
      let randomUser = new User()

      //login not found account
      this.amOnPage('/login')
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_password',secret(randomUser.password))
      this.click('Login')
      this.waitForText('The user hasn\'t been found')

      //not matching passwords
      this.amOnPage('/register')
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_email',randomUser.email)
      this.fillField('#txt_password',secret(randomUser.password))
      this.fillField('#txt_cpassword',secret(randomUser.wrongPassword))
      this.click('Sign up')
      this.waitForText('The password and the confirmation of the password aren\'t the same')

      //matching passwords
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_email',randomUser.email)
      this.fillField('#txt_password',secret(randomUser.password))
      this.fillField('#txt_cpassword',secret(randomUser.password))
      this.click('Sign up')
      this.dontSeeInCurrentUrl('/register')

      //trying to register again
      this.amOnPage('/register')
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_email',randomUser.email)
      this.fillField('#txt_password',secret(randomUser.password))
      this.fillField('#txt_cpassword',secret(randomUser.password))
      this.click('Sign up')
      this.waitForText('Username is already used')

      //trying to register again, with another username, same email
      this.amOnPage('/register')
      this.fillField('#txt_username', makeRandomString(5))
      this.fillField('#txt_email',randomUser.email)
      this.fillField('#txt_password',secret(randomUser.password))
      this.fillField('#txt_cpassword',secret(randomUser.password))
      this.click('Sign up')
      this.waitForText('Email is already used')

      //login
      this.amOnPage('/login')

      //wrong login
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_password',secret(randomUser.wrongPassword))
      this.click('Login')
      this.waitForText('Check of credentials failed')

      //right login
      this.fillField('#txt_username', randomUser.username)
      this.fillField('#txt_password',secret(randomUser.password))
      this.click('Login')
      this.dontSeeInCurrentUrl('/login')
    },

    generateString(length){
      return makeRandomString(length)
    },

    generateEmail(){
      return makeEmail()
    }

  });
}
