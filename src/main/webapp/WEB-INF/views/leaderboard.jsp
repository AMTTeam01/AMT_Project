<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp" %>

<div class="body-with-navbar container-fluid">
    <div class="col-md-6 mx-auto leaderboard">
        <h1 class="mb-5">Leaderboard</h1>
        <div>
            <!-- For each point scale we display the top 10 -->
            <c:forEach var="pointscale" items="${pointscales}" varStatus="loop">
                <div class="row header mt-4">
                    <div class="col-1 "></div>
                    <div class="col font-weight-bold">Username</div>
                    <div class="col font-weight-bold">Total points</div>
                </div>
                <hr/>
                <div class="row pb-2">${pointscale.name}</div>
                <c:forEach var="user" items="${pointscale.leaderboard}" varStatus="loop">
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
                </c:forEach>
            </c:forEach>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
