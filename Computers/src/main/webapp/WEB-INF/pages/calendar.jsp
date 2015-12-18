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
<link rel="stylesheet" href="resources/lib/css/font-awesome.min.css" />
<link rel="stylesheet" href="resources/lib/css/fullcalendar.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/daterangepicker.css"
	type="text/css" />
<link rel="stylesheet" href="resources/lib/css/fullcalendar.print.css"
	media='print' />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker.css" type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker-standalone.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/lib/css/bootstrap-datetimepicker.css" type="text/css" />
<link rel="stylesheet" href="resources/app/css/computers-common.css"
	type="text/css" />

<style type="text/css">
</style>

</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />
	<div class="normal_view">
		<div class="container">
			<sec:authorize access="hasAnyRole('Director', 'Manager')">
				<button type="button" class="btn btn-default" id="schedulemeeting"
					data-toggle="modal" data-target="#inhousemeeting-modal">
					<i class="fa fa-users"></i> Create In-House Meeting
				</button>
			</sec:authorize>
			<div id="calendar" style="padding-top: 5%; width: 100%"></div>
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
						<h4 class="modal-title" id="myModalLabel">Create new event</h4>
					</div>
					<div class="modal-body">
						<form id="event-form">

							<div class="form-group">
								<label for="title">Title</label> <input name="title" type="text"
									class="form-control" id="title" />
							</div>

							<div class="form-group">
								<label for="startDate">Date</label> <input name="startDate"
									type="text" class="form-control" id="startDate">
							</div>

							<div class="form-group">
								<label for="description">Description</label> <input
									name="description" type="text" class="form-control"
									id="description" />
							</div>

							<div class="form-group">
								<label for="creator">Creator</label> <input name="creator"
									type="text" class="form-control" id="creator"
									value="${pageContext.request.userPrincipal.name}" readonly
									placeholder="${pageContext.request.userPrincipal.name}" />
							</div>

							<div class="form-group">
								<label for="reminder-setup">Reminder</label>
								<button id="yes_button" type="button" class="btn btn-default"
									onclick="addFields()">Yes</button>
								<button id="no_button" type="button" class="btn btn-info"
									onclick="removeFields()">No</button>
							</div>

							<div class="form-group" id="set_reminder" style="display: none;">
								<div class='input-group date col-lg-6'>
									<input name="startDate2" id="startDate2" type='text'
										class="form-control" /> <span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>


						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button id="event-add-button" type="button"
							class="btn btn-primary">Create</button>
					</div>
				</div>
			</div>
		</div>


		<div class="modal fade" id="event-click-modal" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Edit event</h4>
					</div>
					<div class="modal-body">
						<form id="event-form1">

							<div class="form-group">
								<label for="title1">Title</label> <input name="title1"
									type="text" class="form-control" id="title1" />
							</div>

							<div class="form-group">
								<label for="startDate1">Date</label> <input name="startDate1"
									type="text" class="form-control" id="startDate1">
							</div>

							<div class="form-group">
								<label for="description1">Description</label> <input
									name="description1" type="text" class="form-control"
									id="description1" />
							</div>

							<div class="form-group">
								<label for="creator1">Creator</label> <input name="creator1"
									type="text" class="form-control" id="creator"
									value="${pageContext.request.userPrincipal.name}" readonly
									placeholder="${pageContext.request.userPrincipal.name}" />
							</div>

							<div class="form-group">
								<label for="startDate3">Reminder</label>
								<div class='input-group date col-lg-6'>
									<input name="startDate3" id="startDate3" type='text'
										class="form-control"> <span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button id="event-edit-button" type="button"
							class="btn btn-primary">Save</button>
						<button id="event-delete-button" type="button"
							class="btn btn-danger">Delete</button>
					</div>
				</div>
			</div>
		</div>


		<div class="modal fade inhousemeeting-modal" id="inhousemeeting-modal"
			role="dialog" tabindex="-1">
			<div class="modal-dialog modal-xl" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="addclientLabel">Create In-House
							Meeting</h4>
					</div>
					<div class="modal-body">
						<form id="inhousemeeting-form" class="form-horizontal">
							<div class="form-group">
								<label class="col-xs-3 control-label">AGENDA</label>
								<div class="col-xs-7">
									<textarea class="form-control" rows="2" id="agenda"></textarea>
								</div>
							</div>
							<div class="form-group"></div>
							<div class="form-group">
								<label class="col-xs-3 control-label" id="owner-label">INVITE
									SALESMAN</label>
								<div class="col-xs-7">
									<select id="salesman-selects"
										class="form-control salesman-selects" name="salesman"
										multiple="multiple">

									</select> <input id="checkbox" type="checkbox"> Invite all
								</div>
							</div>
						</form>
						<hr>
						<br>
						<div class="row">
							<div class="col-lg-7 connectedSortable ui-sortable">
								<div class="box box-success"></div>
								<div class="box-body">
									<div id="calendar1"></div>
								</div>
							</div>
							<div class="col-lg-5 connectedSortable ui-sortable">
								<div class="box">
									<div class="box-body">
										<form class="form-horizontal">
											<div class="form-group">
												<label class="col-sm-4 control-label">DATE AND TIME</label>
												<div class="col-sm-8">
													<input type="text" class="form-control" id="datetime-name">
												</div>
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button id="inhousemeeting-cancel-button" type="button"
							class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button id="inhousemeeting-save-button" type="button"
							class="btn btn-primary">Create</button>
					</div>
				</div>
			</div>
		</div>
		<div id="namesid" style="display: none;"></div>
	</div>

	<script src="resources/lib/js/jquery.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/select2.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/datatables.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/fnReloadAjax.js" type="text/javascript"></script>
	<script src="resources/lib/js/jquery-ui.custom.min.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/moment.min.js" type="text/javascript"></script>
	<script src="resources/lib/js/fullcalendar.js" type="text/javascript"></script>
	<script src="resources/lib/js/gcal.js" type="text/javascript"></script>
	<script src="resources/lib/js/daterangepicker.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script src="resources/lib/js/bootstrap-datetimepicker.min.js"
		type="text/javascript"></script>

	<script src="resources/app/js/common.js" type="text/javascript"></script>
	<script src="resources/app/js/calendar.js" type="text/javascript"></script>

</body>
</html>

