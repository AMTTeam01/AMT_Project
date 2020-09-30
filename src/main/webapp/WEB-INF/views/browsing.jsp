<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp"%>

<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO"/>


<div class="container">

    <h1>Browsing</h1>

    <ul class="list-group">
        <c:forEach var="question" items="${questions.questions}">
            <div class="list-group-item">
                <a href="#">
                    <div class="d-flex">
                        <div class="p-2 mr-auto">${question.title}</div>
                        <div class="p-2">Ranking : ${question.ranking}</div>
                    </div>
                </a>
                <div>
                    <c:forEach var="tag" items="${question.tags}">
                        <a href="#" class="badge">${tag}</a>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </ul>
</div>


<%@include file="fragments/footer.jsp" %>
