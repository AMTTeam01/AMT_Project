<header>
    <!-- Navigation -->
    <nav class="navbar navbar-expand navbar-dark">
        <a class="navbar-left" href="#">
            <img src="${pageContext.request.contextPath}/assets/imgs/logo.png" alt="Logo" style="width:150px;"/>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExample02" aria-controls="navbarsExample02" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse d-flex" id="navbarsExample02">
            <ul class="navbar-nav">
                <li class="nav-item active">
                    <a class="nav-link pr-4 pl-4 nav-item-cell" href="#">Home <span
                            class="sr-only">
                        (current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link pr-4 pl-4 nav-item-cell" href="${pageContext.request.contextPath}/new_question">New Question </a>
                </li>
            </ul>
            <form class="form-inline pr-4 pl-4">
                <input class="form-control" type="text" placeholder="Search"/>
            </form>
        </div>
    </nav>
</header>