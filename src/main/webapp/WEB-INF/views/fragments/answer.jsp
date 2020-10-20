<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="answer" items="${requestScope.question.answers}">
<div class="container-fluid m-5">
    <div class="row" id="answer_block">
        <div class="col-1" id="answer_vote">
            <%@include file="vote.jsp" %>
        </div>
        <div class="col-9" id="answer_detail">
            <p class="justify-content-between">
                ${answer.description}
            </p>
            <span id="answer_bottom">
            <span class="p-2">Author ${answer.username}</span>
            <span class="p-2">Creation Date : <fmt:formatDate value="${answer.creationDate}" pattern="dd.MM.yyyy HH:mm"/> </span>
            </span>
        </div>
    </div>
</div>
</c:forEach>

