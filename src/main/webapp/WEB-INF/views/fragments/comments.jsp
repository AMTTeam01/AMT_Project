<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="comment container-fluid m-5" id="comment">
    <p class="justify-content-between text" id="text">
        ${param.description}
    </p>
    <span id="comment_bottom">
    <span class="p-2" id="comment_author">Author ${param.username}</span>
    <span class="p-2" id="comment_date">Creation Date : ${param.creationDate} </span>
    </span>
</div>
