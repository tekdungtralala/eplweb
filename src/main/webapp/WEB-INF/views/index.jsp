<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<!-- saved from url=(0065)http://ironsummitmedia.github.io/startbootstrap-1-col-portfolio/# -->
<html lang="en">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>epl web app</title>

    <!-- Bootstrap Core CSS -->
    <link href="<c:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 70px; /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
        }

        footer {
            margin: 50px 0;
        }
    </style>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>
    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">
                    English Premier League
                </a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="navbar-collapse collapse" id="bs-example-navbar-collapse-1" aria-expanded="false" style="height: 1px;">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="#">Tables</a>
                    </li>
                    <li>
                        <a href="#">Matchday</a>
                    </li>
                    <li>
                        <a href="#">Results</a>
                    </li>
                    <li>
                        <a href="#">Team of The Week</a>
                    </li>
                    <li>
                        <a href="#">News</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Page Content -->
    <div class="container">

        <!-- Page Heading -->
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header title">
                    <a href="#">
                        Tables
                    </a>
                    <small></small>
                </h1>
            </div>
        </div>
        <!-- /.row -->

        <!-- Project One -->
        <div class="row">
            <div class="col-md-7">
                <table class="table table-striped table-bordered table-hover" 
                    id="dataTables-example">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Team</th>
                            <th>GP</th>
                            <th>W</th>
                            <th>D</th>
                            <th>L</th>
                            <th>GD</th>
                            <th>PTS</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${ranks}" var="r" varStatus="loop">
                            <tr class="odd gradeX">
                                <td>${loop.index + 1}</td>
                                <td>${r.team.name}</td>
                                <td>${r.gamesWon + r.gamesDrawn + r.gamesLost}</td>
                                <td>${r.gamesWon}</td>
                                <td>${r.gamesDrawn}</td>
                                <td>${r.gamesLost}</td>
                                <td>${r.goalsScored} - ${r.goalsAgainst}</td>
                                <td><strong>${r.points}</strong></td>                                
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="col-md-5 ind-right-news">
                <div class="date">
                    7 January 2015 at 21:56 GMT
                </div>
                <h4 class="title">
                    <a href="#">
                        Steven Gerrard: LA Galaxy move right time for Liverpool captain
                    </a>
                </h4>
                <a href="#">
                    <img class="image" src="<c:url value="/resources/image/news/gerrardc.jpg" />" alt="">
                </a>
                Liverpool captain Steven Gerrard says it is the "right time for a new challenge" as he confirms his move to Los Angeles Galaxy in the summer.
                The Reds midfielder, 34, added that the MLS club were "aggressive" in pursuing him after he announced last week he was leaving Anfield at the end of the season.
            </div>
        </div>
        <!-- /.row -->

        <hr>

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header title">
                    <a href="#">
                        Matchday
                    </a>
                    <small></small>
                </h1>
            </div>
        </div>

        <!-- Project Two -->
        <div class="row">
            <div class="col-md-7">
                <table class="table table-striped table-bordered table-hover ind-table" 
                    id="dataTables-example" rules="none">
                    <thead>
                        <tr>
                            <th colspan="6">Saturday, January 10, 2015  </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                    </tbody>
                    <thead>
                        <tr>
                            <th colspan="6">Sunday, January 11, 2015  </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                        <tr class="odd gradeX">
                            <td class="time">12:45</td>
                            <td class="rightTeam">SunderLand</td>
                            <td class="rightTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/sunderland.png" />" alt="">
                            </td>
                            <td class="versus">V</td>
                            <td class="leftTeamLogo">
                                <img class="image" 
                                    src="<c:url value="/resources/image/logo/liverpool.png" />" alt="">
                            </td>
                            <td class="leftTeam">Liverpool</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="col-md-5 ind-right-news">
                <div class="date">
                    7 January 2015 at 18:00 GMT
                </div>
                <h4 class="title">
                    <a href="#">
                        Ronaldo is a great but Messi is unique
                    </a>
                </h4>
                    <a href="#">
                        <img class="image" src="<c:url value="/resources/image/news/messi.jpg" />" alt="">
                    </a>
                    The pair are competing with Manuel Neuer for this year's Ballon d'Or and the Croatia international feels that his Barcelona team-mate should be the clear victor
            </div>
            <div class="col-md-5 ind-right-news">
                <div class="date">
                    7 January 2015 at 14:00 GMT
                </div>
                <h4 class="title">
                    <a href="#">
                        Xavi dismisses CR7's Ballon d'Or bid
                    </a>
                </h4>
                <a href="#">
                    <img class="image" src="<c:url value="/resources/image/news/ronaldo.jpg" />" alt="">
                </a>
                The midfielder does not see why the Real Madrid forward should win the award given his failure to lead Portugal out of the World Cup group stage
            </div>
        </div>
        <!-- /.row -->

        <hr>

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Team of The Week
                    <small></small>
                </h1>
            </div>
        </div>

        <!-- Project Three -->
        <div class="row">
            <div class="col-md-7">
                <div class="teamofweek teamofweek-single">
                    <div>
                        <div class="ind-ttw-container">
                            <div class="input-group season">
                                <div class="input-group-btn">
                                    <button type="button" class="btn btn-default">Season : </button>
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">2014-2015</a></li>
                                        <li><a href="#">2013-2014</a></li>
                                        <li><a href="#">2012-2013</a></li>
                                    </ul>
                                </div>
                                <input type="text" class="form-control input-season" value="2014-2015" disabled>
                            </div>

                            <div class="input-group week">
                                <div class="input-group-btn">
                                    <button type="button" class="btn btn-default">Week : </button>
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">20</a></li>
                                        <li><a href="#">19</a></li>
                                        <li><a href="#">18</a></li>
                                    </ul>
                                </div>
                                <input type="text" class="form-control input-week" value="20" disabled>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div>
                            <div class="team-wrapper">
                                <div class="rows_1">
                                    <div class="gk">
                                        <img src="<c:url value="/resources/image/uniform/mancity-gk.png" />" alt="">
                                        <p>Joe Hart</p><p>18 (64)</p><p></p>
                                    </div>
                                </div>
                                <div class="rows_4">
                                    <div class="de1">
                                        <img src="<c:url value="/resources/image/uniform/southampton.png" />" alt="">
                                        <p>Toby Alderweireld</p><p>50 (103)</p><p></p>
                                    </div>
                                    <div class="de2">
                                        <img src="<c:url value="/resources/image/uniform/hullcity.png" />" alt="">
                                        <p>James Chester</p><p>33 (164)</p><p></p>
                                    </div>
                                    <div class="de3">
                                        <img src="<c:url value="/resources/image/uniform/chelsea.png" />" alt="">
                                        <p>John Terry</p><p>34 (18)</p><p></p>
                                    </div>
                                    <div class="de4">
                                        <img src="<c:url value="/resources/image/uniform/southampton.png" />" alt="">
                                        <p>Ryan Bertrand</p><p>35 (25)</p><p></p>
                                    </div>
                                </div>
                                <div class="rows_4">
                                    <div class="mf1">
                                        <img src="<c:url value="/resources/image/uniform/southampton.png" />" alt="">
                                        <p>Sadio Mane</p><p>50 (154)</p><p></p>
                                    </div>
                                    <div class="mf2">
                                        <img src="<c:url value="/resources/image/uniform/mancity.png" />" alt="">
                                        <p>Fernando</p><p>38 (108)</p><p></p>
                                    </div>
                                    <div class="mf3">
                                        <img src="<c:url value="/resources/image/uniform/hullcity.png" />" alt="">
                                        <p>Gaston Ramirez</p><p>39 (300)</p><p></p>
                                    </div>
                                    <div class="mf4">
                                        <img src="<c:url value="/resources/image/uniform/arsenal.png" />" alt="">
                                        <p>Tomas Rosicky</p><p>40 (334)</p><p></p>
                                    </div>
                                </div>
                                <div class="rows_2">
                                    <div class="st1">
                                        <img src="<c:url value="/resources/image/uniform/arsenal.png" />" alt="">
                                        <p>Alexis S�nchez</p><p>43 (4)</p><p></p>
                                    </div>
                                    <div class="st2">
                                        <img src="<c:url value="/resources/image/uniform/manutd.png" />" alt="">
                                        <p>Wayne Rooney</p><p>57 (12)</p><p></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.row -->

        <hr>

        <!-- Pagination -->
<!--         <div class="row text-center">
            <div class="col-lg-12">
                <ul class="pagination">
                    <li>
                        <a href="#">�</a>
                    </li>
                    <li class="active">
                        <a href="#">1</a>
                    </li>
                    <li>
                        <a href="#">2</a>
                    </li>
                    <li>
                        <a href="#">3</a>
                    </li>
                    <li>
                        <a href="#">4</a>
                    </li>
                    <li>
                        <a href="#">5</a>
                    </li>
                    <li>
                        <a href="#">�</a>
                    </li>
                </ul>
            </div>
        </div> -->
        <!-- /.row -->

        <hr>

        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
                    <p>Copyright � Your Website 2014</p>
                </div>
            </div>
            <!-- /.row -->
        </footer>

    </div>
    <!-- /.container -->

    <!-- jQuery -->
    <script src="<c:url value="/resources/js/jquery-2.1.3.js" />"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="<c:url value="/resources/js/bootstrap.js" />"></script>




</body></html>