<jsp:useBean id="answers" scope="request" type="ch.heigvd.amt.mvcProject.application.answer.AnswersDTO"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid body-with-navbar">
        <div class="row">
            <%@include file="vote.jsp" %>
            <div class="col-10">
                <p class="justify-content-between">
                    <%= request.getParameter("answers") %>
                </p>
            </div>
            <div class="col-sm">
                <span class="p-2">Author</span>
                <span class="p-2">Creation Date</span>
            </div>
        </div>
</div>



