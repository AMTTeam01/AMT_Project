Feature('comments_answers');

Scenario('test comments and answers', (I) => {

    let questionTitle = I.generateString(10)
    let questionDescription = I.generateString(30)
    let comment = I.generateString(20)
    let answer = I.generateString(20)
    let commentOnAnswer = I.generateString(20)

    //login a generated user
    I.registerUsers(1)
    I.login(1)

    //add question
    I.amOnPage('/new_question')
    I.fillField('#txt_title',questionTitle)
    I.fillField('#txt_description',questionDescription)
    I.click('Submit')
    I.click(questionTitle)

    //question page
    I.click('Comment')
    I.fillField('#txt_question_comment',comment)
    I.click('#bnt_submit_question_comment')
    I.see(comment)

    I.fillField('txt_answer',answer)
    I.click('Answer')
    I.see(answer)

    I.click('#add_comment_answer_toggleVisible')
    I.waitForText('Your comment')
    I.fillField('txt_question_comment',commentOnAnswer)
    I.click('Comment this answer')

});
