<%@include file="fragments/header.jsp" %>
<%@include file="fragments/navigation_bar.jsp" %>

<div class="body-with-navbar">
    <div class="container-fluid">
        <div class="my_profile">
            <div class="row">
                <div class="col-md-5 mr-auto fragment-profile">
                    <div class="txt-username">${user.username}</div>
                    <div class="txt-email mb-3">${user.email}</div>
                    <div class="my-questions">
                        <div class="title">My Questions</div>
                        <%@include file="fragments/questions.jsp" %>
                    </div>
                </div>
                <div class="col-md-6 fragment-profile">
                    <c:forEach var="error" items="${errors}">
                        <div id="error" class="col-md-12 p-2 mt-2 bg-danger text-white">${error}</div>
                    </c:forEach>
                    <form method="POST" action="${pageContext.request.contextPath}/edit_profile.do">
                        <input id="txt_username" name="txt_username" class="form-control classic-form"
                               placeholder="Insert your username"/>
                        <input id="txt_email" name="txt_email" type="email" class="form-control classic-form"
                               placeholder="Insert your e-mail"/>
                        <input id="txt_password" name="txt_password" class="form-control classic-form" type="password"
                               placeholder="Insert your password"/>
                        <input id="txt_cpassword" name="txt_cpassword" class="form-control classic-form" type="password"
                               placeholder="Confirm your password"/>
                        <input id="btn_submit" name="btn_submit" type="submit"
                               class="btn btn-primary btn-classic-filled btn-full" value="Edit"/>
                    </form>
                </div>
            </div>
            <div class="row badge-container">
                <div class="fragment-profile mr-auto col mt-5">
                    <div id="badge-title" class="title">Badges</div>
                    <hr class="badge-h-divider"/>
                    <div class="row">
                        <div class="col right-border pl-5 pr-5">
                            <div class="text-center gold title">Gold</div>
                            <ul class="list-group list-group-flush">
                                <!-- TODO: Bind with badge retrieved -->
                                <c:forEach begin="0" end="5" varStatus="loop">
                                    <jsp:include page="fragments/badge_item.jsp">
                                        <jsp:param name="name" value="Name"/>
                                        <jsp:param name="date" value="13.01.2020"/>
                                    </jsp:include>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col right-border pl-5 pr-5">
                            <div class="text-center silver title">Silver</div>
                            <ul class="list-group list-group-flush">
                                <!-- TODO: Bind with badge retrieved -->
                                <c:forEach begin="0" end="5" varStatus="loop">
                                    <jsp:include page="fragments/badge_item.jsp">
                                        <jsp:param name="name" value="Name"/>
                                        <jsp:param name="date" value="13.01.2020"/>
                                    </jsp:include>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col pl-5 pr-5">
                            <div class="text-center bronze title">Bronze</div>
                            <ul class="list-group list-group-flush">
                                <!-- TODO: Bind with badge retrieved -->
                                <c:forEach begin="0" end="5" varStatus="loop">
                                    <jsp:include page="fragments/badge_item.jsp">
                                        <jsp:param name="name" value="Name"/>
                                        <jsp:param name="date" value="13.01.2020"/>
                                    </jsp:include>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
