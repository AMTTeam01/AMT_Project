<%@include file="fragments/header.jsp"%>


<div class="split left">
    <div class="left-home-page">
        <h1>${home}</h1>
        <button class="btn btn-primary classic-button">Sign up</button>
        <a class="btn btn-primary classic-button" href="login">Login</a>
    </div>
</div>

<div class="split right">
    <img src="${pageContext.request.contextPath}/assets/imgs/background.jpg" class="img">
</div>


<%@include file="fragments/footer.jsp"%>

