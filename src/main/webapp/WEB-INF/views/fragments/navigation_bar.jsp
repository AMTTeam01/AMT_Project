<header>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand " href="#"><img class="logo" src="${pageContext.request.contextPath}/assets/imgs/logo.png" alt="C'est le logo normalement" /></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/my_profile"><i class="fas fa-user"></i> Profile</a>
                </li>
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/browsing"><i class="fas fa-home"></i> Home</a>
                </li>
                <li class="nav-item nav-item-cell">
                    <a class="nav-link" href="${pageContext.request.contextPath}/new_question"><i class="fas fa-question"></i> New question</a>
                </li>
            </ul>
            <form class="form-inline mt-2 mt-md-0">
                <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
                <button class="btn btn-search my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </nav>
</header>