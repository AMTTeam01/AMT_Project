<jsp:useBean id="question" scope="request" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO.QuestionDTO"/>


<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="container-fluid body-with-navbar">
    <div class="row">
        <div class="col">
            <div class="row justify-content-center"><i class="fas fa-arrow-alt-circle-up"></i></div>
            <div class="row justify-content-center">${question.ranking}</div>
            <div class="row justify-content-center"><i class="fas fa-arrow-alt-circle-down"></i></div>
        </div>
        <div class="col-10">
            <h4>${question.title}</h4>
            <p class="justify-content-between">${question.description}</p>
        </div>
        <div class="col-sm"></div>
    </div>
    <div class="row">
        <div class="col"></div>
        <div class="col-10">
            <button name="btn_comment" href="#" class="btn btn-primary btn-classic-filled">Comment</button>
            <button name="btn_answer" href="#" class="btn btn-primary btn-classic-filled">Answer</button>
        </div>
        <div class="col-sm"></div>
    </div>
</div>

<%@include file="fragments/footer.jsp"%>