<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="comment container-fluid p-0">
    <p class="justify-content-between text">
        ${param.description}
    </p>
    <div class="comment_footer d-flex flex-row-reverse">
    <span class="p-2" id="comment_author">Author ${param.username}</span>
    <span class="p-2" id="comment_date">Creation Date : ${param.creationDate} </span>
    </div>
</div>
