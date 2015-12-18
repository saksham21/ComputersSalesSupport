<%@ page session="true" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
<title>User Management</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="resources/lib/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/datatables.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/select2.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/select2-bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="resources/app/css/computers-common.css" type="text/css" />
</head>
<body>
	<!-- Common header -->
	<jsp:include page="header.jsp" />
	<jsp:include page="sidebar.jsp" />
	<div class="normal_view">
	<div class="container" id="layout">
		<div class="row">
			<sec:authorize access="hasAnyRole('Director')">
			<div class="col-lg-12 col-sm-12 table-buttons">
				<!-- Buttons -->
				<button type="button" class="btn btn-default btn-md"
					data-toggle="modal" data-target="#office-add-modal">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					<span>Add</span>
				</button>
				<button id="office-open-delete-modal-btn" type="button" class="btn btn-default btn-md" data-toggle="modal" data-target="#office-delete-modal" disabled="disabled">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					<span>Delete</span>
				</button>
			</div>
			</sec:authorize>
			<div class="col-lg-12 col-sm-12">
				<table id="office-table" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	</div>

	<div class="modal fade" id="office-add-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add new office</h4>
				</div>
				<div class="modal-body">
					<form id="office-form">
						<div class="form-group">
							<label for="office-name">Name</label> <input name="name"
								type="text" class="form-control" id="office-name"
								placeholder="Office Name"/>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="office-add-button" type="button" class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="office-delete-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Confirm delete office</h4>
				</div>
				<div class="modal-body" >
					Are you sure you want to delete the selected office?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="office-delete-button" type="button" class="btn btn-primary">Delete</button>
				</div>
			</div>
		</div>
		</div>

	

	<!-- Common footer -->
	<jsp:include page="footer.jsp" />
	<script src="resources/app/js/office.js" type="text/javascript"></script>
</body>
</html>
