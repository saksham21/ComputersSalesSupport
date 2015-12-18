var selectedData = null;
$(document).ready(function() {
    var initPage = function() {
	switchActiveTab('nav-product');

	Computer.dataTable = $('#product-table').DataTable({
	    //			'serverSide' : true,
	    'bFilter' : true,
	    'ajax' : {
		url : 'product/displayProducts',
		type : 'POST',
		contentType : "application/json",
		data : function(d) {
		    // send only data required by backend API
		    delete (d.columns);
		    delete (d.order);
		    delete (d.search);
		    /*console.log(JSON.stringify(d));*/
		    return JSON.stringify(d);
		},
		dataSrc : "productEntities",
	    },
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'name'
	    }, {
		'data' : 'company'
	    }, {
		'data' : 'price'
	    }, {
		'data' : 'description'
	    } ],
	    select : "single"

	});

	$('#product-add-button').click(Computer.addProduct);
	$('#product-delete-button').click(Computer.deleteProduct);

	Computer.dataTable.on('select', function() {
	    $('#product-open-delete-modal-btn').prop('disabled', false);
	    $('#product-edit-modal-btn').prop('disabled', false);
	});

	Computer.dataTable.on('deselect', function() {
	    $('#product-open-delete-modal-btn').prop('disabled', true);
	    $('#product-edit-modal-btn').prop('disabled', true);
	});

	Computer.dataTable.on('draw', function() {
	    $('#product-open-delete-modal-btn').prop('disabled', true);
	    $('#product-edit-modal-btn').prop('disabled', true);
	});

	// When edit button is clicked initialize the dialog
	$('#product-edit-modal-btn').on('click', function() {
	    selectedData = Computer.dataTable.row('.selected').data();
	    $('#id').prop('readonly', true)
	    $('#product-add-modal #myModalLabel').data().mode = 'update';
	    $('#product-add-modal #myModalLabel').html('Edit product');
	    $('#name').val(selectedData.name);
	    $('#company').val(selectedData.company);
	    $('#description').val(selectedData.description);
	    $('#price').val(selectedData.price);
	    // $('#password').val(selectedData.password);
	});

	$('#product-add-modal #myModalLabel').data().mode = 'add';
	// reset data when modal hides
	$('#product-add-modal').on('hidden.bs.modal', function() {
	    $('#product-form')[0].reset();
	});
    };
    initPage();
});

Computer.addProduct = function(evt) {
    var formData = $('#product-form').serializeObject();
    var url = 'product/' + $('#product-add-modal #myModalLabel').data().mode + 'Product';
    if (selectedData != null) {
	formData.id = selectedData.id;
	selectedData = null;
    }
    /*console.log(formData);*/
    $.ajax({
	url : url,
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#product-add-modal").modal('hide');
	// to remove backdrop (grey screen)
	/*$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();	//
	 */$('#product-table').dataTable().fnReloadAjax();
    });
};

Computer.deleteProduct = function(evt) {
    //	console.log("sam in deleteProduct");
    var selectedId = Computer.dataTable.data()[Computer.dataTable.row('.selected')[0]].id;
    $.ajax({
	url : "product/deleteProduct?id=" + selectedId,
	type : 'DELETE',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$('#product-delete-modal').modal('hide');
	/*$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();*/
	$('#product-table').dataTable().fnReloadAjax();

    });
};