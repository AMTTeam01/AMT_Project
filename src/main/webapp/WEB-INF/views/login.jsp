
<%@include file="fragments/header.jsp"%>

<div class="login-form">
    <div class="mx-auto" style="width:400px">
        <img src="${pageContext.request.contextPath}/assets/imgs/logo.png" />
    </div>
    <form method="POST" action="${pageContext.request.contextPath}/request.login">
        <input name="username" class="form-control classic-form" placeholder="Insert your username" required />
        <input name="password" class="form-control classic-form" type="password" placeholder="Insert your password" required />
        <button type="submit" class="btn btn-primary classic-button-filled button-full">Login</button>
    </form>
</div>

<%@include file="fragments/footer.jsp"%>
