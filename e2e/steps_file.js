// in this file you can append custom step methods to 'I' object

class User {
  constructor(username,email,password,wrongPassword){
     this.username = username;
     this.email = email;
     this.password = password;
     this.wrongPassword = wrongPassword;
 }
}

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

let user1 = new User(makeid(8),makeEmail(),'123','1234')
let user2 = new User(makeid(8),makeEmail(),'333','3333')

module.exports = function() {
  return actor({

    // Define custom steps here, use 'this' to access default methods of I.
    // It is recommended to place a general 'login' function here.
    login: function(user){
      this.amOnPage('/login')
      this.fillField('#txt_username', user.username)
      this.fillField('#txt_password',secret(user.password))
      this.click('Login')
    },

    register: function(user){
      this.amOnPage('/register')
      this.fillField('#txt_username', user.username)
      this.fillField('#txt_email',user.email)
      this.fillField('#txt_password',secret(user.password))
      this.fillField('#txt_cpassword',secret(user.password))
      this.click('Sign up')
    },

  });
}
