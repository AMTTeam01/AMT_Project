<div class="col">
    <a href="${pageContext.request.contextPath}/vote?vote=upvote&id=${question.id.asString()}" name="btn_up" class="row justify-content-center"><i class="fas fa-arrow-alt-circle-up"></i></a>
    <div class="row justify-content-center">${param.votes}</div>
    <a href="${pageContext.request.contextPath}/vote?vote=downvote&id=${question.id.asString()}" name="btn_up" class="row justify-content-center"><i class="fas fa-arrow-alt-circle-down"></i></a>
</div>