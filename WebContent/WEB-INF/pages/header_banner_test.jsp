<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- Styles -->

<!-- jquery-ui CSS -->
<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<%--
<!-- Bootstrap CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Animate CSS -->
<link href="css/animate.min.css" rel="stylesheet">
<!-- Basic stylesheet -->
<link rel="stylesheet" href="css/owl.carousel.css">
<!-- Font awesome CSS -->
<link href="css/font-awesome.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="css/style.css" rel="stylesheet">
<link href="css/style-color.css" rel="stylesheet">
<!-- player bar css -->
<link href="css/playerbar.css" rel="stylesheet">
<!-- player bar icon -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<!-- Favicon -->
<link rel="shortcut icon" href="img/logo/favicon.ico">  
	--%>
<!-- Javascript files -->
	<!-- jQuery -->
	<script src="js/jquery.js"></script>
	<!-- Bootstrap JS -->
	<script src="js/bootstrap.min.js"></script>
	<!-- WayPoints JS -->
	<script src="js/waypoints.min.js"></script>
	<!-- Include js plugin -->
	<script src="js/owl.carousel.min.js"></script>
	<!-- One Page Nav -->
	<script src="js/jquery.nav.js"></script>
	<!-- Respond JS for IE8 -->
	<script src="js/respond.min.js"></script>
	<!-- HTML5 Support for IE -->
	<script src="js/html5shiv.js"></script>
	<!-- Custom JS -->
	<script src="js/custom.js"></script>
	<!-- 	plaer bar function js -->

    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
 	<!-- <script type="text/javascript" src="js/player.js"></script> -->
	

	 <script type="text/javascript">
	 $.widget( "custom.catcomplete", $.ui.autocomplete, {
		    _renderMenu: function( ul, items ) {
		      var that = this,
		        currentCategory = "";
		      $.each( items, function( index, item ) {
		        if ( item.category != currentCategory ) {
		          ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
		          currentCategory = item.category;
		        }
		        that._renderItemData( ul, item );
		      });
		    }
		  });
	$(document).ready(function($){
		xhr=$.ajax({
			url:"/SpringWebProject/getFuzzySelectAllDataName.controller",
			async:false
			})
		var availableTags = xhr.responseJSON
		console.log(availableTags);
		$( "#tags" ).catcomplete({
		      source: availableTags
		    });
	});

	</script>
	<style type="text/css">
	.ui-menu{
	z-index: 999999;
	}
	.ui-autocomplete-category {
    font-weight: bold;
    padding: .2em .4em;
    margin: .8em 0 .2em;
    line-height: 1.5;
  }
	</style>
	
	

</head>
<body>		
<!-- header area -->
		<header>
			<!-- secondary menu -->
			<nav class="secondary-menu">
				<div class="container">
					<!-- secondary menu left link area -->
					<div class="sm-left">
						<!-- <strong>Phone</strong>:&nbsp; <a href="#">555 555 555</a>&nbsp;&nbsp;&nbsp;&nbsp;
						<strong>E-mail</strong>:&nbsp; <a href="#">music.site@melodi.com</a> -->
					</div>
					<!-- secondary menu right link area -->
					
					
					<div class="sm-right">
						<!-- social link -->
						
						<div class="sm-social-link">
						<div  style="display:inline-block;line-height: 20px;color: #333; z-index:-100;">
							<form action="<c:url value="FuzzySelect.controller"/>" method="get">
								<input id="tags" class="form-control-sm" type="text" name="selectCondition"/>
								<input class="form-control-sm" type="submit" value="Send">
							</form>
						</div>
							<a class="h-facebook" href="#"><i class="fa fa-facebook"></i></a>
							<a class="h-twitter" href="#"><i class="fa fa-twitter"></i></a> <a
								class="h-google" href="#"><i class="fa fa-google-plus"></i></a>
							<a class="h-linkedin" href="#"><i class="fa fa-linkedin"></i></a>
						</div>
					</div>
					<div class="clearfix"></div>
				</div>
			</nav>
			<!-- primary menu -->
			<nav class="navbar navbar-fixed-top navbar-default">
				<div class="container">
					<!-- Brand and toggle get grouped for better mobile display -->
					<div class="navbar-header">
						<button type="button" class="navbar-toggle collapsed"
							data-toggle="collapse"
							data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span> <span
								class="icon-bar"></span> <span class="icon-bar"></span> <span
								class="icon-bar"></span>
						</button>
						<!-- logo area -->
						<a class="navbar-brand" href="#home"> <!-- logo image --> <img
							class="img-responsive" src="img/logo/logo.png" alt="" />
						</a>
					</div>

					<!-- Collect the nav links, forms, and other content for toggling -->
					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav navbar-right">

							<li><a href="#latestalbum">官方節目</a></li>
							<li><a href="#featuredalbum">矚目新選</a></li>
							<li><a href="#joinus">熱播排行</a></li>
							<li><a href="#portfolio">人氣播客</a></li>
							<li><a href="#events">派對活動</a></li>
							<li><a href="#team">瀏覽搜尋</a></li>
							<li><a href="<c:url value='/login' />">註冊/登入</a></li>
						</ul>
					</div>
					<!-- /.navbar-collapse -->
				</div>
				<!-- /.container-fluid -->
			</nav>
		</header>
		<!--/ header end -->


	<!-- 		banner start -->

<!-- 		banner end -->
		
		
		
		
	
</body>

</html>