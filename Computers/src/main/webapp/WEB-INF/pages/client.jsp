<%@ page session="true"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE HTML>
<html>
<head>
<title>Client Management</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="resources/lib/css/jquery-ui.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/select2.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/datatables.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/font-awesome.min.css" />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker.css" type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker-standalone.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/fullcalendar.print.css"
	media='print' />
<link rel="stylesheet" href="resources/lib/css/daterangepicker.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/slider.css"
	type="text/css" />
<link rel="stylesheet" href="resources/app/css/product_timeline.css"
	type="text/css" />
<link rel="stylesheet" href="resources/app/css/computers-common.css"
	type="text/css" />



<style>
.entry:not ( :first-of-type {
	margin-top: 10 px;
}

)
.glyphicon {
	font-size: 12px;
}

.input {
	float: right;
	clear: both;
}

.tab-content {
	border-left: 1px solid #ddd;
	border-right: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	border-radius: 0px 0px 5px 5px;
	padding: 10px;
}

.nav-tabs {
	margin-bottom: 0;
}

.select2-container .select2-selection--single {
	height: 34px !important;
}

.select2-container--default .select2-selection--single .select2-selection__rendered
	{
	line-height: 34px !important;
}

.wallmessages {
	height: 800px;
	overflow-y: auto;
}

#client-table-3 {
	table-layout: fixed;
	word-wrap: break-word;
}

#ex2Slider .slider-selection {
	background: #BABABA;
}

.rp2 {
	margin-right: 50% !important;
}
</style>

</head>
<body>
	<!-- Common header -->
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />
	<div class="col-xs-3 col-xs-push-9">
		<div align="center" class="displayTrack"
			style="padding-top: 5%; display: none">
			<button type="button" class="btn btn-primary"
				id="current-order-button" onclick="currentOrderFlag()">Current
				Order</button>
			<button type="button" class="btn btn-default" id="past-order-button"
				onclick="pastOrderFlag()">Past Order</button>

			<div class="wallmessages" id="wallmessages"></div>
		</div>
	</div>


	<div class="col-xs-2 col-xs-pull-3"></div>
	<div class="col-xs-7 col-xs-pull-3">
		<div class="normal_view">
			<div class="row">
				<div class="col-lg-12 col-sm-12 table-buttons">
					<!-- Buttons -->
					<sec:authorize access="hasAnyRole('Director', 'Manager')">
						<button id="client-add-btn" type="button"
							class="btn btn-md btn-success" data-toggle="modal"
							data-target="#client-add-modal" style="display: none;">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
							<span>Add</span>
						</button>
						<button id="client-open-delete-modal-btn" type="button"
							class="btn btn-danger btn-md" data-toggle="modal"
							data-target="#client-delete-modal" style="display: none;">
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
							<span>Delete</span>
						</button>
						<button id="client-reassign" type="button"
							class="btn btn-primary btn-md" data-toggle="modal"
							data-target="#client-reassign-modal" style="display: none;">
							<span class="glyphicon glyphicon-share" aria-hidden="true"></span>
							<span>Re-Assign</span>
						</button>
						<button id="client-assign" type="button"
							class="btn btn-primary btn-md" data-toggle="modal"
							data-target="#client-assign-modal" style="display: none;">
							<span class="glyphicon glyphicon-share" aria-hidden="true"></span>
							<span>Assign</span>
						</button>
					</sec:authorize>
					<button id="client-schedule-meeting" type="button"
						class="btn btn-info btn-md" data-toggle="modal"
						data-target="#event-add-modal" style="display: none;">
						<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
						<span>Set Reminder</span>
					</button>
					<sec:authorize access="hasAnyRole('Director', 'Manager')">
						<button id="assign-all-button" type="button"
							class="btn btn-warning btn-md" style="display: none;">
							<span class="glyphicon glyphicon-transfer" aria-hidden="true"></span>
							<span>Assign All</span>
						</button>
					</sec:authorize>
					<sec:authorize access="hasRole('Representative')">
						<button id="order-open-select-modal-btn" type="button"
							class="btn btn-danger btn-md" data-toggle="modal"
							data-target="#addOrder" style="display: none;">
							<span class="glyphicon glyphicon-shopping-cart"
								aria-hidden="true"></span> <span>Client Order</span>
						</button>
						<button id="order-send-modal-btn" type="button"
							class="btn btn-primary btn-md" data-toggle="modal"
							data-target="#sendStatus" style="display: none;">
							<span class="glyphicon glyphicon-send" aria-hidden="true"></span>
							<span>Meeting Details</span>
						</button>
						<button id="product-find-button" type="button"
							class="btn btn-warning btn-md" data-toggle="modal"
							style="display: none;" onclick="findBestProducts()">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							<span>Find best products</span>
						</button>
					</sec:authorize>
					<div class="form-group" id="show_contact_list"
						style="position: auto; display: none;">
						<label for="contact_list" class="col-sm-10 control-label"
							style="padding-left: 50% !important; padding-top: 0.5%">Get
							Client List who haven't been contacted since</label>
						<div class="col-sm-2">
							<input type="number" class="form-control" id="contact_list"
								value="0" min="0" step="0.1">(In months)
						</div>
					</div>
					<ul class="nav nav-tabs" id="tabs" style="margin-top: 6%">
						<li class="active" id="t_1"><a href="#table1"
							data-toggle="tab">Assigned</a></li>
						<sec:authorize access="hasAnyRole('Manager')">
							<li id="t_2"><a href="#table2" data-toggle="tab">Unassigned</a></li>
						</sec:authorize>
						<li id="t_3"><a href="#table3" data-toggle="tab">Existing</a></li>
					</ul>
					<div class="tab-content">
						<div id="table1" class="table-responsive tab-pane fade in active"
							style="overflow-x: hidden;">
							<table id="client-table-1"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>ID</th>
										<th>Name</th>
										<th>Contact</th>
										<th>Address</th>
										<th>Domain</th>
										<th>Status</th>
										<th>Employee Name</th>
									</tr>
								</thead>
							</table>
						</div>
						<sec:authorize access="hasAnyRole('Director', 'Manager')">
							<div id="table2" class="table-responsive tab-pane fade"
								style="overflow-x: hidden;">
								<table id="client-table-2"
									class="table table-striped table-bordered">
									<thead>
										<tr>
											<th>ID</th>
											<th>Name</th>
											<th>Contact</th>
											<th>Address</th>
											<th>Domain</th>
										</tr>
									</thead>
								</table>
							</div>
						</sec:authorize>
						<div id="table3" class="table-responsive tab-pane fade"
							style="overflow-x: hidden;">
							<table id="client-table-3"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>ID</th>
										<th>Name</th>
										<th>Contact</th>
										<th>Address</th>
										<th>Domain</th>
										<th>Last Order Time</th>
										<th>#Orders</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div class="modal fade" id="recommendedProducts">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 class="modal-title">
						Products Hit List <a href="#" class="btn btn-xs btn-primary"
							onclick="showAllProducts()" style="margin-left: 60%;"><span
							class="glyphicon glyphicon-book"></span> All Products</a>
					</h3>
				</div>
				<div class="modal-body">
					<h5 class="text-center addifno"></h5>
					<table class="table table-striped tblGrid" id="tblGrid">
						<thead>
							<tr>
								<th>ID</th>
								<th>Product Name</th>
								<th>Brand</th>
								<th>Price</th>
								<th>Quantity</th>
								<th>Action</th>
							</tr>
						</thead>
					</table>
					<div class="abc" style="display: none;">
						<h4 class="text-center">
							<label style="margin-bottom: 4%;">Ask for Client's Requirements</label>
						</h4>
					</div>
					<div class="form-group rp" style="display: none;">
						<form id="rp_Form">

							<div class="form-group">
								<div class="row">
									<div class="col-lg-3"></div>
									<div class="col-lg-2">
										<label for="rp2" >Brand</label> <!--  style="left: 25%; padding-top: 0.5%;" -->
									</div>
									<div class="col-lg-2">
										<select class="form-control rp2" name="rp2" id="rp2"></select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="row">
									<div class="col-lg-3"></div>
									<div class="col-lg-2">
										<label for="rp3">Storage</label>
									</div>
									<div class="col-lg-2">
										<select class="form-control rp3" name="rp3" id="rp3">
											<option value="">Any Storage</option>
											<option value="250GB">250GB</option>
											<option value="500GB">500GB</option>
											<option value="1TB">1TB</option>
											<option value="2TB">2TB</option>
											<option value="3TB">3TB</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="row">
									<div class="col-lg-3"></div>
									<div class="col-lg-2">
										<label for="rp4">RAM</label>
									</div>
									<div class="col-lg-2">
										<select class="form-control rp4" name="rp4" id="rp4">
											<option value="">Any RAM</option>
											<option value="2GB">2GB</option>
											<option value="4GB">4GB</option>
											<option value="6GB">6GB</option>
											<option value="8GB">8GB</option>
											<option value="16GB">16GB</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="row">
									<div class="col-lg-3"></div>
									<div class="col-lg-2">
										<label for="rp7">Processor</label>
									</div>
									<div class="col-lg-2">
										<select class="form-control rp7" name="rp7" id="rp7">
											<option value="">Any Process</option>
											<option value="2GB">i3</option>
											<option value="4GB">i5</option>
											<option value="6GB">i7</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="row">
									<div class="col-lg-3"></div>
									<div class="col-lg-2">
										<label for="rp9">Price
											Range </label>
									</div>
									<div class="col-lg-1">
										<b>$200 </b><input name="rp9" id="rp9" type="text"
											class="span2" style="width: 300%" data-slider-id="#ex2Slider"
											data-slider-min="200" value="500,2000"
											data-slider-max="10000" data-slider-step="100"
											data-slider-value="[500,2000]" /> <b> $10000</b>
									</div>
								</div>
							</div>
						</form>
					</div>

				</div>
				<div class="modal-footer model_load">
					<div class="form-group">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button onclick="newModalLoad()" type="button"
							class="btn btn-primary">Products based on Client's Need</button>
						<div class="clearfix"></div>
					</div>
				</div>
				<div class="modal-footer searchRecProducts" style="display: none;">
					<div class="form-group">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button onclick="searchRec()" type="button"
							class="btn btn-primary">Search</button>
						<div class="clearfix"></div>
					</div>
				</div>



			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->


	<div class="modal fade" id="client-add-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add new client</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<form id="client-form">
							<div class="form-group">
								<label for="client-name">Name</label> <input name="name"
									type="text" class="form-control" id="client-name"
									placeholder="Client Name" />
							</div>
							<div class="form-group">
								<label for="client-contact">Contact</label> <input
									name="contact" type="text" class="form-control"
									id="client-contact" placeholder="Client Contact" />
							</div>
							<div class="form-group">
								<label for="client-address">Address</label> <input
									name="address" type="text" class="form-control"
									id="client-address" placeholder="Client Address" />
							</div>
							<div class="form-group">
								<label for="client-domain">Domain</label> <select name="domain"
									class="form-control" id="client-domain">
									<option disabled selected>-- select an option --</option>
									<option value="school">School</option>
									<option value="university">University</option>
									<option value="electronics_store">Electronics Store</option>
								</select>
							</div>
							<div class="form-group">
								<label for="client-status">Status</label> <input name="status"
									type="text" class="form-control" id="client-status"
									value="UNASSIGNED" readonly="readonly" />
							</div>

						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="client-add-button" type="button"
						class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="client-assign-modal" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Assign selected
						Client</h4>
				</div>
				<div class="modal-body">
					<form id="client-assign-form" class="assign-form">
						<div class="form-group">
							<label for="id">Id</label> <input name="id" type="number"
								class="form-control" id="id" />
						</div>
						<div class="form-group">
							<label for="name">Name</label> <input name="name" type="text"
								class="form-control" id="name" />
						</div>
						<div class="form-group">
							<label for="employeename">Employee Name</label> <select
								name="employeename" class="form control take_from_autocomplete"
								id="employeename">
								<optgroup label="Recommended Representatives" class="ac_1">
								</optgroup>
								<optgroup label="All Representatives" class="ac_2">
								</optgroup>
							</select>
						</div>
					</form>


				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="client-assign-button" type="button"
						class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="client-reassign-modal" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Re-assign selected
						Client</h4>
				</div>
				<div class="modal-body">
					<form id="client-reassign-form" class="reassign-form">
						<div class="form-group">
							<label for="id1">Id</label> <input name="id1" type="number"
								class="form-control" id="id1" />
						</div>
						<div class="form-group">
							<label for="name1">Name</label> <input name="name1" type="text"
								class="form-control" id="name1" />
						</div>
						<div class="form-group">
							<label for="employeename1">Employee Name</label> <select
								name="employeename1" class="take_from_autocomplete1"
								id="employeename1">
								<optgroup label="Recommended Representatives" class="ac1_1">
								</optgroup>
								<optgroup label="All Representatives" class="ac1_2">
								</optgroup>
							</select>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="client-reassign-button" type="button"
						class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="client-delete-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Confirm delete
						client</h4>
				</div>
				<div class="modal-body">Are you sure you want to delete the
					selected client?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="client-delete-button" type="button"
						class="btn btn-primary">Delete</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="sendStatus" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Send status to
						Manager</h4>
				</div>
				<div class="modal-body">
					<form id="status-send-form" class="status-form">
						<div class="form-group">
							<label for="id4">Id</label> <input name="id4" type="number"
								class="form-control" id="id4" />
						</div>
						<div class="form-group">
							<label for="name4">Name</label> <input name="name4" type="text"
								class="form-control" id="name4" />
						</div>

						<div class="form-group">
							<label for="status4">Status</label> <select name="status4"
								class="form-control" id="status4">
								<option disabled selected>-- select an option --</option>
								<option value="INTERESTED">INTERESTED</option>
								<option value="NOT_INTERESTED">NOT INTERESTED</option>
							</select>
						</div>

						<div class="form-group">
							<label for="description4">Meeting details</label>
							<textarea name="description4" class="form-control"
								id="description4"></textarea>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="status-send-button" type="button"
						class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="event-add-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add new event</h4>
				</div>
				<div class="modal-body">
					<form id="event-form" class="event-form">

						<div class="form-group">
							<label for="title">Event Title</label> <input name="title"
								type="text" class="form-control" id="title" />
						</div>

						<div class="form-group col-lg-6">
							<label for="creator">Id</label> <input name="creator" type="text"
								class="form-control" id="creator"
								value="${pageContext.request.userPrincipal.name}" readonly
								placeholder="${pageContext.request.userPrincipal.name}" />
						</div>

						<div class="form-group col-lg-6">
							<label for="client_id">Client Id</label> <input name="client_id"
								type="text" class="form-control" id="client_id" />
						</div>

						<div class="form-group">
							<label for="startDate">startDate</label> <input name="startDate"
								type="text" class="form-control" id="startDate">
						</div>

						<div class="form-group">
							<label for="description">Event description</label>
							<textarea name="description" class="form-control"
								id="description"></textarea>
						</div>

						<div class="form-group">
							<label for="reminder-setup">Reminder</label>
							<button id="yes_button" type="button" class="btn btn-default"
								onclick="addFields()">Yes</button>
							<button id="no_button" type="button" class="btn btn-info"
								onclick="removeFields()">No</button>
						</div>

						<div class="form-group" id="set_reminder" style="display: none;">
							<div class='input-group date col-lg-6' id='startDate1'>
								<input name="startDate1" id="startDate1" type='text'
									class="form-control" /> <span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="event-add-button" type="button" class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade addOrder" id="addOrder" role="dialog"
		tabindex="-1">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">New Order</h4>
				</div>
				<div class="modal-body">
					<form id="add-order-form" class="add-order-form" role="form"
						autocomplete="on"></form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="save-order-button" type="button"
						class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Common footer -->
	<script src="resources/lib/js/jquery-ui.min.js" type="text/javascript"></script>

	<script src="resources/lib/js/datatables.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/fnReloadAjax.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.custom.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/moment.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-datetimepicker.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/daterangepicker.js"
		type="text/javascript"></script>

	<script src="resources/lib/js/select2.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/fullcalendar.js" type="text/javascript"></script>
	<script src="resources/lib/js/fullcalendar.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-slider.js"
		type="text/javascript"></script>
	<script src="resources/app/js/common.js" type="text/javascript"></script>
	<script src="resources/app/js/client.js" type="text/javascript"></script>
</body>
</html>
