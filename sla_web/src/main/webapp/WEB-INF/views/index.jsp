<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="resources/css/cover.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>SDK Log Analyze Home</title>
</head>
<body>
	<div class="site-wrapper">

		<div class="site-wrapper-inner">

			<div class="cover-container">

				<div class="masthead clearfix">
					<div class="inner">
						<h3 class="masthead-brand">Cover</h3>
						<nav>
						<ul class="nav masthead-nav">
							<li class="active"><a href="#">Home</a></li>
							<li><a href="#">Features</a></li>
							<li><a href="#">Contact</a></li>
						</ul>
						</nav>
					</div>
				</div>

				<div class="inner cover">
					<h1 class="cover-heading" id="result">Cover your page.</h1>
					<p class="lead">Cover is a one-page template for building
						simple and beautiful home pages. Download, edit the text, and add
						your own fullscreen background photo to make it your own.</p>
					<p class="lead">
						<a href="javascript:void(0);" onclick="statisticsAPKStatus()"
							class="btn btn-lg btn-default">查询状态</a>
					</p>
				</div>

				<div class="mastfoot">
					<div class="inner">
						<p>
							Cover template for <a href="http://getbootstrap.com">Bootstrap</a>,
							by <a href="https://twitter.com/mdo">@mdo</a>.
						</p>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function statisticsAPKStatus() {
		alert("11");
		$.ajax({
			type : 'post',
			url : 'statisticsAPKStatus',
			cache : false,
			dataType : 'text',
			success : function(data) {
				alert(data);
				$("#result").html(data);
			}
		});
	}
</script>
</html>