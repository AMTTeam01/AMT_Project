<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="fragments/header.jsp" %>

<%--
<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO"/>
--%>

<div class="container">

    <h1>Browsing</h1>

    <ul class="list-group">
        <c:forEach var="question" items="${questions}" >
            <a href="#" class="list-group-item no">
                <div class="d-flex">
                    <div class="p-2 flex-grow-1 bd-highlight">${question.title}</div>
                    <div class="p-2 bd-highlight">Ranking : ${question.ranking}</div>
                </div>
            </a>
        </c:forEach>
    </ul>
</div>


<%@include file="fragments/footer.jsp" %>
