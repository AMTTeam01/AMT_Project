
<%@include file="fragments/header.jsp"%>
<body>
    <div class="home-container">
        <div class="split-home left">
            <div class="left-home-page">
                <div class="col-md-12 title mb-5">Question about programming ?</div>
                <div class="col-md-12 mb-5">
                    <a id="home_browsing" class="btn btn-primary classic-button btn-wide btn-browse" href="${pageContext.request.contextPath}/browsing">Browse the questions</a>
                </div>
                <div class="col-md-12">
                    <a id="home_signUp" class="classic-link mr-5" href="${pageContext.request.contextPath}/register">Sign up</a>
                    <a id="home_login" class="classic-link" href="${pageContext.request.contextPath}/login">Login</a>
                </div>
            </div>
        </div>

        <div class="split-home right">
            <img src="${pageContext.request.contextPath}/assets/imgs/background.jpg" class="img">
        </div>
    </div>

<%@include file="fragments/footer.jsp"%>

