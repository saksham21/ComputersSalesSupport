<%@ page import="java.io.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE HTML>
<html>
<head>
<title>Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="resources/lib/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/datatables.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/select2.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="resources/app/css/computers-common.css"
	type="text/css" />
<link rel="stylesheet" href="resources/app/css/dashboard.css"
	type="text/css" />

<style>
.abcd {
	height: 512px;
	max-height: 512px;
}

.set_target {
	height: auto;
	max-height: 512px;
	overflow-y: scroll;
}

.progress-bar-red {
	background-color: #dd4b39;
}

.show_office_sales {
	height: auto;
	max-height: 512px;
	overflow-y: scroll;
}
</style>
</head>


<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />
	<div class="normal_view">
		<div class="container">

			<div class="row">
				<div class="col-lg-3 col-md-6">
					<a href="clientmanagement#table2">
						<div class="panel panel-primary">
							<div class="panel-heading">
								<div class="row">
									<div class="col-xs-3">
										<i class="fa fa-star fa-5x"></i>
									</div>
									<div class="col-xs-9 text-right">
										<div class="huge leads_generated_office"></div>
										<div>Leads Generated!</div>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-6">
					<a href="clientmanagement#table1">
						<div class="panel panel-green">
							<div class="panel-heading">
								<div class="row">
									<div class="col-xs-3">
										<i class="fa fa-flag fa-5x"></i>
									</div>
									<div class="col-xs-9 text-right">
										<div class="huge active_leads_office"></div>
										<div>Active Leads!</div>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-6">
					<a href="clientmanagement#table3">
						<div class="panel panel-yellow">
							<div class="panel-heading">
								<div class="row">
									<div class="col-xs-3">
										<i class="fa fa-shopping-cart fa-5x"></i>
									</div>
									<div class="col-xs-9 text-right">
										<div class="huge sales_per_order"></div>
										<div>Average Sales per Order!</div>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-6">
					<a href="employeemanagement">
						<div class="panel panel-red">
							<div class="panel-heading">
								<div class="row">
									<div class="col-xs-3">
										<i class="fa fa-child fa-5x"></i>
									</div>
									<div class="col-xs-9 text-right">
										<div class="huge rep_in_office"></div>
										<div>Representatives!</div>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
			</div>
			<div class="row" style="padding-top: 10%">
				<div class="col-lg-3 abcd">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-bar-chart-o fa-fw"></i> <span>Monthly
								Sales</span>
							<!-- <span id='close' class="pull-right"  onclick='this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode); return false;'>x</span> -->
						</div>
						<div class="panel-body text-center">
							<!-- 
	                        	<div class="huge" id="monthly_sales"></div> -->
							<div id="morris-donut-chart"></div>
							<sec:authorize access="hasRole('Director')">
								<button id="view_office_sales1" type="button"
									class="btn btn-default btn-md">
									<span class="glyphicon glyphicon-mail" aria-hidden="true"></span>
									<span>View Details</span>
								</button>
							</sec:authorize>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<div class="col-lg-3 abcd">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-bar-chart-o fa-fw"></i> <span>Annual Sales</span>
							<!-- <span id='close' class="pull-right"  onclick='this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode); return false;'>x</span> -->
						</div>
						<div class="panel-body text-center">
							<!-- 	                        <div class="huge" id="annual_sales"></div> -->
							<div id="morris-donut-chart1"></div>
							<sec:authorize access="hasRole('Director')">
								<button id="view_office_sales2" type="button"
									class="btn btn-default btn-md">
									<span class="glyphicon glyphicon-mail" aria-hidden="true"></span>
									<span>View Details</span>
								</button>
							</sec:authorize>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<sec:authorize access="hasRole('Director')">
					<div class="col-lg-6 show_office_sales" style="display: none">
						<div class="well">
							<span>Select an office:</span><br> <select
								id="employee-office-filter"
								class="form-control select-box-inline office-selects">
								<option disabled selected>-- select an option --</option>
							</select> <br>
							<div class="officewise_stats" style="display: none;">

								<label>Monthly Sales Status</label><br> <small
									class="pull-right dsales_bar1"></small> <small>(Sales
									achieved till today)</small>
								<div class="progress">
									<div class="progress-bar progress-bar-success"
										role="progressbar" id="bar1" aria-valuenow="70"
										aria-valuemin="0" aria-valuemax="100"></div>
								</div>
								<small>(Sales to be achieved till today)</small>
								<div class="progress">
									<div class="progress-bar progress-bar-red" role="progressbar"
										id="bar2" aria-valuenow="70" aria-valuemin="0"
										aria-valuemax="100"></div>
								</div>
								<br> <label>Annual Sales Status</label><br> <small
									class="pull-right dsales_bar2"></small> <small>(Sales
									achieved till today)</small>
								<div class="progress">
									<div class="progress-bar progress-bar-success"
										role="progressbar" id="bar3" aria-valuenow="70"
										aria-valuemin="0" aria-valuemax="100"></div>
								</div>
								<small>(Sales to be achieved till today)</small>
								<div class="progress">
									<div class="progress-bar progress-bar-red" role="progressbar"
										id="bar4" aria-valuenow="70" aria-valuemin="0"
										aria-valuemax="100"></div>
								</div>
								<div class="show_office_sales_director"></div>
								<form role="form" id="status_form" style="display: none;">
									<div class="form-group col-md-8">
										<label for="comment">Comment:</label>
										<textarea class="form-control" id="comment"></textarea>
									</div>
									<div class="form-group col-md-4" style="margin-top: 7%">
										<button id="msg_broadcast" type="button"
											class="btn btn-default btn-md">
											<span class="glyphicon glyphicon-mail" aria-hidden="true"></span>
											<span>Send</span>
										</button>
									</div>
								</form>
								<br>
								<button id="director_broadcast" type="button"
									class="btn btn-default btn-md">
									<span class="glyphicon glyphicon-send" aria-hidden="true"></span>
									<span>Send Message to Manager</span>
								</button>
							</div>
						</div>
					</div>
				</sec:authorize>

				<sec:authorize access="hasRole('Manager')">
					<div class="col-lg-6">
						<div class="well">
							<label>Monthly Sales Status</label><br> <small
								class="pull-right msales_bar1"></small> <small>(Sales
								achieved till today)</small>
							<div class="progress">
								<div class="progress-bar progress-bar-success"
									role="progressbar" id="mbar1" aria-valuenow="70"
									aria-valuemin="0" aria-valuemax="100"></div>
							</div>
							<small>(Sales to be achieved till today)</small>
							<div class="progress">
								<div class="progress-bar progress-bar-red" role="progressbar"
									id="mbar2" aria-valuenow="70" aria-valuemin="0"
									aria-valuemax="100"></div>
							</div>
							<br> <label>Annual Sales Status</label><br> <small
								class="pull-right msales_bar2"></small> <small>(Sales
								achieved till today)</small>
							<div class="progress">
								<div class="progress-bar progress-bar-success"
									role="progressbar" id="mbar3" aria-valuenow="70"
									aria-valuemin="0" aria-valuemax="100"></div>
							</div>
							<small>(Sales to be achieved till today)</small>
							<div class="progress">
								<div class="progress-bar progress-bar-red" role="progressbar"
									id="mbar4" aria-valuenow="70" aria-valuemin="0"
									aria-valuemax="100"></div>
							</div>
							<div class="show_office_sales_manager"></div>
							<button id="recommend_inhouse" class="btn btn-xs btn-info"
								style="display: none; margin-top: 2%">
								<i class="fa fa-users"></i>&nbsp;Create In-house Meeting
							</button>
							<button id="contact_existing_clients" class="btn btn-xs btn-info"
								style="display: none; margin-top: 2%">
								<span class="glyphicon glyphicon-user"></span> Contact Existing
								Clients
							</button>
						</div>
					</div>
				</sec:authorize>

				<sec:authorize access="hasRole('Director')">
					<div class="col-lg-6 set_target" style="display: none">
						<div class="well">
							<div class="form-group">
								<label class="pull-left">Set Growth(%): </label> <input
									name="growth" type="number" class="form-control pull-left"
									style="width: 15%; margin-top: -1.5%" id="growth" value="5"
									min="0" /> <label style="padding-left: 10%;"
									class="month_year"></label>
								<button id="set_target_sales_btn" type="button"
									class="btn btn-default btn-md pull-right"
									style="margin-top: -1.5%;">
									<span class="glyphicon glyphicon-stats" aria-hidden="true"></span>
									<span>Set Target</span>
								</button>
							</div>
							<div class="form-group" style="padding-top: 7%">
								<form class="add_offices" id="add_target_sales"></form>
							</div>
						</div>
					</div>
				</sec:authorize>
			</div>
			<sec:authorize access="hasRole('Director')">
				<div class="row">
					<div class="text-center col-lg-6" style="padding-top: 5%">
						<button id="set_target_btn" type="button"
							class="btn btn-default btn-md btn-block">
							<span class="glyphicon glyphicon-stats" aria-hidden="true"></span>
							<span>Set Sales Target</span>
						</button>
					</div>
				</div>
			</sec:authorize>
		</div>
	</div>


	<script src="resources/lib/js/jquery-2.1.4.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-2.1.4.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/select2.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.custom.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/metisMenu.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/raphael-min.js" type="text/javascript"></script>
	<script src="resources/lib/js/morris.min.js" type="text/javascript"></script>
	<script src="resources/app/js/dashboard.js" type="text/javascript"></script>
</body>
</html>