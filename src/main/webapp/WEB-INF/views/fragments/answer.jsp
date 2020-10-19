<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid m-5">
    <div class="row" id="answer_block">
        <div class="col-1" id="answer_vote">
            <%@include file="vote.jsp" %>
        </div>
        <div class="col-9" id="answer_detail">
            <p class="justify-content-between">
                <%= request.getParameter("answers") %>
            </p>
            <span id="answer_bottom">
            <span class="p-2">Author <%=request.getParameter("username") %></span>
            <span class="p-2">Creation Date : <%=request.getParameter("creation_date") %> </span>
            </span>
        </div>
    </div>
</div>



