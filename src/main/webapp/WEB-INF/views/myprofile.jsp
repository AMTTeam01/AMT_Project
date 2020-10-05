
<%@include file="fragments/header.jsp"%>
<%@include file="fragments/navigation_bar.jsp"%>

<div class="body-with-navbar">
  <h1>
    <p>Username : <b>${user.username}</b></p>
  </h1>
  <h1>
    <p>Email : <b>${user.email}</b></p>
  </h1>

  <div class="questions-container">
    <h1>
      My Questions :
    </h1>
  </div>
</div>

<%@include file="fragments/footer.jsp"%>
