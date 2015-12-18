<%@ page session="true" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
<title>Client Management</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="resources/lib/css/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/jquery-ui.min.css" type="text/css" />
<link rel="stylesheet" href="resources/lib/css/bootstrap.css" type="text/css" />
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
					data-toggle="modal" data-target="#product-add-modal">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					<span>Add</span>
				</button>
				<button id="product-edit-modal-btn" type="button" class="btn btn-default btn-md"
					data-toggle="modal" data-target="#product-add-modal" disabled="disabled">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<span>Edit</span>
				</button>
				<button id="product-open-delete-modal-btn" type="button" class="btn btn-default btn-md"
					data-toggle="modal" data-target="#product-delete-modal" disabled="disabled">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					<span>Delete</span>
				</button>
			</div>
			</sec:authorize>
			
			<div class="col-lg-12 col-sm-12">
					<table id="product-table" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>ID</th>
							<th>Product Name</th>
							<th>Company Name</th>
							<th>Price</th>
							<th>Description</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	</div>
	<div class="modal fade" id="product-add-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Add new product</h4>
				</div>
				<div class="modal-body">
					<form id="product-form">
						<div class="form-group">
							<label for="id">Id</label>
							<input name="id" type="number" class="form-control" id="id" readonly placeholder="System Generated"/>
						</div>
						<div class="form-group">
							<label for="name">Product Name</label>
							<input name="name" type="text" class="form-control" id="name"
								placeholder="Product Name"/>
						</div>
						<div class="form-group">
							<label for="company">Product Company</label>
							<input name="company" type="text" class="form-control" id="company"
								placeholder="Company Name"/>
						</div>
						<div class="form-group">
							<label for="description">Description of Product</label>
							<textarea name="description" class="form-control" id="description" ></textarea>
						</div>						
						<div class="form-group">
							<label for="price">Price</label>
							<input name="price" type="number" step="0.01" class="form-control" id="price"
								placeholder="Product price"/>
						</div>
						
						
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="product-add-button" type="button" class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>
	
		<div class="modal fade" id="product-delete-modal" tabindex="-1" role="dialog"
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
					Are you sure you want to delete the selected product?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button id="product-delete-button" type="button" class="btn btn-primary">Delete</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- Common footer -->
	<jsp:include page="footer.jsp" />
	<script src="resources/app/js/product.js" type="text/javascript"></script>
</body>
</html>