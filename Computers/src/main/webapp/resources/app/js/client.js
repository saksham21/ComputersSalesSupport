var office_id = 0;
var tab1 = true;
var x = "Assigned";
var y;
var all_products = {};
var findBestProductsdata = {};
var searchRecdata = {};
var which_button = 1;
var product_save_data = {};
var g_flag = -1;
var g_cnt = 1000;
var dTable;
var existingClient_data = {};
var search_table1 = "";
var data_table2;

$(document).ready(function() {
    var initPage = function() {
	switchActiveTab('nav-clients');

	$.ajax({
	    url : 'clientmanagement/findUserByClientId3',
	    type : 'POST',
	    contentType : "application/json",
	    data : JSON.stringify({
		status : "EXISTING",
		draw : 0,
		start : 0,
		length : 10
	    })
	}).done(function(data) {
	    existingClient_data = data;
	});

	dTable = $('#tblGrid').DataTable({
	    "bResetDisplay" : false,
	    select : "single",
	    "info" : false,
	    "bLengthChange" : false
	});
	Computer.dataTable3 = $('#client-table-3').DataTable({
	    "bResetDisplay" : false,
	    select : "single",
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'name'
	    }, {
		'data' : 'contact'
	    }, {
		'data' : 'address'
	    }, {
		'data' : 'domain'
	    }, {
		'data' : 'last_order_time',
		render : function(data, a, b, c) {
		    return moment(new Date(data * 1000)).fromNow();
		}
	    }, {
		'data' : 'num_orders'
	    } ],

	    "bAutoWidth" : false,
	    "columnDefs" : [ {
		"width" : "5%",
		"targets" : 0
	    }, {
		"width" : "15%",
		"targets" : 1
	    }, {
		"width" : "10%",
		"targets" : 2
	    }, {
		"width" : "35%",
		"targets" : 3
	    }, {
		"width" : "10%",
		"targets" : 4
	    }, {
		"width" : "15%",
		"targets" : 5
	    }, {
		"width" : "10%",
		"targets" : 6
	    } ]
	});
	Computer.dataTable1 = $('#client-table-1').DataTable({
	    'bFilter' : true,
	    'ajax' : {
		url : 'clientmanagement/findUserByClientId1',
		type : 'POST',
		contentType : "application/json",
		data : function(d) {
		    delete (d.columns);
		    delete (d.order);
		    delete (d.search);
		    d.status = "";
		    return JSON.stringify(d);
		},
		dataSrc : "clientEntities",
		xhrFields : {
		    withCredentials : true
		}
	    },
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'name'
	    }, {
		'data' : 'contact'
	    }, {
		'data' : 'address'
	    }, {
		'data' : 'domain'
	    }, {
		'data' : 'status'
	    }, {
		'data' : 'employeename'
	    } ],
	    select : "single",
	    "fnRowCallback" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {

		$(nRow).children().each(function(index, td) {
		    if (index == 5) {

			if ($(td).html() === "ASSIGNED") {
			    $(td).css("color", "#60AD40");
			} else if ($(td).html() === "NOT_INTERESTED") {
			    $(td).css("color", "#FF3300");
			} else {
			    $(td).css("color", "#03A0D4");
			}
		    }
		});
		return nRow;
	    }

	});

	Computer.dataTable2 = $('#client-table-2').DataTable({
	    'bFilter' : true,
	    'ajax' : {
		url : 'clientmanagement/findUserByClientId',
		type : 'POST',
		contentType : "application/json",
		data : function(d) {
		    delete (d.columns);
		    delete (d.order);
		    delete (d.search);
		    d.status = "UNASSIGNED";
		    return JSON.stringify(d);
		},
		dataSrc : "clientEntities",
		xhrFields : {
		    withCredentials : true
		}
	    },
	    "columns" : [ {
		'data' : 'id'
	    }, {
		'data' : 'name'
	    }, {
		'data' : 'contact'
	    }, {
		'data' : 'address'
	    }, {
		'data' : 'domain'
	    } ],
	    select : "single"

	});

	var res = location.hash.split(",");
	if (res[0] == "#table1" || res[0] == "") {
	    var t = $('#t_1').index();
	    $('.nav-tabs li:eq(' + t + ') a').tab('show');
	    $('#client-add-btn').show();
	    $('#client-open-delete-modal-btn').show();
	    $('#client-reassign').show();
	    $('#client-assign').hide();
	    $('#assign-all-button').hide();
	    $('#client-schedule-meeting').show();
	    $('#order-open-select-modal-btn').show();
	    $('#order-send-modal-btn').show();
	    $('#product-find-button').show();
	    Computer.dataTable1.search(res[1]);
	} else if (res[0] == "#table2") {
	    var t = $('#t_2').index();
	    $('.nav-tabs li:eq(' + t + ') a').tab('show');
	    $('#client-add-btn').show();
	    $('#client-open-delete-modal-btn').show();
	    $('#client-reassign').hide();
	    $('#client-assign').show();
	    $('#assign-all-button').show();
	    $('#client-schedule-meeting').show();
	    $('#order-open-select-modal-btn').hide();
	    $('#order-send-modal-btn').hide();
	    $('#product-find-button').hide();
	} else {
	    var t = $('#t_3').index();
	    $('.nav-tabs li:eq(' + t + ') a').tab('show');
	    $('#client-add-btn').show();
	    $('#client-open-delete-modal-btn').show();
	    $('#client-reassign').hide();
	    $('#client-assign').show();
	    $('#assign-all-button').hide();
	    $('#client-schedule-meeting').show();
	    $('#order-open-select-modal-btn').hide();
	    $('#order-send-modal-btn').hide();
	    $('#product-find-button').hide();
	    Computer.dataTable3.search(res[1]);
	}

	$('.nav-tabs a').on('shown.bs.tab', function(event) {
	    which_button = 1;
	    x = $(event.target).text(); // active tab
	    y = $(event.relatedTarget).text(); // previous tab
	    $('#client-schedule-meeting').show();
	    $(".act span").text(x);
	    $(".prev span").text(y);
	    if (x != "Assigned") {
		if (x == "Existing") {
		    $('#show_contact_list').show();
		    $('#assign-all-button').hide();
		} else {
		    $('#show_contact_list').hide();
		    $('#assign-all-button').show();
		}
		$('#client-assign').show();
		$('#order-open-select-modal-btn').hide();
		$('#order-send-modal-btn').hide();
		$('#product-find-button').hide();
		$('#client-reassign').hide();
		$('#client-table-1').DataTable().row('.selected').deselect();

		$('#client-table-1').dataTable().fnReloadAjax();
		$('#client-table-2').dataTable().fnReloadAjax();
	    }
	    if (x != "Unassigned") {
		if (x == "Existing") {
		    $('#client-reassign').hide();
		    $('#show_contact_list').show();
		    $('#order-open-select-modal-btn').hide();
		    $('#order-send-modal-btn').hide();
		    $('#product-find-button').hide();
		    $('#client-assign').show();
		} else {
		    $('#client-reassign').show();
		    $('#show_contact_list').hide();
		    $('#order-open-select-modal-btn').show();
		    $('#order-send-modal-btn').show();
		    $('#product-find-button').show();
		    $('#client-assign').hide();
		}
		$('#assign-all-button').hide();
		$('#client-table-2').DataTable().row('.selected').deselect();

		$('#client-table-1').dataTable().fnReloadAjax();
		$('#client-table-2').dataTable().fnReloadAjax();
	    }
	    if (x != "Existing") {
		if (x == "Assigned") {
		    $('#client-assign').hide();
		    $('#assign-all-button').hide();
		    $('#client-reassign').show();
		    $('#order-open-select-modal-btn').show();
		    $('#order-send-modal-btn').show();
		    $('#product-find-button').show();
		} else {
		    $('#client-assign').show();
		    $('#assign-all-button').show();
		    $('#client-reassign').hide();
		    $('#order-open-select-modal-btn').hide();
		    $('#order-send-modal-btn').hide();
		    $('#product-find-button').hide();
		}
		$('#show_contact_list').hide();
		$('#client-table-3').DataTable().row('.selected').deselect();

		$('#client-table-1').dataTable().fnReloadAjax();
		$('#client-table-2').dataTable().fnReloadAjax();
	    }

	    $('#contact_list').val("0");
	    var data = existingClient_data;
	    Computer.dataTable3.clear();
	    $.each(data.clientEntitiesExisting, function(i, client) {
		Computer.dataTable3.row.add(client).draw(false);
	    });

	});

	$('#existing_client_tab').on('click', function() {
	    $('#contact_list').val("0");
	    var data = existingClient_data;
	    Computer.dataTable3.clear();
	    $.each(data.clientEntitiesExisting, function(i, client) {
		Computer.dataTable3.row.add(client).draw(false);
	    });
	});

	Computer.dataTable1.on('select', function() {
	    $('#client-assign').prop('disabled', true);
	    $('#client-reassign').prop('disabled', false);
	    $('#client-schedule-meeting').prop('disabled', false);
	    $('#order-open-select-modal-btn').prop('disabled', false);
	    $('#order-send-modal-btn').prop('disabled', false);
	    $('#product-find-button').prop('disabled', false);
	    $('#client-open-delete-modal-btn').prop('disabled', false);
	    $('.displayTrack').show(showTimeline());
	    var a = {};
	    g_flag = -1;
	    a["client_id"] = Computer.dataTable1.row('.selected').data().id;
	    getAllProductsFromCart(a);
	});

	Computer.dataTable1.on('deselect', function() {
	    $('#client-assign').prop('disabled', true);
	    $('#client-reassign').prop('disabled', true);
	    $('#client-schedule-meeting').prop('disabled', true);
	    $('#order-open-select-modal-btn').prop('disabled', true);
	    $('#order-send-modal-btn').prop('disabled', true);
	    $('#product-find-button').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	    $('.displayTrack').hide();
	    which_button = 1;
	    $('#current-order-button').removeClass("btn-default btn-primary");
	    $('#past-order-button').removeClass("btn-default btn-primary");
	    $('#current-order-button').addClass("btn-primary");
	    $('#past-order-button').addClass("btn-default");
	});

	Computer.dataTable1.on('draw', function() {
	    $('#client-assign').prop('disabled', true);
	    $('#client-reassign').prop('disabled', true);
	    $('#client-schedule-meeting').prop('disabled', true);
	    $('#order-open-select-modal-btn').prop('disabled', true);
	    $('#order-send-modal-btn').prop('disabled', true);
	    $('#product-find-button').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	    $('.displayTrack').hide();
	});

	Computer.dataTable2.on('select', function() {
	    $('#client-assign').prop('disabled', false);
	    $('#client-reassign').prop('disabled', true);
	    $('#client-schedule-meeting').prop('disabled', false);
	    $('#order-open-select-modal-btn').prop('disabled', false);
	    $('#order-send-modal-btn').prop('disabled', true);
	    $('#product-find-button').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', false);
	    $('.displayTrack').show(showTimeline());
	});

	Computer.dataTable2.on('deselect', function() {
	    $('#client-assign').prop('disabled', true);
	    $('#client-reassign').prop('disabled', true);
	    $('#client-schedule-meeting').prop('disabled', true);
	    $('#order-open-select-modal-btn').prop('disabled', true);
	    $('#order-send-modal-btn').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	    $('#product-find-button').prop('disabled', true);
	    $('.displayTrack').hide();
	    which_button = 1;
	    $('#current-order-button').removeClass("btn-default btn-primary");
	    $('#past-order-button').removeClass("btn-default btn-primary");
	    $('#current-order-button').addClass("btn-primary");
	    $('#past-order-button').addClass("btn-default");
	});

	Computer.dataTable2.on('draw', function() {
	    $('#client-assign').prop('disabled', true);
	    $('#client-reassign').prop('disabled', true);
	    $('#client-schedule-meeting').prop('disabled', true);
	    $('#order-open-select-modal-btn').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	    $('#order-send-modal-btn').prop('disabled', true);
	    $('#product-find-button').prop('disabled', true);
	    $('.displayTrack').hide();
	});

	Computer.dataTable3.on('select', function() {
	    $('.displayTrack').show(showTimeline());
	    $('#client-assign').prop('disabled', false);
	    $('#client-schedule-meeting').prop('disabled', false);
	    $('#client-open-delete-modal-btn').prop('disabled', false);
	});

	Computer.dataTable3.on('deselect', function() {
	    $('#client-assign').prop('disabled', true);
	    $('.displayTrack').hide();
	    $('#client-schedule-meeting').prop('disabled', true);
	    which_button = 1;
	    $('#current-order-button').removeClass("btn-default btn-primary");
	    $('#past-order-button').removeClass("btn-default btn-primary");
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	    $('#current-order-button').addClass("btn-primary");
	    $('#past-order-button').addClass("btn-default");
	});

	Computer.dataTable3.on('draw', function() {
	    $('#client-assign').prop('disabled', true);
	    $('.displayTrack').hide();
	    $('#client-schedule-meeting').prop('disabled', true);
	    $('#client-open-delete-modal-btn').prop('disabled', true);
	});

	$('#client-assign').on('click', function() {
	    if (x == "Unassigned") {
		selectedData = Computer.dataTable2.row('.selected').data();
	    } else if (x == "Existing") {
		selectedData = Computer.dataTable3.row('.selected').data();
	    }
	    $('#id').val(selectedData.id);
	    $('#id').prop('readonly', true);
	    $('#name').val(selectedData.name);
	    $('#name').prop('readonly', true);

	    $.ajax({
		url : 'employeemanagement/findUserByEmpId',
		type : 'POST',
		contentType : "application/json",
		data : JSON.stringify({
		    officeId : 0,
		    draw : 0,
		    start : 0,
		    length : 10
		})
	    }).done(function(data) {
		$('.ac_1').text('');
		$('.ac_2').text('');
		$.each(data.recommendedEmp, function(i, emp) {
		    $('.take_from_autocomplete').find('optgroup[label="Recommended Representatives"]').append($('<option value="' + emp.username + '">' + emp.username + "   ,    " + emp.clients_to_meet + '</option>'));
		});

		$.each(data.employeeAccountEntities, function(i, emp) {
		    if (emp.role == "Representative") {
			$('.take_from_autocomplete').find('optgroup[label="All Representatives"]').append($('<option value="' + emp.username + '">' + emp.username + "   ,    " + emp.clients_to_meet + '</option>'));
		    }
		});

		$(".take_from_autocomplete").select2();
	    });
	});

	$('#contact_list').on('change', function() {
	    var data = existingClient_data;
	    Computer.dataTable3.clear();

	    var d = Math.floor((new Date()).getTime() / 1000);

	    $.each(data.clientEntitiesExisting, function(i, client) {
		if ((d - client.last_order_time) >= $('#contact_list').val() * 2592000) {
		    Computer.dataTable3.row.add(client).draw(false);
		}
	    });
	    Computer.dataTable3.draw();
	});

	$('.rp2').select2({
	    'width' : '100%'
	});

	$('.rp3').select2({
	    'width' : '100%'
	});

	$('.rp4').select2({
	    'width' : '100%'
	});

	$('.rp7').select2({
	    'width' : '100%'
	});

	$('#assign-all-button').on('click', function() {
	    var formdata = {};
	    formdata["status"] = "UNASSIGNED";
	    formdata["draw"] = 0;
	    formdata["start"] = 0;
	    formdata["length"] = 10;
	    $.ajax({
		url : 'clientmanagement/findUserByClientIdToAssignAll',
		type : 'POST',
		contentType : "application/json",
		data : JSON.stringify(formdata)
	    }).done(function() {
		alert("Clients Assigned");
		$('#client-table-1').dataTable().fnReloadAjax();
		$('#client-table-2').dataTable().fnReloadAjax();
	    });
	});

	$('#client-reassign').on('click', function() {
	    if (x == "Unassigned") {
		selectedData = Computer.dataTable2.row('.selected').data();
	    } else if (x == "Existing") {
		selectedData = Computer.dataTable3.row('.selected').data();
	    }
	    $('#id1').val(selectedData.id);
	    $('#id1').prop('readonly', true);
	    $('#name1').val(selectedData.name);
	    $('#name1').prop('readonly', true);
	    $('#employeename1').val(selectedData.employeename);

	    $.ajax({
		url : 'employeemanagement/findUserByEmpId',
		type : 'POST',
		contentType : "application/json",
		data : JSON.stringify({
		    officeId : 0,
		    draw : 0,
		    start : 0,
		    length : 10
		})
	    }).done(function(data) {
		$('.ac1_1').text('');
		$('.ac1_2').text('');
		$.each(data.recommendedEmp, function(i, emp) {
		    $('.take_from_autocomplete1').find('optgroup[label="Recommended Representatives"]').append($('<option value="' + emp.username + '">' + emp.username + "   ,    " + emp.clients_to_meet + '</option>'));
		});

		$.each(data.employeeAccountEntities, function(i, emp) {
		    if (emp.role == "Representative") {
			$('.take_from_autocomplete1').find('optgroup[label="All Representatives"]').append($('<option value="' + emp.username + '">' + emp.username + "   ,    " + emp.clients_to_meet + '</option>'));
		    }
		});

		$(".take_from_autocomplete1").select2();

	    });
	});

	$('#order-send-modal-btn').on('click', function() {
	    selectedData = Computer.dataTable1.row('.selected').data();
	    $('#id4').val(selectedData.id);
	    $('#id4').prop('readonly', true);
	    $('#name4').val(selectedData.name);
	    $('#name4').prop('readonly', true);
	});

	$('#save-order-button').click(Computer.addNewOrder);

	$('#order-open-select-modal-btn').on('click', function() {

	    $('.add-order-form').text('');
	    selectedData = $('#client-table-1').DataTable().row('.selected').data();
	    $('#id3').val(selectedData.id);
	    var v = selectedData.id;
	    var product_s_data = product_save_data;
	    var a_val = 0;
	    var p_price = 0;
	    if (product_s_data.length == 0) {
		$('.add-order-form').append('<div class="form-group"><label for="id3">Client ID</label><input name="id3" type="number" class="form-control" id="id3" value="' + v + '" readonly /></div><div class="form-group"><h4>Place Order</h4><div class="container add_here"><div class="row" id="0"><select class="form-control product-selects col-xs-1" name="name"  id="name"></select><input name="quantity" type="number" step="1" class="form-control col-xs-1 pull-right" style="width: 15%; margin-right: 40%" id="quantity" placeholder="Quantity"><input name="price" type="number" step="0.01" class="form-control col-xs-1 pull-right product_price" style="width: 15%; margin-right: 2%" id="price" placeholder="Price"><span class="input-group-btn col-xs-1 pull-right" style="margin-right: -34%"><button class="btn btn-success btn-add multiple_orders" type="button"><span class="glyphicon glyphicon-plus"></span></button></span></div></div></div>');
		$(".product-selects").select2({
		    placeholder : "Select a Product"
		});

		$.ajax({
		    url : 'product/getAllProducts',
		    type : 'POST'
		}).done(function(data) {
		    all_products = data;
		    $.each(data.productEntities, function(i, office) {
			$('.product-selects').append($('<option value="' + office.id + '">' + office.name + '</option>'));
		    });
		    $('.product_price').val(data.productEntities[0].price);
		    $(".product-selects").select2({
			placeholder : "Select a Product"
		    }).on('change', function() {
			var p_id_flag = $(this)[0].value;
			$.each(data.productEntities, function(i, office) {
			    if (office.id == p_id_flag) {
				$('.product_price').val(office.price);
				return false;
			    }
			});
		    });
		});

	    } else {
		a_val = product_s_data[0].product_id;
		$('.add-order-form').append('<div class="form-group"><label for="id3">Client ID</label><input name="id3" type="number" class="form-control" id="id3" value="' + v + '" readonly /></div><div class="form-group"><h4>Place Order</h4><div class="container add_here"><div class="row" id="0"><select class="product-selects col-xs-1" name="name"  id="name"></select><input name="quantity" type="number" step="1" class="form-control col-xs-1 pull-right" style="width: 15%; margin-right: 40%" id="quantity" value="' + product_s_data[0].quantity + '" placeholder="Quantity"><input name="price" type="number" step="0.01" class="form-control col-xs-1 pull-right product_price" style="width: 15%; margin-right: 2%" id="price" placeholder="Price"><span class="input-group-btn col-xs-1 pull-right" style="margin-right: -34%"><button class="btn btn-success btn-add multiple_orders" type="button"><span class="glyphicon glyphicon-plus"></span></button></span></div></div></div>');
		$(".product-selects").select2({
		    placeholder : "Select a Product"
		});

		var $example = $(".product-selects").select2();

		$.ajax({
		    url : 'product/getAllProducts',
		    type : 'POST'
		}).done(function(data) {
		    all_products = data;
		    $.each(data.productEntities, function(i, office) {
			if (office.id == a_val) {
			    p_price = office.price;
			}
			$('.product-selects').append($('<option value="' + office.id + '">' + office.name + '</option>'));
		    });
		    $example.val(a_val).trigger("change");
		    $('.product_price').val(p_price);
		    $(".product-selects").select2({
			placeholder : "Select a Product"
		    }).on('change', function() {
			var p_id_flag = $(this)[0].value;
			$.each(data.productEntities, function(i, office) {
			    if (office.id == p_id_flag) {
				$('.product_price').val(office.price);
				return false;
			    }
			});
		    });
		});

		for (var i = 1; i < product_s_data.length; i++) {
		    addMoreFields(product_s_data[i].product_id, product_s_data[i].quantity, i);
		}
	    }

	    $(".multiple_orders").on('click', function() {
		addMoreFields(0, 0, g_cnt++);
	    });
	});

	$('#client-assign-modal').on('hidden.bs.modal', function() {
	    $('#client-assign-form')[0].reset();
	});

	$('#client-reassign-modal').on('hidden.bs.modal', function() {
	    $('#client-reassign-form')[0].reset();
	});

	$('#client-assign-button').click(Computer.assign);
	$('#client-reassign-button').click(Computer.reassign);

	$('#startDate').daterangepicker({
	    "showDropdowns" : true,
	    "showWeekNumbers" : true,
	    "timePicker" : true,
	    "timePickerIncrement" : 5,
	    "autoApply" : true,
	    "locale" : {
		"format" : "YYYY-MM-DD:HH:mm",
		"separator" : " to "
	    }
	});

	$('#startDate1').datetimepicker({
	    format : "YYYY-MM-DD:HH:mm"
	});

	$('#client-schedule-meeting').on('click', function() {
	    if (x == "Assigned")
		selectedData = $('#client-table-1').DataTable().row('.selected').data();
	    else if (x == "Existing")
		selectedData = $('#client-table-3').DataTable().row('.selected').data();
	    $('#client_id').val(selectedData.id);
	    $('#client_id').prop('readonly', true);
	    $('#title').val("Meeting with Client: " + selectedData.id);
	});

	$('#status-send-button').click(Computer.sendStatus);
	$('#event-add-button').click(addEvent);

	$('#set_reminder').hide();
	$("#rp9").slider({
	    formatter : function(value) {
		return 'Current value: ' + value;
	    }
	});

    };
    initPage();
});

function addMoreFields(a, b, cnt) {

    if (a == 0 && b == 0) {
	$('.add_here').append('<div class="row" id="' + cnt + '"><br><select class="form-control product-selects' + cnt + ' col-xs-1" name="name"  id="name"></select><input name="quantity" type="number" step="1" class="form-control col-xs-1 pull-right" style="width: 15%; margin-right: 40%" id="quantity" placeholder="Quantity"><input name="price" type="number" step="0.01" class="form-control col-xs-1 pull-right product_price' + cnt + '" style="width: 15%; margin-right: 2%" id="price" placeholder="Price"><span class="input-group-btn col-xs-1 pull-right" style="margin-right: -34%"><button class="btn btn-success btn-danger delete_order" id=' + cnt + ' type="button"><span class="glyphicon glyphicon-minus"></span></button></span></div>')
	$('.product-selects' + cnt).select2({
	    placeholder : "Select a Product"
	});

	$('.delete_order').on('click', function() {
	    $("div").remove("#" + $(this).attr('id'));
	});

	$.ajax({
	    url : 'product/getAllProducts',
	    type : 'POST',
	    contentType : "application/json"
	}).done(function(data) {
	    all_products = data;
	    $.each(data.productEntities, function(i, office) {
		$('.product-selects' + cnt).append($('<option value="' + office.id + '">' + office.name + '</option>'));
	    });
	    $('.product_price' + cnt).val(data.productEntities[0].price);
	    $(".product-selects" + cnt).select2({
		placeholder : "Select a Product"
	    }).on('change', function() {
		var p_id_flag = $(this)[0].value;
		$.each(data.productEntities, function(i, office) {
		    if (office.id == p_id_flag) {
			$('.product_price' + cnt).val(office.price);
			return false;
		    }
		});
	    });

	});
    } else {
	$('.add_here').append('<div class="row" id="' + cnt + '"><br><select class="form-control product-selects' + cnt + ' col-xs-1" name="name"  id="name"></select><input name="quantity" type="number" step="1" class="form-control col-xs-1 pull-right" style="width: 15%; margin-right: 40%" id="quantity" placeholder="Quantity" value="' + b + '"><input name="price" type="number" step="0.01" class="form-control col-xs-1 pull-right product_price' + cnt + '" style="width: 15%; margin-right: 2%" id="price" placeholder="Price"><span class="input-group-btn col-xs-1 pull-right" style="margin-right: -34%"><button class="btn btn-success btn-danger delete_order" id=' + cnt + ' type="button"><span class="glyphicon glyphicon-minus"></span></button></span></div>')
	$('.product-selects' + cnt).select2({
	    placeholder : "Select a Product"
	});

	$('.delete_order').on('click', function() {
	    $("div").remove("#" + $(this).attr('id'));
	});

	var $example = $(".product-selects" + cnt).select2();
	var p_price = 0;
	$.ajax({
	    url : 'product/getAllProducts',
	    type : 'POST',
	    contentType : "application/json"
	}).done(function(data) {
	    all_products = data;
	    $.each(data.productEntities, function(i, office) {
		if (office.id == a) {
		    p_price = office.price;
		}
		$('.product-selects' + cnt).append($('<option value="' + office.id + '">' + office.name + '</option>'));
	    });
	    $example.val(a).trigger("change");
	    // alert(p_price)
	    $('.product_price' + cnt).val(p_price);
	    $(".product-selects" + cnt).select2({
		placeholder : "Select a Product"
	    }).on('change', function() {
		var p_id_flag = $(this)[0].value;
		$.each(data.productEntities, function(i, office) {
		    if (office.id == p_id_flag) {
			$('.product_price' + cnt).val(office.price);
			return false;
		    }
		});
	    });
	});
    }

};

var showTimelineAjax = function(id) {
    if (which_button == 1) {
	$.ajax({
	    url : 'clientmanagement/createOrderTimeline',
	    type : 'POST',
	    data : ({
		id : id
	    }),
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function(data) {
	    var sam = 1;
	    var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
	    $('.wallmessages').text('');
	    $.each(data, function(i) {
		var date = new Date(data[i].creation_time * 1000);
		var str = date.getHours() + ":";
		if (date.getMinutes() < 10) {
		    str = str + "0" + date.getMinutes();
		} else {
		    str = str + date.getMinutes();
		}
		str = str + " " + months[date.getMonth()] + " " + date.getDate() + "," + date.getFullYear();
		var str_append = '<div class="message-item" id="' + data[i].id + '"><div class="message-inner"><div class="message-head clearfix"><div class="message-icon pull-left"><a href="#"><i class="glyphicon glyphicon-check"></i></a></div><div class="user-detail"><h5 class="handle">' + data[i].title + '</h5><div class="post-type"><p>-By ' + data[i].creator + '</p></div><div class="post-time"><p><i class="glyphicon glyphicon-time"></i>' + str + '</p></div></div></div><div class="qa-message-content"><p>';
		if (data[i].description == "" || data[i].description == null) {
		    str_append = str_append + '</p></div></div></div>';
		} else {
		    console.log(data[i].description);
		    str_append = str_append + data[i].description + '</p></div></div></div>';
		}
		$('.wallmessages').prepend($(str_append));
		sam = 0;
	    });
	    if (sam) {
		$('.wallmessages').prepend($('<div class="message-item"><div class="message-inner"><div class="message-head clearfix"><div class="user-detail"><h5 class="handle">Please assign this client a Representative by pressing assign button</h5></div></div></div></div></div>'));
	    }
	});

    } else {
	$.ajax({
	    url : 'clientmanagement/createPastOrderTimeline',
	    type : 'POST',
	    data : ({
		id : id
	    }),
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function(data_all) {
	    counter = 0;
	    var sam = 1;
	    var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
	    $('.wallmessages').text('');
	    while (data_all.length > 0) {
		sam = 0;
		var idx = 1;
		for (i = 1; i < data_all.length; i++) {
		    if (data_all[i - 1].order_id == data_all[i].order_id) {
			idx = i + 1;
		    } else {
			break;
		    }
		}
		var data = data_all.slice(0, idx);
		for (i = 0; i < idx; i++) {
		    data_all.shift();
		}
		counter++;
		$('#wallmessages').prepend($('<div><div class="panel panel-success" style="padding-top:1% !important">' + '<div class="panel-heading"><div class="panel-heading"><h4 class="panel-title">' + '<button type="button" class="pull-left btn btn-success btn-sm buttons" onclick=\"displayOrderWise(' + counter + ')\"  my_id="' + counter + '" >' + '<span id="c' + counter + '" class="glyphicon glyphicon-plus"></span>' + '</button>' + 'Order#' + counter + ', OrderID:' + data[0].order_id + '</h4></div></div></div>' + '<div id="message' + counter + '"></div></div>'));

		$.each(data, function(i) {
		    var date = new Date(data[i].creation_time * 1000);
		    var str = date.getHours() + ":";
		    if (date.getMinutes() < 10) {
			str = str + "0" + date.getMinutes();
		    } else {
			str = str + date.getMinutes();
		    }
		    str = str + " " + months[date.getMonth()] + " " + date.getDate() + "," + date.getFullYear();
		    $('#message' + counter).prepend($('<div class="message-item" id="' + data[i].id + '">' + '<div class="message-inner"><div class="message-head clearfix"><div class="message-icon pull-left">' + '<a href="#"><i class="glyphicon glyphicon-check"></i></a></div>' + '<div class="user-detail"><h5 class="handle">' + data[i].title + '</h5>' + '<div class="post-type"><p>-By ' + data[i].creator + '</p></div><div class="post-time"><p>' + '<i class="glyphicon glyphicon-time"></i>' + str + '</p></div></div></div>' + '<div class="qa-message-content"><p>' + data[i].description + '</p></div></div></div>'));
		    $('#message' + counter).hide();
		    sam = 0;
		});
	    }
	    if (sam) {
		$('.wallmessages').prepend($('<div class="message-item"><div class="message-inner"><div class="message-head clearfix"><div class="user-detail"><h5 class="handle">There are no past orders for this Client</h5></div></div></div></div></div>'));
	    }
	});
    }
}

function displayOrderWise(id) {
    $('#c' + id).toggleClass('glyphicon-plus glyphicon-minus');
    $('#message' + id).toggle();
}

showTimeline = function(evt) {
    if (x == "Assigned") {
	selectedData = Computer.dataTable1.row('.selected').data();
    } else if (x == "Unassigned") {
	selectedData = Computer.dataTable2.row('.selected').data();
    } else {
	selectedData = Computer.dataTable3.row('.selected').data();
    }
    // console.log(selectedData);
    var id = selectedData.id;
    showTimelineAjax(id);
}

currentOrderFlag = function(evt) {
    which_button = 1;
    $('#current-order-button').toggleClass('btn-primary btn-default');
    $('#past-order-button').toggleClass('btn-primary btn-default');
    showTimeline();
}

pastOrderFlag = function(evt) {
    which_button = 2;
    $('#current-order-button').toggleClass('btn-primary btn-default');
    $('#past-order-button').toggleClass('btn-primary btn-default');
    showTimeline();
}

searchRec = function(evt) {
    var formData = $('#rp_Form').serializeObject();
    var r = (document.getElementById('rp9').value).split(",");
    formData.start = r[0];
    formData.end = r[1];
    formData.ram = formData.rp4;
    formData.processor = formData.rp7;
    formData.storage = formData.rp3;
    formData.brand = formData.rp2;
    delete (formData.rp2);
    delete (formData.rp3);
    delete (formData.rp4);
    delete (formData.rp5);
    delete (formData.rp6);
    delete (formData.rp7);
    delete (formData.rp9);

    $.ajax({
	url : 'salesmanagement/getRecSearchBased',
	type : 'POST',
	data : JSON.stringify(formData),
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function(data) {
	searchRecdata = data;
	show_searchRec();
    });

};

newModalLoad = function(evt) {
    $('.addifno').text('');
    // $('.tblGrid').text('');
    dTable.clear();
    $('#tblGrid').parents('div.dataTables_wrapper').first().hide();
    $('.abc').show();
    $('.rp').show();
    $('.model_load').hide();
    $('.searchRecProducts').show();

};

findBestProducts = function(evt) {

    $.ajax({
	url : 'product/getAllProducts',
	type : 'POST'
    }).done(function(data) {
	all_products = data;
	var company_list = {};
	// alert("done")
	$('.rp2').append($('<option value="">Any Brand</option>'));
	$.each(data.productEntities, function(i, office) {
	    if (company_list[office.company]) {
		// do nothing
	    } else {
		company_list[office.company] = true;
		$('.rp2').append($('<option value="' + office.company + '">' + office.company + '</option>'));
	    }
	});
    })

    selectedData = Computer.dataTable1.row('.selected').data();

    var formdata = {};
    formdata["client_id"] = selectedData.id;
    getAllProductsFromCart(formdata);

    var domain = selectedData.domain;
    delete (selectedData);
    $.ajax({
	url : 'salesmanagement/getRecDomainBased',
	type : 'POST',
	data : ({
	    domain : domain
	}),
	xhrFields : {
	    withCredentials : true
	}
    }).done(function(data) {
	findBestProductsdata = data;
	show_findBestProducts();
    });
};

function show_searchRec() {

    var product_list = product_save_data;
    var data = searchRecdata;
    $('.searchRecProducts').hide();
    $('.abc').hide();
    $('.rp').hide();
    $('.addifno').text('');
    dTable.clear();
    dTable.draw();
    $('#tblGrid').parents('div.dataTables_wrapper').first().show();
    $('.model_load').show();
    var sam = 0;
    $.each(data, function(i) {
	sam = 1;
    });
    if (!sam) {
	$('.addifno').append($('<label>Please ask for some other input. Nothing suitable found!! Sorry. </label>'));
    } else {
	$('.addifno').append($('<label>Best products based on Client requirements.</label>'));
	$.each(data, function(i) {
	    var flag = 0;
	    var index = 0;
	    $.each(product_list, function(j) {
		if (product_list[j].product_id == data[i].id) {
		    flag = 1;
		    index = j;
		    return false;
		}
	    });

	    if (flag == 0) {
		dTable.row.add([ data[i].id, data[i].name, data[i].company, data[i].price, "<input id=in" + data[i].id + " class='form-control' type='number' value='1' />", "<button id=bu" + data[i].id + " type='button' class='btn btn-default' value=" + data[i].id + ">Save Product</button>" ]).draw(false);
		$('#bu' + data[i].id).on('click', function() {
		    g_flag = 0;
		    add_to_cart($(this), $('#in' + data[i].id).val());
		});
	    } else {
		dTable.row.add([ data[i].id, data[i].name, data[i].company, data[i].price, "<input id=in" + data[i].id + " class='form-control' type='number' value=" + product_list[index].quantity + " />", "<button id=bu" + data[i].id + " type='button' class='btn btn-success' value=" + data[i].id + ">Product Saved</button>" ]).draw(false);
		$('#bu' + data[i].id).on('click', function() {
		    g_flag = 0;
		    add_to_cart($(this), $('#in' + data[i].id).val());
		});

		$('#in' + data[i].id).change(function() {

		    var f_data = {};
		    f_data["client_id"] = $('#client-table-1').DataTable().row('.selected').data().id;
		    f_data["product_id"] = data[i].id
		    f_data["quantity"] = $('#in' + data[i].id).val();
		    updateQuantity(f_data);

		});
	    }

	});
	$('#tblGrid').parents('div.dataTables_wrapper').first().show();
    }
    $('#recommendedProducts').modal('show');

}

function timeConverter(a) {
    var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
    return time;
}

function show_findBestProducts() {

    var product_list = product_save_data;
    var data = findBestProductsdata;
    $('.addifno').text('');
    // $('.tblGrid').text('');
    dTable.clear();
    dTable.search('');
    $('#tblGrid').parents('div.dataTables_wrapper').first().show();
    $('.abc').hide();
    $('.rp').hide();
    $('.searchRecProducts').hide();
    $('.model_load').show();
    var sam = 0;
    $.each(data, function(i) {
	sam = 1;
	return false;
    });
    if (!sam) {
	$('.addifno').append($('<label>Please ask for manual input. Nothing suitable found!! Sorry. </label>'));
    } else {
	$('.addifno').append($('<label>Some Trending Products in this Domain.</label>'));
	$.each(data, function(i) {
	    var flag = 0;
	    var index = 0;
	    $.each(product_list, function(j) {
		if (product_list[j].product_id == data[i].id) {
		    flag = 1;
		    index = j;
		    return false;
		}
	    });

	    if (flag == 0) {
		dTable.row.add([ data[i].id, data[i].name, data[i].company, data[i].price, "<input id='in" + data[i].id + "' class='form-control' type='number' value='1' />", "<button id='bu" + data[i].id + "' type='button' class='btn btn-default' value='" + data[i].id + "'>Save Product</button>" ]).draw(false);
		$('#bu' + data[i].id).on('click', function() {
		    g_flag = 1;
		    add_to_cart($(this), $('#in' + data[i].id).val());
		});
	    } else {
		dTable.row.add([ data[i].id, data[i].name, data[i].company, data[i].price, "<input id='in" + data[i].id + "' class='form-control' type='number' value='" + product_list[index].quantity + "' />", "<button id='bu" + data[i].id + "' type='button' class='btn btn-success' value='" + data[i].id + "'>Product Saved</button>" ]).draw(false);
		$('#bu' + data[i].id).on('click', function() {
		    add_to_cart($(this), $('#in' + data[i].id).val());
		    g_flag = 1;
		});
		$('#in' + data[i].id).change(function() {
		    var f_data = {};
		    f_data["client_id"] = $('#client-table-1').DataTable().row('.selected').data().id;
		    f_data["product_id"] = data[i].id;
		    f_data["quantity"] = $('#in' + data[i].id).val();
		    updateQuantity(f_data);

		});
	    }
	});
    }

    $('#recommendedProducts').modal('show');
};

function showAllProducts() {

    $('.addifno').text('');
    $('.abc').hide();
    $('.rp').hide();
    $('.model_load').show();
    $('.searchRecProducts').hide();
    dTable.clear();
    /* dTable.draw(); */
    $('#tblGrid').parents('div.dataTables_wrapper').first().show();
    var client_product_save_data = product_save_data;
    var company_product_data = all_products.productEntities;
    var strg1 = "";
    $('.addifno').append($('<label>All Product List</label>'));

    $.each(company_product_data, function(i) {
	var flag = -1;
	$.each(client_product_save_data, function(j) {
	    if (client_product_save_data[j].product_id == company_product_data[i].id) {
		flag = j;
	    }
	});
	if (flag == -1) {
	    strg1 = "Save Product";
	    dTable.row.add([ company_product_data[i].id, company_product_data[i].name, company_product_data[i].company, company_product_data[i].price, "<input id='in" + company_product_data[i].id + "' class='form-control' type='number' value='1' />", "<button id='bu" + company_product_data[i].id + "' onclick=\"p_call('" + company_product_data[i].id + "','" + strg1 + "')\" type='button' class='btn btn-default' value='" + company_product_data[i].id + "'>Save Product</button>" ]).draw(false);
	} else {
	    strg1 = "Product Saved";
	    dTable.row.add([ company_product_data[i].id, company_product_data[i].name, company_product_data[i].company, company_product_data[i].price, "<input id='in" + company_product_data[i].id + "' onchange=\"quantity_call('" + company_product_data[i].id + "')\" class='form-control' type='number' value='" + client_product_save_data[flag].quantity + "' />", "<button id='bu" + company_product_data[i].id + "' onclick=\"p_call('" + company_product_data[i].id + "','" + strg1 + "')\" type='button' class='btn btn-success' value='" + company_product_data[i].id + "'>Product Saved</button>" ]).draw(false);
	}
    });
};

function quantity_call(id) {
    var f_data = {};
    f_data["client_id"] = $('#client-table-1').DataTable().row('.selected').data().id;
    f_data["product_id"] = id;
    f_data["quantity"] = $('#in' + id).val();
    updateQuantity(f_data);
};

function p_call(id, str) {
    g_flag = 2;
    var formdata = {};
    formdata["client_id"] = $('#client-table-1').DataTable().row('.selected').data().id;
    formdata["product_id"] = id;
    formdata["quantity"] = $('#in' + id).val();
    if (str == "Save Product") {

	// ajax to add in db
	$.ajax({
	    url : 'salesmanagement/addProductInCart',
	    data : JSON.stringify(formdata),
	    type : 'POST',
	    contentType : 'application/json',
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function() {
	    // if things gets slower, then remove this ajax call
	    getAllProductsFromCart(formdata);
	    alert("product added");

	});

    } else {
	// ajax to delete from db
	$.ajax({
	    url : 'salesmanagement/deleteProductFromCart',
	    data : JSON.stringify(formdata),
	    type : 'POST',
	    contentType : 'application/json',
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function() {
	    // if things gets slower, then remove this ajax call
	    getAllProductsFromCart(formdata);

	    alert("product removed");

	});
    }
}

function updateQuantity(f_data) {

    $.ajax({
	url : 'salesmanagement/updateQuantity',
	data : JSON.stringify(f_data),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	for (var i = 0; i < product_save_data.length; i++) {
	    if (product_save_data[i].client_id == f_data["client_id"] && product_save_data[i].product_id == f_data["product_id"]) {
		product_save_data[i].quantity = f_data["quantity"];
		return false;
	    }
	}
    });

};

function add_to_cart(product_id, x) {
    var formdata = {};
    var myDivElement = $(product_id);
    formdata["client_id"] = $('#client-table-1').DataTable().row('.selected').data().id;
    formdata["product_id"] = myDivElement[0].value;
    formdata["quantity"] = x;
    if (myDivElement[0].innerHTML == "Save Product") {

	// ajax to add in db
	$.ajax({
	    url : 'salesmanagement/addProductInCart',
	    data : JSON.stringify(formdata),
	    type : 'POST',
	    contentType : 'application/json',
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function() {
	    // if things gets slower, then remove this ajax call
	    getAllProductsFromCart(formdata);
	    alert("product added");

	});

    } else {
	// ajax to delete from db
	$.ajax({
	    url : 'salesmanagement/deleteProductFromCart',
	    data : JSON.stringify(formdata),
	    type : 'POST',
	    contentType : 'application/json',
	    xhrFields : {
		withCredentials : true
	    }
	}).done(function() {
	    // if things gets slower, then remove this ajax call
	    getAllProductsFromCart(formdata);

	    alert("product removed");

	});
    }
}

function getAllProductsFromCart(formdata) {

    $.ajax({
	url : 'salesmanagement/getAllProductsFromCart',
	data : JSON.stringify(formdata),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function(data) {
	product_save_data = data;
	if (g_flag == -1) {
	    g_flag = 1;
	} else {
	    if (g_flag == 1) {
		show_findBestProducts();
	    } else if (g_flag == 0) {
		show_searchRec();
	    } else {
		showAllProducts();
	    }
	}
    });

};

function addFields() {
    $('#no_button').removeClass("btn-info");
    $('#no_button').addClass("btn-default");
    $('#yes_button').removeClass("btn-default");
    $('#yes_button').addClass("btn-info");
    $('#set_reminder').show();
}

function removeFields() {
    $('#no_button').removeClass("btn-default");
    $('#no_button').addClass("btn-info");
    $('#yes_button').removeClass("btn-info");
    $('#yes_button').addClass("btn-default");
    $('#set_reminder').hide();
}

Computer.sendStatus = function(evt) {
    var formData = $('#status-send-form').serializeObject();
    formData.id = formData.id4;
    formData.status = formData.status4;
    formData.description = formData.description4;
    delete (formData.name4);
    delete (formData.id4);
    delete (formData.status4);
    delete (formData.description4);
    var table_id = Computer.dataTable1.row('.selected').data().id;
    $.ajax({
	url : 'clientmanagement/sendStatus',
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#sendStatus").modal('hide');
	// to remove backdrop (grey screen)
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
	$('#client-table-1').dataTable().fnReloadAjax();
	// showTimeline();
	// Computer.dataTable1.row(table_id).addClass('selected');
    });
};

Computer.assign = function(evt) {
    var formData = $('#client-assign-form').serializeObject();
    if (x == "Unassigned")
	selectedData = Computer.dataTable2.row('.selected').data();
    else if (x == "Assigned")
	selectedData = Computer.dataTable3.row('.selected').data();
    formData.status = "ASSIGNED";
    formData.domain = selectedData.domain;
    $.ajax({
	url : 'clientmanagement/assignClients',
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#client-assign-modal").modal('hide');
	// to remove backdrop (grey screen)
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove(); //
	$('#client-table-1').dataTable().fnReloadAjax();
	$('#client-table-2').dataTable().fnReloadAjax();
	table3_reload();
    });
};

Computer.reassign = function(evt) {
    var formData = $('#client-reassign-form').serializeObject();
    formData.status = "INTERESTED";
    formData.id = formData.id1;
    formData.employeename = formData.employeename1;
    delete (formData.id1);
    delete (formData.employeename1);
    $.ajax({
	url : 'clientmanagement/assignClients',
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#client-reassign-modal").modal('hide');
	// to remove backdrop (grey screen)
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove(); //
	$('#client-table-1').dataTable().fnReloadAjax();
	$('#client-table-2').dataTable().fnReloadAjax();
	table3_reload();
    });
};

function table3_reload() {
    Computer.dataTable3.clear();
    $.ajax({
	url : 'clientmanagement/findUserByClientId3',
	type : 'POST',
	contentType : "application/json",
	data : JSON.stringify({
	    status : "EXISTING",
	    draw : 0,
	    start : 0,
	    length : 10
	})
    }).done(function(data) {
	existingClient_data = data;
	$.each(data.clientEntitiesExisting, function(i, client) {
	    Computer.dataTable3.row.add(client).draw(false);
	});
	Computer.dataTable3.draw();
    });
}

function parsedatetime(x) {
    var r = x.split(" to ");
    var startdateandtime = r[0];
    var enddateandtime = r[1];
    var parsedobject = {};
    parsedobject["start"] = Date.parse(startdateandtime) / 1000;
    parsedobject["end"] = Date.parse(enddateandtime) / 1000;

    return parsedobject;
}

addEvent = function(evt) {
    var formData = $('#event-form').serializeObject();
    var ob = parsedatetime(formData.startDate);
    formData.start = ob["start"];
    formData.end = ob["end"];
    formData.reminder = Date.parse(formData.startDate1) / 1000;
    delete (formData.startDate);
    delete (formData.startDate1);

    $.ajax({
	url : 'taskmanagement/addClientEvent',
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#event-add-modal").modal('hide');
	$("#calendar").fullCalendar('refetchEvents');
	showTimeline();
    });
};

Computer.addNewOrder = function(evt) {
    var formData = $('#add-order-form').serializeObject();
    formData.id = formData.id3;
    delete (formData.id3); // formData contains clientId, product details
    if (typeof (formData.name) === 'string') {
	formData.name = [ formData.name ];
	formData.quantity = [ formData.quantity ];
	formData.price = [ formData.price ];
    }
    formData.status = "EXISTING";
    $.ajax({
	url : 'salesmanagement/addNewOrder',
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	},
	success : function() {
	    $('#addOrder').modal('hide');
	    $('body').removeClass('modal-open');
	    $('.modal-backdrop').remove();
	    $('#client-table-1').dataTable().fnReloadAjax();
	    $('#client-table-2').dataTable().fnReloadAjax();
	}
    }).done();
}
