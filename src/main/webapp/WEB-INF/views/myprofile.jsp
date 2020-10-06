<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="body-with-navbar">
  <div class="container-fluid">
    <div class="row">
    <div class="col-md-6">
      <div class="txt-username">Username : ${user.username}</div>
      <div class="txt-email">Email : ${user.email}</div>
      <a class="btn btn-primary btn-classic-filled" href="${pageContext.request.contextPath}/edit_profile"><i class="fas fa-pen"></i> EDIT</a>
    </div>
    <div class="col-md-6">
      <h1>
        My Questions
      </h1>
      <%@include file="fragments/questions.jsp"%>
    </div>
    </div>
  </div>
</div>

<%@include file="fragments/footer.jsp"%>
