<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="resources/lib/css/jquery-ui.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/datatables.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/select2.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/slider.css"
	type="text/css" />
<link rel="stylesheet" href="resources/app/css/computers-common.css"
	type="text/css" />

<title>Find New Clients</title>
<meta charset="utf-8">
<style>
html, body {
	height: 100%;
}

#map {
	height: 70%;
	margin-top: 1% !important;
	border: 10px groove #ddd;
}

#ex1Slider .slider-selection {
	background: #BABABA;
}

</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />

	<div class="normal_view">
		<div class="container" tabindex="-1">
			<div class="col-xs-3" style="width: 23%; margin-top: 3%;">
				<label>Enter Location</label> <input id="postal_code"
					class="form-control controls" type="text" placeholder="Search Box">
			</div>
			<div class="col-xs-3" style="width: 23%; margin-top: 3%;">
				<label>Distance (in meter)</label> <input id="radius_size"
					data-slider-id='ex1Slider' class="form-control" type="text"
					data-slider-min="500" data-slider-max="5000"
					data-slider-step="250">
			</div>
			<div class="pull-right col-xs-1"
				style="width: 16%; margin-top: 5%; right: 21%;">
				<label style="display: none;">Submit Value</label>
				<button class=" form-control btn btn-primary" id="submit_values">Find
					New Clients</button>
			</div>
			<div class="pull-right col-xs-3"
				style="width: 17%; margin-top: 3%; right: 22%;">
				<label>Select Domain</label> <select class="form-control"
					id="places_type">
					<option value="school">School</option>
					<option value="university">University</option>
					<option value="electronics_store">Electronics Store</option>
				</select>
			</div>
		</div>
		<br>
	</div>
	<div class="container" id="map"></div>


	<!-- Modal to show clients. -->
	<div class="modal fade" id="showPossibleClients" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Potential Client
						List</h4>
				</div>
				<div class="modal-body">
					<!-- 		<div class="col-lg-12 col-sm-12 table-buttons"> -->
					<button id="add_new_clients" type="button"
						class="btn btn-success btn-md" disabled="disabled">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
						<span>Add Clients</span>
					</button>
					<button id="unwanted_clients" type="button"
						class="btn btn-danger btn-md" disabled="disabled">
						<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
						<span>Delete Clients</span>
					</button>
					<button id="view_assigned_client" type="button"
						class="btn btn-primary btn-md">
						<span class="glyphicon glyphicon-new-window" aria-hidden="true"></span>
						<span>View Clients</span>
					</button>
					<button id="add_all_potential" type="button"
						class="btn btn-info btn-md">
						<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
						<span>Add All Clients</span>
					</button>
					<br> <br>
					<table id="map_clients_table"
						class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Name</th>
								<th>Address</th>
								<th>Contact No.</th>
								<th>Domain</th>
								<th>Place Id</th>
								<th>Latitude</th>
								<th>Longitude</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>

	</div>
	<script src="resources/lib/js/jquery-ui.min.js" type="text/javascript"></script>

	<script src="resources/lib/js/datatables.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/fnReloadAjax.js" type="text/javascript"></script>
	<!-- <script src="resources/lib/js/bootstrap.js" type="text/javascript"></script> -->
	<script src="resources/lib/js/jquery-ui.custom.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-slider.js"
		type="text/javascript"></script>
	<script src="resources/app/js/common.js" type="text/javascript"></script>
	<%-- <jsp:include page="footer.jsp" /> --%>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCJBOqCab7LIZRlQbxg7jHmtlDfevmSecw&libraries=places"></script>
	<script type="text/javascript" src="resources/app/js/googlemaps.js"></script>
</body>
</html>

<%-- 
    <div class="container" id="map"></div>
    <div class="container-fluid"><button id="output" onclick="myFunction()" >get list</button></div>
	<div id="display"></div>
	<jsp:include page="footer.jsp" />
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCJBOqCab7LIZRlQbxg7jHmtlDfevmSecw&libraries=places&callback=initMap" async defer></script>
    <script type="text/javascript" src="resources/app/js/googlemaps.js"></script>
 --%>