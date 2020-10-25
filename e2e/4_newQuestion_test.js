Feature('New question')

Scenario('adding question', (I) => {

    let questionTitle = 'What is Lorem Ipsum?'
    let questionDescription = 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.'

    //login user 1 generated in setup
    I.login(1)

    //add question
    I.amOnPage('/new_question')
    I.fillField('#txt_title',questionTitle)
    I.fillField('#txt_description',questionDescription)
    I.click('Submit')

    //see my question
    I.seeInCurrentUrl('/browsing')
    I.waitForText('Browsing')
    I.waitForText(questionTitle)
    I.click(questionTitle)
    I.seeInCurrentUrl('/question?id=')
    I.waitForText(questionTitle)
    I.waitForText(questionDescription)

});
