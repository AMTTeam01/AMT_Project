<header>
    <!-- Navigation -->
    <nav class="navbar navbar-expand navbar-dark bg-dark">
        <a class="navbar-left" href="#">
            <img src="${pageContext.request.contextPath}/assets/imgs/logo.png" alt="Logo" style="width:150px;"/>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExample02" aria-controls="navbarsExample02" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse d-flex" id="navbarsExample02">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#" style="border-right: 1px solid orange; padding-right: 10px; padding-left: 10px">Home <span class="sr-only">
                        (current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/new_question" style="border-right: 1px solid orange; padding-right: 10px; padding-left: 10px" >New Question </a>
                </li>
            </ul>
            <form class="form-inline my-2 my-md-0">
                <input class="form-control" type="text" placeholder="Search" />
            </form>
        </div>
    </nav>
</header>