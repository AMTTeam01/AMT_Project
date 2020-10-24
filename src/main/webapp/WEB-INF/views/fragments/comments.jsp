<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="comment container-fluid m-5">
    <p class="justify-content-between text">
        ${param.description}
    </p>
    <div class="comment_footer float-right">
    <span class="p-2" id="comment_author">Author ${param.username}</span>
    <span class="p-2" id="comment_date">Creation Date : ${param.creationDate} </span>
    </div>
</div>
