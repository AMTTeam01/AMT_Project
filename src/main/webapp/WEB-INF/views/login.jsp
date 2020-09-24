
<%@include file="fragments/header.jsp"%>

<div class="login-form">
    <div class="mx-auto" style="width:400px">
        <img src="${pageContext.request.contextPath}/assets/imgs/logo.png" />
    </div>
    <%
        String error = request.getParameter("error");
        if(error != null) {
    %>
    <div class="col-md-12 p-2 mt-2 bg-danger text-white"><%=error%></div>
    <% } %>
    <form method="POST" action="${pageContext.request.contextPath}/request.login">
        <input name="username" class="form-control classic-form" placeholder="Insert your username" required />
        <input name="password" class="form-control classic-form" type="password" placeholder="Insert your password" required />
        <button type="submit" class="btn btn-primary classic-button-filled button-full">Login</button>
    </form>
</div>

<%@include file="fragments/footer.jsp"%>
