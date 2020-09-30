
<%@include file="fragments/header.jsp"%>

<div class="login-form">
    <h1>New Question</h1>

    <form method="POST" action="${pageContext.request.contextPath}/new_question.do">
        <input id="question_title" name="title" class="form-control classic-form" placeholder="Insert your question" required />
        <input id="question_description" name="description" class="form-control classic-form" placeholder="Insert your description" required />
        <input type="submit" class="btn btn-primary classic-button-filled button-full" value="Submit" />
    </form>
</div>



<%@include file="fragments/footer.jsp"%>
