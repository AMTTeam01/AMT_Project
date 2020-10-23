<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp"%>


<div class="container body-with-navbar">
    <div class="row justify-content-between px-3">
        <h1>Browsing</h1>
        <form class="form-inline mt-2 mt-md-0">
            <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
            <button class="btn btn-search my-2 my-sm-0" type="submit">Search</button>
        </form>
    </div>
    <%@include file="fragments/questions.jsp"%>
</div>


<%@include file="fragments/footer.jsp" %>
