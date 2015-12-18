$(document).ready(function() {
    var initPage = function() {
	makeRequest();
	setInterval(makeRequest, (1000 * 5));
    };
    initPage();
});

makeRequest = function(evt) {
    $.ajax({
	url : 'taskmanagement/getAllNotifications',
	type : 'POST',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function(data) {
	$('.notification_tab').text('');
	var sam = 1;
	$.each(data, function(i) {
	    sam = 0;
	    var url = "";
	    if (data[i].value == 0) {
		var a = parseInt((data[i].title).split(":")[1]);
		url = "clientmanagement#table1,"+a;
	    } else if (data[i].value == 1) {
		var b = parseInt((data[i].description).split(":")[1]);
		url = "clientmanagement#table3,"+b;
	    } else if (data[i].value == 2) {
		var res = data[i].description.split("@");
		url = "taskmanagement#" + res[0] + ',' + data[i].reminder_time;
	    }
	    $('.notification_tab').append($('<div class="panel panel-success"><div class="panel-heading"><h2 class="panel-title"><a href="'+url+'" target="_blank">' + data[i].title + '</a></h2><button id="' + data[i].id + '" class="btn btn-default pull-right" onclick="deleteNotification(this.id)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button></div><div class="panel-body">' + data[i].description + '</div></div>'));

	});
	if (sam) {
	    $('.notification_tab').append($('<br><br><h4 style="color: #000" align="center">No New Notifications!!</h4>'));
	}
    });
}

function show_notif(url) {
    window.open(url, '_blank');
}

function deleteNotification(clicked_id) {
    $.ajax({
	url : 'taskmanagement/deleteNotificationById?id=' + clicked_id,
	type : 'DELETE',
	xhrFields : {
	    withCredentials : true
	}
    }).done(function() {
	makeRequest();
    });
};