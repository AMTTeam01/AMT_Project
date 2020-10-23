<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp" %>

<div class="body-with-navbar">
    <div class="container-fluid">
        <div class="my_profile">
            <div class="row">
                <div class="col-md-5 mr-auto fragment-profile">
                    <div class="txt-username">${user.username}</div>
                    <div class="txt-email mb-3">${user.email}</div>
                    <div class="my-questions">
                        <div class="title">My Questions</div>
                        <%@include file="fragments/questions.jsp" %>
                    </div>
                </div>
                <div class="col-md-6 fragment-profile">
                    <c:forEach var="error" items="${errors}">
                        <div class="col-md-12 p-2 mt-2 bg-danger text-white">${error}</div>
                    </c:forEach>
                    <form method="POST" action="${pageContext.request.contextPath}/edit_profile.do">
                        <input id="txt_username" name="txt_username" class="form-control classic-form"
                               placeholder="Insert your username"/>
                        <input id="txt_email" name="txt_email" type="email" class="form-control classic-form"
                               placeholder="Insert your e-mail"/>
                        <input id="txt_password" name="txt_password" class="form-control classic-form" type="password"
                               placeholder="Insert your password"/>
                        <input id="txt_cpassword" name="txt_cpassword" class="form-control classic-form" type="password"
                               placeholder="Confirm your password"/>
                        <input id="btn_submit" name="btn_submit" type="submit"
                               class="btn btn-primary btn-classic-filled btn-full" value="Edit"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
