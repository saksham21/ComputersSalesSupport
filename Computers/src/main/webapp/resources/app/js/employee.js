var selectedData = null;
$(document).ready(function() {
    var initPage = function() {
	switchActiveTab('nav-employee');

	Computer.dataTable = $('#employee-table').DataTable({
	    'serverSide' : true,
	    'bFilter' : true,
	    'ajax' : {
		url : 'employeemanagement/findUserByOfficeId',
		type : 'POST',
		contentType : "application/json",
		data : function(d) {
		    // send only data required by backend API
		    delete (d.columns);
		    delete (d.order);
		    delete (d.search);
		    d.officeId = $('#employee-office-filter').val();
		    return JSON.stringify(d);
		},
		dataSrc : "employeeAccountEntities",
	    },
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'firstname'
	    }, {
		'data' : 'lastname'
	    }, {
		'data' : 'username'
	    }, {
		'data' : 'email'
	    }, {
		'data' : 'officename'
	    }, {
		'data' : 'role'
	    } ],
	    select : "single"

	});

	$.ajax({
	    url : 'offices',
	    type : 'POST',
	    contentType : "application/json",
	    data : JSON.stringify({
		draw : 0,
		start : 0,
		length : 10
	    })
	}).done(function(data) {
	    var officeSelects = $('.office-selects');
	    $.each(data.offices, function(i, office) {
		$.each(officeSelects, function(i, select) {
		    $(select).append($('<option data-display = "' + office.name + '" value="' + office.id + '">' + office.name + '</option>'));
		    /*console.log(select);*/
		});
	    });
	});

	$('#employee-add-button').click(Computer.addEmployee);
	$('#employee-delete-button').click(Computer.deleteEmployee);
	$('#employee-office-filter').change(function() {
	    $('#employee-table').dataTable().fnReloadAjax();
	})

	// disable delete button if nothing selected
	Computer.dataTable.on('select', function() {
	    $('#employee-open-delete-modal-btn').prop('disabled', false);
	    $('#employee-edit-modal-btn').prop('disabled', false);
	});

	Computer.dataTable.on('deselect', function() {
	    $('#employee-open-delete-modal-btn').prop('disabled', true);
	    $('#employee-edit-modal-btn').prop('disabled', true);
	});

	Computer.dataTable.on('draw', function() {
	    $('#employee-open-delete-modal-btn').prop('disabled', true);
	    $('#employee-edit-modal-btn').prop('disabled', true);
	});

	// When edit button is clicked initialize the dialog
	$('#employee-edit-modal-btn').on('click', function() {
	    selectedData = Computer.dataTable.row('.selected').data();
	    $('#id').prop('readonly', true)
	    $('#employee-add-modal #myModalLabel').data().mode = 'update';
	    $('#employee-add-modal #myModalLabel').html('Edit employee');
	    $('#firstname').val(selectedData.firstname);
	    $('#lastname').val(selectedData.lastname);
	    $('#username').val(selectedData.username);
	    $('#email').val(selectedData.email);
	    $('#officename').val(selectedData.officename);
	    /*$('#officename option[data-display='+selectedData.officename+']').attr('selected', true);*/
	    $('#role').val(selectedData.role);
	    // $('#password').val(selectedData.password);
	});

	$('#employee-add-modal #myModalLabel').data().mode = 'add';
	// reset data when modal hides
	$('#employee-add-modal').on('hidden.bs.modal', function() {
	    $('#employee-form')[0].reset();
	});

    };

    initPage();
});

Computer.addEmployee = function(evt) {
    var formData = $('#employee-form').serializeObject();
    console.log(JSON.stringify(formData));
    var url = 'employeemanagement/' + $('#employee-add-modal #myModalLabel').data().mode + 'UserAccount';
    if (selectedData != null) {
	formData.id = selectedData.id;
	selectedData = null;
    }
    $.ajax({
	url : url,
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#employee-add-modal").modal('hide');
	// to remove backdrop (grey screen)
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove(); //
	$('#employee-table').dataTable().fnReloadAjax();
    });
};

Computer.deleteEmployee = function(evt) {
    var selectedId = Computer.dataTable.data()[Computer.dataTable.row('.selected')[0]].id;
    $.ajax({
	url : "employeemanagement/deleteUserAccount?id=" + selectedId,
	type : 'DELETE',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$('#employee-delete-modal').modal('hide');
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
	$('#employee-table').dataTable().fnReloadAjax();

    });
};
