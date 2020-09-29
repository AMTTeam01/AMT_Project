<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="fragments/header.jsp" %>

<div class="container">

    <h1>Browsing</h1>

    <ul class="list-group">
        <c:forEach var="item" items="${questions}" >
            <a href="#" class="list-group-item no">
                <div class="d-flex">
                    <div class="p-2 flex-grow-1 bd-highlight">${item.question}</div>
                    <div class="p-2 bd-highlight">${item.date}</div>
                    <div class="p-2 bd-highlight">Ranking : ${item.ranking}</div>
                </div>
            </a>
        </c:forEach>
    </ul>
</div>


<%@include file="fragments/footer.jsp" %>
