$(document).ready(function() {
    var initPage = function() {
	switchActiveTab('nav-office');

	Computer.dataTable = $('#office-table').DataTable({
	    'serverSide' : true,
	    'ajax' : {
		url : 'offices',
		type : 'POST',
		contentType : "application/json",
		data : function(d) {
		    delete (d.columns);
		    delete (d.order);
		    delete (d.search);
		    return JSON.stringify(d);
		},
		dataSrc : "offices"
	    },
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'name'
	    } ],
	    select : "single"

	});

	$('#office-add-button').click(Computer.addOffice);
	$('#office-delete-button').click(Computer.deleteOffice);

	Computer.dataTable.on('select', function() {
	    $('#office-open-delete-modal-btn').prop('disabled', false);
	});

	Computer.dataTable.on('deselect', function() {
	    $('#office-open-delete-modal-btn').prop('disabled', true);
	});

	Computer.dataTable.on('draw', function() {
	    $('#office-open-delete-modal-btn').prop('disabled', true);
	});

	$('#office-add-modal').on('hidden.bs.modal', function() {
	    $('#office-form')[0].reset();
	});
    };

    initPage();
});

Computer.addOffice = function(evt) {

    var formData = $('#office-form').serializeObject();
    $.ajax({
	url : "office",
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$('#office-add-modal').modal('hide');
	$('#office-table').dataTable().fnReloadAjax();

    });
};

Computer.deleteOffice = function(evt) {
    var selectedId = Computer.dataTable.data()[Computer.dataTable.row('.selected')[0]].id;
    $.ajax({
	url : "office?id=" + selectedId,
	type : 'DELETE',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$('#office-delete-modal').modal('hide');
	$('#office-table').dataTable().fnReloadAjax();

    });
};