<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="answer" items="${requestScope.question.answersDTO.answers}">
    <div class="container-fluid mb-3 mt-3 answer_container">
        <div class="row" id="answer_block">
            <div class="col-1" id="answer_vote">
                <%@include file="vote.jsp" %>
            </div>
            <div class="col" id="answer_detail">
                <p class="justify-content-between">
                        ${answer.description}
                </p>
                <div class="answer_footer d-flex flex-row-reverse">
                    <span class="p-2">Creation Date : <fmt:formatDate value="${answer.creationDate}" pattern="dd.MM.yyyy HH:mm"/> </span>
                    <span class="p-2">Author ${answer.username}</span>
                    <a href="javascript:void(0)" class="p-2 btn-link" onclick="toggleVisibility('comment-container-${answer.id}' )">Add comment</a>
                </div>
                <div id="comment-container-${answer.id}" class="m-2 comment-container" style="display: none">
                    <form method="POST" action="${pageContext.request.contextPath}/comment_question.do">
                        <input type="hidden" id="comment_question_id" name="comment_question_id" value="${question.id.asString()}">
                        <label for="txt_question_comment">Your comment</label>
                        <textarea class="form-control" id="txt_question_comment" name="txt_question_comment" rows="3" placeholder="Your comment"
                                  required></textarea>
                        <div class="d-flex flex-row-reverse">
                            <button id="bnt_submit_question_comment" name="bnt_submit_question_comment" class="btn btn-primary btn-classic-filled mt-2"
                                    type="submit">Comment
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</c:forEach>

