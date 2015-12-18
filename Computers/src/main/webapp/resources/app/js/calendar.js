var id_event;
var flag = 0;
var underEmployeeDataList = {};
var eventData = null;
var calendar;
var flag_calendar = 0;
var url_val;

$(document).ready(function() {
    Date.prototype.yyyymmdd = function() {
	var hh = this.getHours().toString();
	var mm = this.getMinutes().toString();
	return hh + ":" + mm;
    };

    var initPage = function() {

	if (location.hash == "#1") {
	    $('#agenda').val('To discuss the decrease of sales activity');
	    $('#inhousemeeting-modal').modal('show');
	} else if (location.hash == "") {
	} else {
	    flag_calendar = 1;
	    // alert(location.hash);
	    url_val = location.hash.split(",");
	    /*
	     * console.log(url_val[0]); console.log(url_val[1]);
	     * console.log("eventData:")
	     */;
	    call_calendar();
	}

	switchActiveTab('nav-calendar');
	var date = moment().format("");

	if (flag_calendar == 0) {
	    call_calendar();
	}

	var calendar1 = $('#calendar1').fullCalendar({
	    height : 550,
	    header : {
		left : 'prev next today',
		center : 'title',
		right : 'month,agendaWeek,agendaDay'
	    },
	    buttonText : {
		today : 'today',
		month : 'month',
	    },
	    events : function(start, end, timezone, callback) {
		$.ajax({
		    "url" : "taskmanagement/getAllEventsOfEmployees",
		    "data" : JSON.stringify({
			username : $('#namesid').text(),
			start : start.unix(),
			end : end.unix()
		    }),
		    "type" : 'POST',
		    "contentType" : 'application/json',
		    "xhrFields" : {
			withCredentials : true
		    }
		}).done(function(data) {
		    var events = [];
		    $.each(data.taskEntities, function(i) {
			events.push({
			    id : data.taskEntities[i].id,
			    title : data.taskEntities[i].title,
			    start : moment(data.taskEntities[i].start * 1000),
			    end : moment(data.taskEntities[i].end * 1000),
			    description : data.taskEntities[i].description,
			    creator : data.taskEntities[i].creator
			});

		    });

		    callback(events);
		});
	    }
	});

	$('#inhousemeeting-modal').on('shown.bs.modal', function() {
	    $("#calendar1").fullCalendar('render');
	});

	$('#startDate').daterangepicker({
	    "showDropdowns" : true,
	    "showWeekNumbers" : true,
	    "timePicker" : true,
	    "timePickerIncrement" : 30,
	    "autoApply" : true,
	    "locale" : {
		"format" : "YYYY-MM-DD:HH:mm",
		"separator" : " to "
	    }
	});

	$('#startDate1').daterangepicker({
	    "showDropdowns" : true,
	    "showWeekNumbers" : true,
	    "timePicker" : true,
	    "timePickerIncrement" : 30,
	    "autoApply" : true,
	    "locale" : {
		"format" : "YYYY-MM-DD:HH:mm",
		"separator" : " to "
	    }
	});

	$('#event-add-button').click(addEvent);
	$('#event-add-modal #myModalLabel').data().mode = 'add';

	$(".salesman-selects").select2({
	    width : '100%'
	}).on("change", function(event) {
	    var valu = $(this).val();
	    if (flag == 0) {
		$('#checkbox').attr('checked', false);
	    } else {
		flag = 0;
	    }
	    if (valu == null) {
		valu = "";
	    }
	    $('#namesid').text(valu);
	    $('#calendar1').fullCalendar('refetchEvents');
	});

	$.ajax({
	    url : 'employeemanagement/getUnderEmployees',
	    type : 'POST'
	}).done(function(data) {
	    underEmployeeDataList = data;
	    var salesmanselects = $('.salesman-selects');
	    var salesmanselects1 = $('.salesman-selects1');
	    $.each(data, function(i) {
		$(salesmanselects).append($('<option data-display = "' + data[i].username + '" value="' + data[i].id + '">' + data[i].username + '</option>'));
		$(salesmanselects1).append($('<option data-display = "' + data[i].username + '" value="' + data[i].id + '">' + data[i].username + '</option>'));

	    });
	});

	$('#datetime-name').daterangepicker({
	    "showDropdowns" : true,
	    "showWeekNumbers" : true,
	    "timePicker" : true,
	    "timePickerIncrement" : 30,
	    "autoApply" : true,
	    "locale" : {
		"format" : "YYYY-MM-DD:HH:mm",
		"separator" : " to "
	    }
	});

	$('#startDate2').datetimepicker({
	    format : "YYYY-MM-DD:HH:mm"
	});

	$('#startDate3').datetimepicker({
	    format : "YYYY-MM-DD:HH:mm"
	});

	$('#set_reminder').hide();

	$('#inhousemeeting-save-button').click(addInHouseMeetingEvent);

	$('#event-edit-button').click(editEvent);
	$('#event-delete-button').click(deleteEvent);

	$('#checkbox').on('click', function() {
	    var checked = $("#checkbox").is(":checked");
	    if (checked == true) {
		flag = 1;
		addAllMembers();
	    } else {
		removeAllMembers();
	    }
	});

    };
    initPage();
});

function call_calendar() {
    var calendar = $('#calendar').fullCalendar({
	header : {
	    left : 'prev next today',
	    center : 'title',
	    right : 'month,agendaWeek,agendaDay'
	},
	buttonText : {
	    today : 'today',
	    month : 'month',
	},
	eventLimit : true,

	dayClick : function(date, jsEvent, view) {
	    $('#event-add-modal').modal();
	},
	beforeShow : function(i) {
	    if ($(i).attr('readonly')) {
		return false;
	    }
	},
	eventClick : function(event, jsEvent, view) {
	    $('#title1').val(event.title);
	    $('#title1').prop('readonly', true);
	    $('#startDate1').val(moment(event.start).format("YYYY-MM-DD:HH:mm") + " to " + moment(event.end).format("YYYY-MM-DD:HH:mm"));
	    $('#startDate1').attr('readonly', 'readonly');
	    $('#description1').val(event.description);
	    $('#description1').prop('readonly', true);
	    $('#startDate3').val(moment(event.reminder).format("YYYY-MM-DD:HH:mm"));
	    id_event = event.id;
	    $('#event-click-modal').modal('show');
	},

	events : function(start, end, timezone, callback) {

	    $.ajax({
		url : 'taskmanagement/getAllEvents',
		type : 'POST',
		contentType : "application/json",
		data : JSON.stringify({
		    start : start.unix(),
		    end : end.unix(),
		    username : $("#employee_username").text()
		}),
		dataSrc : "taskEntities",
	    }).done(function(data) {
		eventData = data;
		var events = [];
		var my_event = [];
		$.each(data.taskEntities, function(i, task) {
		    if (flag_calendar == 1) {
			if (url_val[0] == ('#' + task.title) && url_val[1] == task.reminder) {
			    my_event.push({
				id : task.id,
				title : task.title,
				start : moment(task.start * 1000),
				end : moment(task.end * 1000 + 1),
				description : task.description,
				creator : task.creator,
				reminder : moment(task.reminder * 1000)
			    });
			}
		    }
		    events.push({
			id : task.id,
			title : task.title,
			start : moment(task.start * 1000),
			end : moment(task.end * 1000 + 1),
			description : task.description,
			creator : task.creator,
			reminder : moment(task.reminder * 1000)

		    });
		});
		callback(events);
		if (flag_calendar == 1) {
		    $('#title1').val(my_event[0].title);
		    $('#title1').prop('readonly', true);
		    $('#startDate1').val(moment(my_event[0].start).format("YYYY-MM-DD:HH:mm") + " to " + moment(my_event[0].end).format("YYYY-MM-DD:HH:mm"));
		    $('#startDate1').attr('readonly', 'readonly');
		    $('#description1').val(my_event[0].description);
		    $('#description1').prop('readonly', true);
		    $('#startDate3').val(moment(my_event[0].reminder).format("YYYY-MM-DD:HH:mm"));
		    id_event = my_event[0].id;
		    $('#event-click-modal').modal('show');
		    flag_calendar = 0;
		}
	    });
	}
    });

};

function editEvent() {
    var formData = $('#event-form1').serializeObject();
    var formdata = {};
    var ob = parsedatetime(formData.startDate1);
    formdata["id"] = id_event;
    formdata["start"] = ob["start"];
    formdata["end"] = ob["end"];
    formdata["title"] = formData.title1;
    formdata["creator"] = formData.creator1;
    formdata["description"] = formData.description1;
    formdata["reminder"] = Date.parse(formData.startDate3) / 1000;

    $.ajax({
	"url" : "taskmanagement/editEvent",
	"data" : JSON.stringify(formdata),
	"type" : 'POST',
	"contentType" : 'application/json',
	"xhrFields" : {
	    withCredentials : true
	}
    }).done(function() {
	$('#event-click-modal').modal('hide');
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
	$("#calendar").fullCalendar('refetchEvents');
    });
};

function deleteEvent() {
    var formData = $('#event-form1').serializeObject();
    var formdata = {};
    var ob = parsedatetime(formData.startDate1);
    formdata["id"] = id_event;

    $.ajax({
	"url" : "taskmanagement/deleteEvent",
	"data" : JSON.stringify(formdata),
	"type" : 'POST',
	"contentType" : 'application/json',
	"xhrFields" : {
	    withCredentials : true
	}
    }).done(function() {
	$('#event-click-modal').modal('hide');
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
	$("#calendar").fullCalendar('refetchEvents');
    });
};

function addFields() {
    $('#no_button').removeClass("btn-info");
    $('#no_button').addClass("btn-default");
    $('#yes_button').removeClass("btn-default");
    $('#yes_button').addClass("btn-info");
    $('#set_reminder').show();
};

function removeFields() {
    $('#no_button').removeClass("btn-default");
    $('#no_button').addClass("btn-info");
    $('#yes_button').removeClass("btn-info");
    $('#yes_button').addClass("btn-default");
    $('#set_reminder').hide();
};

function parsedatetime(x) {
    var r = x.split(" to ");
    var startdateandtime = r[0];
    var enddateandtime = r[1];
    var parsedobject = {};
    parsedobject["start"] = Date.parse(startdateandtime) / 1000;
    parsedobject["end"] = Date.parse(enddateandtime) / 1000;

    return parsedobject;
};

addInHouseMeetingEvent = function(evt) {

    var datetime = $('#datetime-name').val();
    var agenda = $('#agenda').val();
    var peopleid = $('#namesid').text();
    var ob = parsedatetime(datetime);

    if (peopleid != null) {
	if (peopleid != "") {

	    var formdata = {};
	    formdata["start"] = ob["start"];
	    formdata["end"] = ob["end"];
	    formdata["username"] = peopleid;
	    formdata["description"] = agenda;

	    $.ajax({
		"url" : "taskmanagement/createInHouseMeeting",
		"data" : JSON.stringify(formdata),
		"type" : 'POST',
		"contentType" : 'application/json',
		"xhrFields" : {
		    withCredentials : true
		}
	    }).done(function() {
		$('#inhousemeeting-modal').modal('hide');
		$("#calendar").fullCalendar('refetchEvents');

	    });

	}
    }
};

addEvent = function(evt) {
    var formData = $('#event-form').serializeObject();
    var ob = parsedatetime(formData.startDate);
    formData.start = ob["start"];
    formData.end = ob["end"];
    delete (formData.startDate);
    var url = 'taskmanagement/' + $('#event-add-modal #myModalLabel').data().mode + 'Event';
    $.ajax({
	url : url,
	data : JSON.stringify(formData),
	type : 'POST',
	contentType : 'application/json',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	$("#event-add-modal").modal('hide');
	$("#calendar").fullCalendar('refetchEvents');
    });
};

function addAllMembers() {
    var valu = "";
    if (underEmployeeDataList.length == 0) {
	valu = "";
    } else {
	var stringVal = [];
	for (var i = 0; i < underEmployeeDataList.length - 1; i++) {
	    valu += underEmployeeDataList[i].id;
	    valu += ",";
	    stringVal.push(underEmployeeDataList[i].id);
	}
	valu += underEmployeeDataList[underEmployeeDataList.length - 1].id;
	stringVal.push(underEmployeeDataList[underEmployeeDataList.length - 1].id);
	$('#salesman-selects').val(stringVal).trigger("change");
    }
    $('#namesid').text(valu);
    $('#calendar1').fullCalendar('refetchEvents');
};

function removeAllMembers() {
    var valu = "";
    var stringVal = [];
    $('#salesman-selects').val(stringVal).trigger("change");
    $('#namesid').text(valu);
    $('#calendar1').fullCalendar('refetchEvents');
};