
<%@include file="fragments/header.jsp"%>

<div class="login-form">
    <h1>New Question</h1>

    <form method="POST" action="${pageContext.request.contextPath}/new_question.do">
        <input id="txt_title" name="txt_title" class="form-control classic-form" placeholder="Insert your question" required />
        <input id="txt_description" name="txt_description" class="form-control classic-form" placeholder="Insert your description" required />
        <input id="btn_submit" name="btn_submit" type="submit" class="btn btn-primary classic-button-filled button-full" value="Submit" />
    </form>
</div>



<%@include file="fragments/footer.jsp"%>
