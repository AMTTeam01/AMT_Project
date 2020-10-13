
<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="container-fluid body-with-navbar">
    <div class="col-md-6 edit-profile-form mx-auto">
        <h1>Edit profile</h1>
        <form method="POST" action="${pageContext.request.contextPath}/edit_profile.do">
            <input id="txt_username" name="txt_username" class="form-control classic-form" placeholder="Insert your username" />
            <input id="txt_email" name="txt_email" type="email" class="form-control classic-form" placeholder="Insert your e-mail" />
            <input id="txt_password" name="txt_password" class="form-control classic-form" type="password" placeholder="Insert your password" />
            <input id="txt_cpassword" name="txt_cpassword" class="form-control classic-form" type="password" placeholder="Confirm your password" />
            <input id="btn_submit" name="btn_submit" type="submit" class="btn btn-primary btn-classic-filled btn-full" value="Edit" />
        </form>
    </div>
</div>



<%@include file="fragments/footer.jsp"%>
