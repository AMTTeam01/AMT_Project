<div class="col">
    <a href="${pageContext.request.contextPath}/${param.servlet}?vote=upvote&${param.extras}" name="btn_up" class="row justify-content-center"><i class="fas fa-arrow-alt-circle-up"></i></a>
    <div class="row justify-content-center">${param.votes}</div>
    <a href="${pageContext.request.contextPath}/${param.servlet}?vote=downvote&${param.extras}" name="btn_down" class="row justify-content-center"><i class="fas fa-arrow-alt-circle-down"></i></a>
</div>