var selectedData = null;
var officeSelects;
var office_data;
var req_values;
var office_stats;
var date;
var month;
var year;

var switchActiveTab = function(id) {
    $.each($('#computer-navbar').children(), function(i, el) {
	if (el.id === id) {
	    $(el).addClass('active');
	} else {
	    $(el).removeClass('active');
	}
    });
};

$(document).ready(function() {
    var initPage = function() {
	switchActiveTab('nav-dashboard');
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
	    officeSelects = $('.office-selects');
	    $.each(data.offices, function(i, office) {
		$.each(officeSelects, function(i, select) {
		    $(select).append($('<option data-display = "' + office.name + '" value="' + office.id + '">' + office.name + '</option>'));
		});
	    });
	});

	$.ajax({
	    url : 'getAllRequiredValues',
	    type : 'POST'
	}).done(function(data) {
	    req_values = data;
	    $('.sales_per_order').text('');
	    $('.sales_per_order').append(data.average_sale_order);
	    $('#monthly_sales').text('');
	    $('#monthly_sales').append(data.monthly_sales);
	    $('#annual_sales').text('');
	    $('#annual_sales').append(data.annual_sales);
	    $('.active_leads_office').text('');
	    $('.active_leads_office').append(data.active_leads);
	    $('.rep_in_office').text('');
	    $('.rep_in_office').append(data.representatives);
	    $('.leads_generated_office').text('');
	    $('.leads_generated_office').append(data.leads_generated);

	    var today = new Date();
	    var day_year = today.getDOY();
	    var days_in_month = daysInMonth(today.getMonth() + 1, today.getFullYear());
	    var no_days = day_year - today.getDate() + days_in_month;

	    if (data.assigned_monthly_sales)
		var i1 = ((data.monthly_sales) / (data.assigned_monthly_sales)) * 100;
	    else
		var i1 = 0;
	    var i2 = (today.getDate() / days_in_month) * 100;
	    if (data.assigned_annual_sales)
		var i3 = ((data.annual_sales) / (data.assigned_annual_sales)) * 100;
	    else
		var i3 = 0;
	    var i4 = (day_year / no_days) * 100;

	    i1 = Math.round(i1 * 100) / 100;
	    i2 = Math.round(i2 * 100) / 100;
	    i3 = Math.round(i3 * 100) / 100;
	    i4 = Math.round(i4 * 100) / 100;

	    if (i1 > i2) {
		$('.msales_bar1').append("<span><span class='glyphicon glyphicon-arrow-up' aria-hidden='true'></span>" + Math.round((i1 - i2) * 100) / 100 + "%</span>");
		$('.msales_bar1').css("color", "green");
	    } else {
		$('.msales_bar1').append("<span><span class='glyphicon glyphicon-arrow-down' aria-hidden='true'></span>" + Math.round((i2 - i1) * 100) / 100 + "%</span>");
		$('.msales_bar1').css("color", "red");
	    }

	    if (i3 > i4) {
		$('.msales_bar2').append("<span><span class='glyphicon glyphicon-arrow-up' aria-hidden='true'></span>" + Math.round((i3 - i4) * 100) / 100 + "%</span>");
		$('.msales_bar2').css("color", "green");
	    } else {
		$('.msales_bar2').append("<span><span class='glyphicon glyphicon-arrow-down' aria-hidden='true'></span>" + Math.round((i4 - i3) * 100) / 100 + "%</span>");
		$('.msales_bar2').css("color", "red");
	    }

	    $('#mbar1').text('');
	    $('#mbar2').text('');
	    $('#mbar3').text('');
	    $('#mbar4').text('');

	    $('#mbar1').append("<span>" + i1 + "%</span>");
	    $('#mbar2').append("<span>" + i2 + "%</span>");
	    $('#mbar3').append("<span>" + i3 + "%</span>");
	    $('#mbar4').append("<span>" + i4 + "%</span>");

	    if (i1 > 100.0) {
		$('#mbar1').css("width", 100 + "%");
	    } else {
		$('#mbar1').css("width", i1 + "%");
	    }
	    $('#mbar2').css("width", i2 + "%");
	    if (i3 > 100.0) {
		$('#mbar3').css("width", 100 + "%");
	    } else {
		$('#mbar3').css("width", i3 + "%");
	    }
	    $('#mbar4').css("width", i4 + "%");

/*	    console.log((data.assigned_annual_sales - data.annual_sales));
	    console.log((no_days - day_year));*/
	    var str = "";
	    if (days_in_month != today.getDate()) {
		var c = 1;
		if ((data.assigned_monthly_sales - data.monthly_sales)>0 && Math.round((i2 - i1) * 100) / 100 > 10.0) {
		    str += c + ". In order to achieve the monthly sales target, sales of <label>" + Math.floor((data.assigned_monthly_sales - data.monthly_sales) / (days_in_month - today.getDate())) + "</label> per day has to be made in remaining month.<br>";
		    c++
		}
		if ((data.assigned_annual_sales - data.annual_sales)>0 && (Math.round((i4 - i3) * 100) / 100 > 30.0)) {
		    str += c + ". In order to achieve the annual sales target, sales of <label>" + Math.floor((data.assigned_annual_sales - data.annual_sales) / (no_days - day_year)) + "</label> per day has to be made till this month end.<br>";
		}
	    } else {
		var c = 1;
		if ((data.assigned_monthly_sales - data.monthly_sales)>0 && Math.round((i2 - i1) * 100) / 100 > 10.0) {
		    str += c + ". In order to achieve the monthly sales target, sales of <label>" + Math.floor((data.assigned_monthly_sales - data.monthly_sales)) + "</label> has to be made today.<br>";
		    c++
		}
		if ((data.assigned_annual_sales - data.annual_sales)>0 && (Math.round((i4 - i3) * 100) / 100 > 30.0)) {
		    str += c + ". In order to achieve the annual sales target, sales of <label>" + Math.floor((data.assigned_annual_sales - data.annual_sales)) + "</label> has to be made today.<br>";
		}
	    }

	    if (str.length > 0) {
		$('.show_office_sales_manager').append($('<span style="color:red"><br> Note:<br> ' + str + '</span><'));
		$('#recommend_inhouse').show();
		$('#contact_existing_clients').show();
	    }
	    
	    $('#recommend_inhouse').on('click', function() {
		var win = window.open('taskmanagement#1', '_blank');
	    });
	    
	    $('#contact_existing_clients').on('click', function() {
		var win = window.open('clientmanagement#table3', '_blank');
	    });

	    Morris.Donut({
		element : 'morris-donut-chart',
		data : [ {
		    label : "Completed",
		    value : req_values.monthly_sales
		}, {
		    label : "Remaining",
		    value : Math.max((req_values.assigned_monthly_sales - req_values.monthly_sales),0)
		} ],
		resize : true,
		colors : [ '#5cb85c', '#337ab7' ]
	    });

	    Morris.Donut({
		element : 'morris-donut-chart1',
		data : [ {
		    label : "Completed",
		    value : req_values.annual_sales
		}, {
		    label : "Remaining",
		    value : Math.max((req_values.assigned_annual_sales - req_values.annual_sales),0)
		} ],
		resize : true,
		colors : [ '#5cb85c', '#337ab7' ]
	    });

	});

	gothere();

	show_stats();

	$('#growth').change(function() {
	    update_fields($('#growth').val());
	});

	$('#director_broadcast').on('click', function() {
	    $('#status_form').show();
	});

	$('#msg_broadcast').on('click', function() {
	    $('#comment').val('');
	    $('#status_form').hide();
	});

	$('#view_office_sales1').on('click', function() {
	    $('.set_target').hide();
	    $('.show_office_sales').show();
	});

	$('#view_office_sales2').on('click', function() {
	    $('.set_target').hide();
	    $('.show_office_sales').show();
	});

	$('#set_target_btn').on('click', function() {
	    $('.show_office_sales').hide();
	    $('.set_target').show();
	});

	$('#set_target_sales_btn').on('click', function() {
	    data = office_data;
	    $.each(data, function(i) {
		data[i].past_sales = $('#' + data[i].id).val();
	    });

	    $.ajax({
		url : 'setSalesTarget',
		type : 'POST',
		contentType : "application/json",
		data : JSON.stringify(data)
	    }).done(function() {
		alert("Sales Target Set");
		$('.set_target').hide();
	    });

	});

	$.ajax({
	    url : 'getAllOfficeStats',
	    type : 'POST'
	}).done(function(data) {
	    office_stats = data;
	});

	$('#employee-office-filter').change(function() {
	    $('#status_form').hide();
	    data = office_stats;
	    $('.officewise_stats').show();

	    $.each(data, function(i) {
		if (data[i].id == $('#employee-office-filter').val()) {

		    var today = new Date();
		    var day_year = today.getDOY();
		    var days_in_month = daysInMonth(today.getMonth() + 1, today.getFullYear());
		    var no_days = day_year - today.getDate() + days_in_month;

		    if (data[i].assigned_monthly_sales)
			var i1 = ((data[i].monthly_sales) / (data[i].assigned_monthly_sales)) * 100;
		    else
			var i1 = 0;
		    var i2 = (today.getDate() / days_in_month) * 100;
		    if (data[i].assigned_annual_sales)
			var i3 = ((data[i].annual_sales) / (data[i].assigned_annual_sales)) * 100;
		    else
			var i3 = 0;
		    var i4 = (day_year / no_days) * 100;

		    i1 = Math.round(i1 * 100) / 100;
		    i2 = Math.round(i2 * 100) / 100;
		    i3 = Math.round(i3 * 100) / 100;
		    i4 = Math.round(i4 * 100) / 100;

		    $('.dsales_bar1').text('');
		    $('.dsales_bar2').text('');
		    if (i1 > i2) {
			$('.dsales_bar1').append("<span><span class='glyphicon glyphicon-arrow-up' aria-hidden='true'></span>" + (i1 - i2).toFixed(2) + "%</span>");
			$('.dsales_bar1').css("color", "green");
		    } else {
			$('.dsales_bar1').append("<span><span class='glyphicon glyphicon-arrow-down' aria-hidden='true'></span>" + Math.round((i2 - i1) * 100) / 100 + "%</span>");
			$('.dsales_bar1').css("color", "red");
		    }

		    if (i3 > i4) {
			$('.dsales_bar2').append("<span><span class='glyphicon glyphicon-arrow-up' aria-hidden='true'></span>" + Math.round((i3 - i4) * 100) / 100 + "%</span>");
			$('.dsales_bar2').css("color", "green");
		    } else {
			$('.dsales_bar2').append("<span><span class='glyphicon glyphicon-arrow-down' aria-hidden='true'></span>" + Math.round((i4 - i3) * 100) / 100 + "%</span>");
			$('.dsales_bar2').css("color", "red");
		    }

		    $('#bar1').text('');
		    $('#bar2').text('');
		    $('#bar3').text('');
		    $('#bar4').text('');

		    $('#bar1').append("<span>" + i1 + "%</span>");
		    $('#bar2').append("<span>" + i2 + "%</span>");
		    $('#bar3').append("<span>" + i3 + "%</span>");
		    $('#bar4').append("<span>" + i4 + "%</span>");

		    if (i1 > 100.0) {
			$('#bar1').css("width", 100 + "%");
		    } else {
			$('#bar1').css("width", i1 + "%");
		    }
		    $('#bar2').css("width", i2 + "%");
		    if (i3 > 100.0) {
			$('#bar3').css("width", 100 + "%");
		    } else {
			$('#bar3').css("width", i3 + "%");
		    }
		    $('#bar4').css("width", i4 + "%");

		    $('.show_office_sales_director').text('');
		    var str = "";
		    if (days_in_month != today.getDate()) {
			if ((data[i].assigned_monthly_sales - data[i].monthly_sales) / (days_in_month - today.getDate()) > 1.5 * (data[i].assigned_monthly_sales / days_in_month)) {
			    str = "In order to achieve the monthly sales target, sales of <label>" + Math.floor((data[i].assigned_monthly_sales - data[i].monthly_sales) / (days_in_month - today.getDate())) + "</label> per day has to be made in remaining month in this office.";
			}
		    } else {
			if (data[i].assigned_monthly_sales - data[i].monthly_sales >= 0) {
			    str = "In order to achieve the monthly sales target, sales of <label>" + Math.floor((data[i].assigned_monthly_sales - data[i].monthly_sales) / (days_in_month - today.getDate())) + "</label> has to be made today in this office.";
			}
		    }

		    if (str.length > 0) {
			$('.show_office_sales_director').append($('<span style="color:red"><br> Note: ' + str + '<br><br></span><'));
		    }

		}
	    });

	});

    };
    initPage();
});

var months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];

Date.prototype.getDOY = function() {
    var onejan = new Date(this.getFullYear(), 0, 1);
    return Math.ceil((this - onejan) / 86400000);
}

function daysInMonth(month, year) {
    return new Date(year, month, 0).getDate();
}

function gothere() {
    date = new Date();
    month = date.getMonth();
    year = date.getFullYear();
    if (month == 11) {
	month = 0;
	year++;
    } else {
	month++;
    }
    $('.month_year').text('');
    $('.month_year').append($('<span>' + months[month] + ', ' + year + '</span>'));
};

function show_stats() {

    $.ajax({
	url : 'addSalesTarget',
	type : 'POST',
    }).done(function(data) {
	office_data = data;
	update_fields(5);
    });
};

function update_fields(rate) {

    data = office_data;
    $('.add_offices').text('');
    $.each(data, function(i) {
	input = 100000;
	if (data[i].past_emp > 0) {
	    input = data[i].past_sales * data[i].current_emp;
	    input = input / data[i].past_emp;
	    input = input * 100 + input * rate;
	    input = input / 100;
	    input = Math.floor(input);
	}

	$('.add_offices').append('<div class="panel panel-primary form-group"><div class="panel-heading clearfix"><div class="row">' + '<h4 class="panel-title pull-left col-md-4" style="padding-top: 1.5%;">' + data[i].name + '</h4><span>PAST: #' + data[i].past_emp + ' , ' + data[i].past_sales + ' : NOW: #' + data[i].current_emp + ' </span>' + '<div class="input-group pull-right" style="width:25%;"><input id="' + data[i].id + '" type="number" class="form-control" value="' + input + '" ></div></div></div></div>');
    });

};