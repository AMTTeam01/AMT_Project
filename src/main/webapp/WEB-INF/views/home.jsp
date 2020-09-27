
<%@include file="fragments/header.jsp"%>
<body>
    <div class="split left">
        <div class="left-home-page">
            <h1>Question about programming ?</h1>
            <a id="home_signUp" class="btn btn-primary classic-button" href="${pageContext.request.contextPath}/register">Sign up</a>
            <a id="home_login" class="btn btn-primary classic-button" href="${pageContext.request.contextPath}/login">Login</a>
        </div>
    </div>

    <div class="split right">
        <img src="${pageContext.request.contextPath}/assets/imgs/background.jpg" class="img">
    </div>

<%@include file="fragments/footer.jsp"%>
