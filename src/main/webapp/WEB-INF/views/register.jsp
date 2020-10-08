<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="fragments/header.jsp"%>

<div class="login-form">
    <div class="mx-auto" style="width:400px">
        <a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/assets/imgs/logo.png" /></a>
    </div>
    <c:forEach var="error" items="${errors}">
        <div class="col-md-12 p-2 mt-2 bg-danger text-white">${error}</div>
    </c:forEach>
    <form method="POST" action="${pageContext.request.contextPath}/register.do">
        <input id="txt_username" name="txt_username" class="form-control classic-form" placeholder="Insert your username" required />
        <input id="txt_email" name="txt_email" type="email" class="form-control classic-form" placeholder="Insert your e-mail" required />
        <input id="txt_password" name="txt_password" class="form-control classic-form" type="password" placeholder="Insert your password" required />
        <input id="txt_cpassword" name="txt_cpassword" class="form-control classic-form" type="password" placeholder="Confirm your password" required />
        <button type="submit" class="btn btn-primary btn-classic-filled btn-full">Sign up</button>
    </form>
</div>

<%@include file="fragments/footer.jsp"%>
