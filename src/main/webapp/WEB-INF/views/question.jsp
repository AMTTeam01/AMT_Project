<jsp:useBean id="question" scope="request" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO.QuestionDTO"/>


<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="body-with-navbar">
    <div class="container-fluid">
        ${question.id.asString()}
    </div>
    <div class="container-fluid">
        ${question.description}
    </div>
</div>

<%@include file="fragments/footer.jsp"%>
