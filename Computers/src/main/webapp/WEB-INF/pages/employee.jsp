<%@ page session="true" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
<title>Employee Management</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- <script src="resources/lib/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script> -->
    <link rel="stylesheet" href="resources/lib/css/bootstrap.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/datatables.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/select2.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
    <link rel="stylesheet" href="resources/app/css/computers-common.css" type="text/css" />
<!-- <ct th:replace="fragments/header :: commonhead"></ct> -->
</head>
<body>
	<!-- Common header -->
	<!-- <div th:replace="fragments/header :: header"></div> -->
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />
	<div class="normal_view">
	<div class="container" id="layout">
		<div class="row">
			<div class="col-lg-12 col-sm-12 table-buttons">
				<!-- Buttons -->
				<sec:authorize access="hasAnyRole('Director', 'Manager')">
				<button type="button" class="btn btn-default btn-md"
					data-toggle="modal" data-target="#employee-add-modal">
					<span class="glyphicon glyphicon-plus" aria-hidden="true" ></span>
					<span>Add</span>
				</button>
				<button id="employee-edit-modal-btn" type="button" class="btn btn-default btn-md" data-toggle="modal" data-target="#employee-add-modal" disabled="disabled">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<span>Edit</span>
				</button>
				<button id="employee-open-delete-modal-btn" type="button" class="btn btn-default btn-md" data-toggle="modal" data-target="#employee-delete-modal" disabled="disabled">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					<span>Delete</span>
				</button>
				</sec:authorize>
			</div>
			<div class="col-lg-2 col-sm-3">
			<span>Filter by office:</span>
				<select id="employee-office-filter" class="form-control select-box-inline office-selects">
					<option selected="selected" value="0">All</option>
				</select>
			</div>
			<div class="col-lg-12 col-sm-12">
				<table id="employee-table" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>ID</th>
							<th>First Name</th>
							<th>Last Name</th>
							<th>User Name</th>
							<th>Email</th>
							<th>Office</th>
							<th>Role</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	</div>

	<div class="modal fade" id="employee-add-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add new employee</h4>
				</div>
				<div class="modal-body">
					<form id="employee-form">
					
						<div class="form-group">
							<label for="id">Id</label> <input name="id"
								type="number" class="form-control" id="id"
								readonly placeholder="System Generated"/>
						</div>
						
						<div class="form-group">
							<label for="firstname">Firstname</label> <input name="firstname"
								type="text" class="form-control" id="firstname"
								placeholder="Firstname"/>
						</div>
						
						<div class="form-group">
							<label for="lastname">Lastname</label> <input name="lastname"
								type="text" class="form-control" id="lastname"
								placeholder="Lastname"/>
						</div>
						
						<div class="form-group">
							<label for="role">Role</label>
							<select id="role" class="form-control" name="role">
								<option value="Admin">Admin</option>
								<option value="Manager" selected="selected">Manager</option>
								<option value="Representative">Representative</option>
							</select>
						</div>
						
						<div class="form-group">
							<label for="username">Username</label> <input name="username"
								type="text" class="form-control" id="username"
								placeholder="Username"/>
						</div>
						
						<div class="form-group">
							<label for="email">Email</label> <input name="email"
								type="text" class="form-control" id="email"
								placeholder="Email"/>
						</div>
						
						<div class="form-group">
							<label for="password">Password</label> <input name="password"
								type="password" class="form-control" id="password"
								placeholder="Password"/>
						</div>
						
						<div class="form-group">
							<label for="officename">Office</label>
							<select id="officename" class="form-control office-selects" name="officename">
							</select>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="employee-add-button" type="button" class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="employee-delete-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Confirm delete employee</h4>
				</div>
				<div class="modal-body">
					Are you sure you want to delete the selected employee?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="employee-delete-button" type="button" class="btn btn-primary">Delete</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Common footer -->
<%-- 	<jsp:include page="footer.jsp" /> --%>
	<!-- <div th:replace="fragments/footer :: footer"></div> -->
	<script src="resources/lib/js/jquery-2.1.4.js" type="text/javascript"></script>
    <!-- <script src="resources/lib/js/jquery-2.1.4.min.js" type="text/javascript"></script> -->
<!--     <script src="resources/lib/js/jquery-ui.js" type="text/javascript"></script> -->
    <!-- <script src="resources/lib/js/jquery-ui.min.js" type="text/javascript"></script> -->
        <script src="resources/lib/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- <script src="resources/lib/js/select2.min.js" type="text/javascript"></script> -->
    <script src="resources/lib/js/datatables.min.js" type="text/javascript"></script>
    <script src="resources/lib/js/fnReloadAjax.js" type="text/javascript"></script>

    <script src="resources/app/js/common.js" type="text/javascript"></script>
    <!-- <script src="resources/lib/js/jquery-ui.custom.min.js" type="text/javascript"></script> -->
    <script src="resources/app/js/employee.js" type="text/javascript"></script>
</body>
</html>
