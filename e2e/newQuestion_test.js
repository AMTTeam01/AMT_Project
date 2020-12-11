Feature('New question')

Scenario('adding question', (I) => {

    let questionTitle = I.generateString(10)
    let questionDescription = I.generateString(100)

    //login a generated user
    I.registerUsers(2)
    I.login(1)

    //add question
    I.amOnPage('/new_question')
    I.fillField('#txt_title', questionTitle)
    I.fillField('#txt_description', questionDescription)
    I.click('Submit')

    //see my question
    I.seeInCurrentUrl('/browsing')
    I.waitForText('Browsing')
    I.waitForText(questionTitle)
    I.click(questionTitle)
    I.seeInCurrentUrl('/question?id=')
    I.waitForText(questionTitle)
    I.waitForText(questionDescription)

    //see question on my profile
    I.amOnPage('/my_profile')
    I.see(questionTitle)

    //login with other user
    I.login(2)
    I.amOnPage('/my_profile')
    I.dontSee(questionTitle)

});
