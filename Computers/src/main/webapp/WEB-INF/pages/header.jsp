<%@ page session="true"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!--     <link rel="stylesheet" href="resources/lib/css/bootstrap.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/datatables.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/select2.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/app/css/computers-common.css" type="text/css" /> -->
</head>
<style>
.navbar {
	position: absolute;
	height: 55px;
}

.center {
	margin: auto;
	width: 50%;
	padding: auto;
}

.pull-right {
	float: right !important;
}
</style>
<body>
	<div id="employee_username" style="display: none;">${pageContext.request.userPrincipal.name}</div>
	<c:set var="string1" value="${pageContext.request.userPrincipal.name}" />
	<c:set var="string2" value="${fn:toUpperCase(string1)}" />
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<h4
			style="color: #9d9d9d; float: left; margin-left: 10%; margin-top: 1%">COMPUTERS</h4>
		<div class="center">
			<div class="navbar-header">
				<button class="navbar-toggle" type="button" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

			</div>
			<nav class="navbar-collapse collapse" style="background: #222">
				<ul id="computer-navbar" class="nav navbar-nav">
					<sec:authorize access="hasAnyRole('Director', 'Manager')">
						<li id="nav-dashboard"><a href="./dashboard">Home</a></li>
						<li id="nav-office"><a href="./officemanagement">Office</a></li>
					</sec:authorize>
					<li id="nav-product"><a href="./product">Products</a></li>
					<sec:authorize access="hasAnyRole('Director', 'Manager')">
						<li id="nav-employee"><a href="./employeemanagement">Employee</a></li>
					</sec:authorize>
					<sec:authorize access="hasRole('Manager')">
						<li id="nav-googlemaps"><a href="./maps">Find Clients</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('Representative', 'Manager')">
						<li id="nav-clients"><a href="./clientmanagement">Clients</a></li>
					</sec:authorize>
					<li id="nav-calendar"><a href="./taskmanagement">Calendar</a></li>
					<li id="nav-logout"><a href="./logout">Logout</a></li>
				</ul>
			</nav>
		</div>
	</nav>
</body>
</html>