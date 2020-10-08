<%@include file="fragments/header.jsp" %>

<button onclick="history.back()">Back to Previous Page</button>
<h1>404 Page Not Found.</h1>
<br />
    <p><b>Error code:</b> ${pageContext.errorData.statusCode}</p>
    <p><b>Request URI:</b> ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
<br />

<%@include file="fragments/footer.jsp" %>