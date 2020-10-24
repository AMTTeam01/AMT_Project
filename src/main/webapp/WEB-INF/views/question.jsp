<jsp:useBean id="question" scope="request" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO.QuestionDTO"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp" %>

<script src="../../assets/js/Utils.js"></script>

<div class="container-fluid body-with-navbar col-8">
    <h1>${question.title}</h1>
    <div class="row mt-5 mb-5" id="question">
        <%@include file="fragments/vote.jsp" %>
        <div class="col-10" id="question_description">
            <p class="justify-content-between">${question.description}</p>
        </div>
    </div>
    <div class="ml-5" id="question_comments_container">
        <c:forEach var="comment" items="${question.commentsDTO.comments}">
            <fmt:formatDate value="${comment.creationDate}" pattern="dd.MM.yyyy HH:mm" var="strDate"/>
            <jsp:include page="fragments/comments.jsp">
                <jsp:param name="description" value="${comment.description}"/>
                <jsp:param name="username" value="${comment.username}"/>
                <jsp:param name="creationDate" value="${strDate}"/>
            </jsp:include>
        </c:forEach>
    </div>
    <div id="add_question_comment_container">
        <button name="btn_comment" id="btn_comment" href="#" class="btn btn-primary btn-classic-filled" onclick="toggleVisibility('add-comment-container')">
            Comment
        </button>
        <div id="add-comment-container" class="m-2" style="display: none">
            <form method="POST" action="${pageContext.request.contextPath}/comment_question.do">
                <input type="hidden" id="comment_question_id" name="comment_question_id" value="${question.id.asString()}">
                <label for="txt_question_comment">Your comment</label>
                <textarea class="form-control" id="txt_question_comment" name="txt_question_comment" rows="3" placeholder="Your comment"
                          required></textarea>
                <div class="d-flex flex-row-reverse">
                    <button id="bnt_submit_question_comment" name="bnt_submit_question_comment" class="btn btn-primary btn-classic-filled mt-2"
                            type="submit">Comment
                    </button>
                </div>
            </form>
        </div>
    </div>
    <hr class="solid">

    <div class="row" id="answer_container">
        <c:import url="fragments/answer.jsp"/>
    </div>

    <div id="add_answer_container">
        <div class="m-2">
            <form method="POST" action="${pageContext.request.contextPath}/answer.do">
                <label for="txt_answer">Your answer</label>
                <input type="hidden" id="answer_question_id" name="answer_question_id" value="${question.id.asString()}">
                <textarea class="form-control" id="txt_answer" name="txt_answer" rows="3" placeholder="Your answer" required></textarea>
                <button id="bnt_submit_answer" name="bnt_submit_answer" class="btn btn-primary btn-classic-filled mt-2 float-right" type="submit">Answer
                </button>
            </form>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
