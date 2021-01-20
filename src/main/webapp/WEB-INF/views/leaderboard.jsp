<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp" %>

<div class="body-with-navbar container-fluid">
    <div class="col-md-6 mx-auto leaderboard">
        <h1 class="mb-5">Leaderboard</h1>
        <div>
            <div class="row header">
                <div class="col-1 "></div>
                <div class="col font-weight-bold">Username</div>
                <div class="col font-weight-bold">Total points</div>
            </div>
            <hr/>

            <c:forEach var="user" items="${users}" varStatus="loop">
                <div class="row pb-2">
                    <div class="col-1">
                        <c:choose>
                            <c:when test="${loop.index == 0}"> <i class="fas fa-trophy gold"></i> </c:when>
                            <c:when test="${loop.index == 1}"> <i class="fas fa-trophy silver"></i> </c:when>
                            <c:when test="${loop.index == 2}"> <i class="fas fa-trophy bronze"></i> </c:when>
                        </c:choose>
                    </div>
                    <div class="col">${user.username}</div>
                    <div class="col">${user.points} pts</div>
                </div>

                <div class="list-group-item list-group-item-classic">
                    <a class="classic-link" href="${pageContext.request.contextPath}/question?id=${question.id.asString()}" id="link_question_url">
                        <div class="d-flex">
                            <h4 class="p-2 mr-auto">${question.title}</h4>
                            <div>
                                <c:forEach begin="1" end="${question.votes}">
                                    <div class="glyphicon glyphicon-star" style="color: orange"></div>
                                </c:forEach>
                                <c:forEach begin="1" end="${5 - question.votes}">
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

            <c:forEach begin="0" end="5" varStatus="loop">
                <div class="row pb-2">
                    <div class="col-1">
                        <c:choose>
                            <c:when test="${loop.index == 0}"> <i class="fas fa-trophy gold"></i> </c:when>
                            <c:when test="${loop.index == 1}"> <i class="fas fa-trophy silver"></i> </c:when>
                            <c:when test="${loop.index == 2}"> <i class="fas fa-trophy bronze"></i> </c:when>
                        </c:choose>
                    </div>
                    <div class="col">Username</div>
                    <div class="col">321 pts</div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
