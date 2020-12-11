Feature('edit_account');

Scenario('test edit account', ({ I }) => {

    let newUsername = I.generateString(10)
    let newEmail = I.generateEmail()

    //create user
    I.registerUsers(1)
    I.login(1)

    I.amOnPage('/my_profile')
    I.dontSee(newUsername)
    I.dontSee(newEmail)
    I.fillField('#txt_username',newUsername)
    I.fillField('#txt_email',newEmail)
    I.click('Edit')
    I.see(newUsername)
    I.see(newEmail)
});
