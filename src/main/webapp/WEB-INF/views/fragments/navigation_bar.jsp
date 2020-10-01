<header>
    <!-- Navigation -->
    <nav class="navbar navbar-dark">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">
                    <img src="${pageContext.request.contextPath}/assets/imgs/logo.png" alt="Logo" style="width:150px;"/>
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="#">Home</a>
                    </li>
                    <li class="dropdown">
                        <a href="${pageContext.request.contextPath}/new_question">New Question</a>
                    </li>
                </ul>
                <form class="navbar-form navbar-left">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    <%--<nav class="navbar navbar-expand navbar-dark">
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
    </nav>--%>
</header>