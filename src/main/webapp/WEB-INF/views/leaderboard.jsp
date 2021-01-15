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
