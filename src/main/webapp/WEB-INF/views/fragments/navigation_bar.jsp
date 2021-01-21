<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand " href="${pageContext.request.contextPath}/browsing"><img class="logo"
                                                                                         src="${pageContext.request.contextPath}/assets/imgs/logo.png"
                                                                                         alt="C'est le logo normalement"/></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/browsing"><i class="fas fa-home"></i> Home</a>
                </li>
                <c:out value=""/>
                <c:if test="${not empty cookie['username'].value}">
                    <li class="nav-item nav-item-cell">
                        <a class="nav-link" href="${pageContext.request.contextPath}/my_profile" id="profile"><i class="fas fa-user"></i> Profile</a>
                    </li>
                </c:if>
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/new_question"><i class="fas fa-question"></i> New question</a>
                </li>
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/leaderboard"><i class="fas fa-trophy"></i> Leaderboard</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${not empty cookie['username'].value}">
                        <li class="nav-item nav-item-cell">
                            <form method="post" action="${pageContext.request.contextPath}/logout.do">
                                <button class="btn btn-link nav-link" type="submit" id="logout"><i class="fas fa-sign-out-alt"></i> Logout</button>
                            </form>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item nav-item-cell">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login" id="login"><i class="fas fa-sign-in-alt"></i> Login</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>

    </nav>
</header>