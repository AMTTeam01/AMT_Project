<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp"%>


<div class="container body-with-navbar">
    <div class="row justify-content-between px-3">
        <h1>Browsing</h1>
        <form method="GET" action="${pageContext.request.contextPath}/browsing${pageContext.request.getAttribute("txt_search")}" class="form-inline mt-2 mt-md-0">
            <input id="txt_search" name="txt_search" class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
            <button class="btn btn-search my-2 my-sm-0" type="submit">Search</button>
        </form>
    </div>
    <%@include file="fragments/questions.jsp"%>
</div>


<%@include file="fragments/footer.jsp" %>
