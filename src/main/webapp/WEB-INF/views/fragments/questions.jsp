
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.mvcProject.application.question.QuestionsDTO"/>

<ul class="list-group" id="list-item_question">
    <c:forEach var="question" items="${questions.questions}">
        <div class="list-group-item">
            <a href="#" id="link_question_url">
                <div class="d-flex">
                    <h4 class="p-2 mr-auto">${question.title}</h4>
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
                        <a href="#" class="badge badge-primary" id="link_question_tag">${tag}</a>
                    </c:forEach>
                </div>
            </a>
        </div>
    </c:forEach>
</ul>