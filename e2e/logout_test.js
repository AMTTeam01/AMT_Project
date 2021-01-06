Feature('logout')

Scenario('A unconnected user can\'t not see the profile button and had a login button on the nav bar' , (I) => {
    I.amOnPage('/browsing')

    I.dontSeeElement('#profile')
    I.seeElement('#login')
});


Scenario('A connected user can the profile button and had a logout button on the nav bar' , (I) => {
    I.registerUsers(1)
    I.login(1)

    I.amOnPage('/browsing')

    I.seeElement('#profile')
    I.seeElement('#logout')
});

Scenario('A connected user can logout' , (I) => {
    I.registerUsers(1)
    I.login(1)
    I.amOnPage('/browsing')
    I.click('#logout')
    I.amOnPage('/browsing')
    I.dontSeeElement('#profile')
});

Scenario('When the user logout, redirect to home' , (I) => {
    I.registerUsers(1)
    I.login(1)
    I.amOnPage('/browsing')
    I.click('#logout')
    I.seeInCurrentUrl('/')
});
