
<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="container-fluid body-with-navbar">
    <div class="col-md-6 new-question-form mx-auto">
        <h1>New Question</h1>
        <form method="POST" action="${pageContext.request.contextPath}/new_question.do">
            <input id="txt_title" name="txt_title" class="form-control classic-form" placeholder="Insert your question" required />
            <textarea id="txt_description" name="txt_description" class="form-control classic-form bigger-text-area" placeholder="Insert your description" required></textarea>
            <input id="btn_submit" name="btn_submit" type="submit" class="btn btn-primary btn-classic-filled btn-full" value="Submit" />
        </form>
    </div>
</div>



<%@include file="fragments/footer.jsp"%>
