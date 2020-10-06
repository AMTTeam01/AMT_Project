<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="body-with-navbar">
  <h1 class="test">Username : ${user.username}</h1>
  <h1>Email : ${user.email}</h1>
  <h1><a href="${pageContext.request.contextPath}/edit_profile"><i class="fas fa-pen"></i> EDIT</a></h1>

  <div class="questions-container">
    <h1>
      My Questions :
    </h1>
    <%@include file="fragments/questions.jsp"%>
  </div>
</div>

<%@include file="fragments/footer.jsp"%>
