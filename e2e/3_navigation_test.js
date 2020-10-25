Feature('Navigation')

Scenario('navigation tests', (I) => {

    //login user 1 generated in setup
    I.login(1)

    //home -> browsing
    I.amOnPage('/')
    I.click('Browse questions')
    I.seeInCurrentUrl('/browsing')

    //navbar
    I.click('Profile')
    I.seeInCurrentUrl('/my_profile')
    I.click('New question')
    I.seeInCurrentUrl('new_question')
    I.click('Home')
    I.seeInCurrentUrl('/browsing')

});