<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="fragments/header.jsp" %>


<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO"/>


<div class="container">

    <h1>Browsing</h1>

    <ul class="list-group">
        <c:forEach var="question" items="${questions.questions}">
         <div class="list-group-item">
            <a href="#" >
                <div class="d-flex">
                    <h4 class="p-2 mr-auto ">${question.title}</h4>
                    <div>
                        <c:forEach begin="1" end="${question.ranking}">
                            <div class="glyphicon glyphicon-star" style="color: orange"></div>
                        </c:forEach>
                        <c:forEach begin="1" end="${5 - question.ranking}">
                            <div class="glyphicon glyphicon-star-empty" style="color: orange"></div>
                        </c:forEach>
                    </div>
                </div>
                <div>
                    <c:forEach var="tag" items="${question.tags}">
                        <a href="#" class="badge badge-primary">${tag}</a>
                    </c:forEach>
                </div>
            </a>
         </div>
        </c:forEach>
    </ul>
</div>


<%@include file="fragments/footer.jsp" %>
