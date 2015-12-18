var pyrmont = {
    lat : 23.2500,
    lng : 77.4170
};
var map;
var geocoder;
var infowindow;
var service;
var client_data;
var view_button = 0;
/*var my_circle = {
 path : google.maps.SymbolPath.CIRCLE,
 fillColor : "#FF0000",
 fillOpacity : .4,
 scale : 4.5,
 strokeColor : 'white',
 strokeWeight : 1
 };*/

$(document).ready(function() {
    switchActiveTab('nav-googlemaps');

    $('#radius_size').slider({
	formatter : function(value) {
	    return 'Current value: ' + value;
	}
    });

    var dTable = $('#map_clients_table').DataTable({
	"bResetDisplay" : false,
	select : "single"
    });
    dTable.column(4).visible(false);
    dTable.column(5).visible(false);
    dTable.column(6).visible(false);

    dTable.on('select', function() {
	$('#add_new_clients').prop('disabled', false);
	$('#unwanted_clients').prop('disabled', false);
    });

    dTable.on('deselect', function() {
	$('#add_new_clients').prop('disabled', true);
	$('#unwanted_clients').prop('disabled', true);
    });

    $('#add_new_clients').click(function() {

	var index = dTable.row('.selected').index();
	var formData = {};
	var clientname = dTable.cell(index, 0).data();
	var contact = dTable.cell(index, 2).data();
	var address = dTable.cell(index, 1).data();
	var status = "UNASSIGNED";
	var employeeid = "0";
	var employeename = "NOT AVAILABLE";
	var domain = dTable.cell(index, 3).data();

	formData['name'] = clientname;
	formData['contact'] = contact;
	formData['address'] = address;
	formData['status'] = status;
	formData['domain'] = domain;
	formData['employeeid'] = employeeid;
	formData['employeename'] = employeename;
	formData['placeid'] = dTable.cell(index, 4).data();
	formData['enabled'] = "1";
	formData['lat'] = dTable.cell(index, 5).data();
	formData['lng'] = dTable.cell(index, 6).data();
	$.ajax({
	    "url" : "clientmanagement/addClients",
	    "data" : JSON.stringify(formData),
	    "type" : 'POST',
	    "contentType" : 'application/json',
	    "xhrFields" : {
		withCredentials : true
	    }
	}).done(function() {
	    alert("added as a potential client");

	    dTable.row(index).remove();
	    dTable.draw();
	    view_button = 1;
	    $('#view_assigned_client').prop('disabled', false);
	});
    });

    $('#add_all_potential').click(function() {
	var data = dTable.rows().data();
	var flag=0;
	$.each(data, function(i) {
	    
	    flag=1;
	    var formData = {};
	    formData['name'] = data[i][0];
	    formData['contact'] = data[i][2];
	    formData['address'] = data[i][1];
	    formData['status'] = "UNASSIGNED";
	    formData['domain'] = data[i][3];
	    formData['employeeid'] = "0";
	    formData['employeename'] = "NOT AVAILABLE";
	    formData['placeid'] = data[i][4];
	    formData['enabled'] = "1";
	    formData['lat'] = data[i][5];
	    formData['lng'] = data[i][6];
	    $.ajax({
		"url" : "clientmanagement/addClients",
		"data" : JSON.stringify(formData),
		"type" : 'POST',
		"contentType" : 'application/json',
		"xhrFields" : {
		    withCredentials : true
		}
	    }).done(function() {
	    });
	});
	dTable.clear();
	dTable.draw();
	if(flag==1){
	    alert(data.length + " client(s) added as potential client");
	}
	else{
	    alert("No Clients To Add!!");
	}



    });

    $('#unwanted_clients').click(function() {

	var index = dTable.row('.selected').index();
	var formData = {};
	var clientname = dTable.cell(index, 0).data();
	var contact = dTable.cell(index, 2).data();
	var address = dTable.cell(index, 1).data();
	var status = "UNASSIGNED";
	var employeeid = "0";
	var employeename = "NOT AVAILABLE"
	var domain = dTable.cell(index, 3).data();

	formData['name'] = clientname;
	formData['contact'] = contact;
	formData['address'] = address;
	formData['status'] = status;
	formData['domain'] = domain;
	formData['employeeid'] = employeeid;
	formData['employeename'] = employeename;
	formData['placeid'] = dTable.cell(index, 4).data();
	formData['enabled'] = "0";
	formData['lat'] = dTable.cell(index, 5).data();
	formData['lng'] = dTable.cell(index, 6).data();
	$.ajax({
	    "url" : "clientmanagement/addClients",
	    "data" : JSON.stringify(formData),
	    "type" : 'POST',
	    "contentType" : 'application/json',
	    "xhrFields" : {
		withCredentials : true
	    }
	}).done(function() {
	    alert("Client Permanently deleted");

	    dTable.row(index).remove();
	    dTable.draw();
	});
    });

    function initMap() {
	$.ajax({
	    "url" : "clientmanagement/findAllClientsByOfficeId",
	    "type" : 'POST',
	    "xhrFields" : {
		withCredentials : true
	    }
	}).done(function(data) {
	    i = 0;
	    while (true) {
		if (i < data.length && data[i].lat != 0 && data[i].lng != 0) {
		    pyrmont = {
			lat : data[i].lat,
			lng : data[i].lng
		    };
		    break;
		}
		if (i >= data.length)
		    break;
		i++;
	    }
	    client_data = data;

	    map_call();
	});
    }
    $('#view_assigned_client').on('click', function() {
	location.href = "clientmanagement#table2";

    });

    $('#submit_values').click(function() {
	$('#add_new_clients').prop('disabled', true);
	$('#unwanted_clients').prop('disabled', true);
	if (view_button == 0) { // *** CHNAGE TO 1 ***
	    $('#view_assigned_client').prop('disabled', false);
	}
	dTable.clear();
	geocodeAddress(geocoder, map);
    });

    function map_call() {

	var input = document.getElementById('postal_code');
	var autocomplete = new google.maps.places.Autocomplete(input);
	geocoder = new google.maps.Geocoder();
	map = new google.maps.Map(document.getElementById('map'), {
	    center : pyrmont,
	    zoom : 11,
	    mapTypeControl : true,
	    mapTypeControlOptions : {
		style : google.maps.MapTypeControlStyle.DROPDOWN_MENU,
		mapTypeIds : [ google.maps.MapTypeId.ROADMAP, ]
	    }
	});

	createMarker1();
	infowindow = new google.maps.InfoWindow();

	service = new google.maps.places.PlacesService(map);
	var distanceservice = new google.maps.DistanceMatrixService();
    }

    function createMarker1() {
	for (var i = 0; i < client_data.length; i++) {
	    if (client_data[i].enabled == 1) {
		var marker = new google.maps.Marker({
		    map : map,
		    position : {
			lat : client_data[i].lat,
			lng : client_data[i].lng
		    },
		    icon : "https://storage.googleapis.com/support-kms-prod/SNP_2752129_en_v0"
		});
	    } else {
		var marker = new google.maps.Marker({
		    map : map,
		    position : {
			lat : client_data[i].lat,
			lng : client_data[i].lng
		    },
		    icon : "https://storage.googleapis.com/support-kms-prod/SNP_2752125_en_v0"
		});
	    }
	}
    }

    function createMarker(place) {

	var marker = new google.maps.Marker({
	    map : map,
	    position : place.geometry.location
	});

	google.maps.event.addListener(marker, 'mouseover', function() {
	    infowindow.setContent(place.name + "<br />" + place.vicinity);
	    infowindow.open(map, this);
	});

	google.maps.event.addListener(marker, 'mouseout', function() {
	    infowindow.close();
	});
    }

    function myFunction() {
	var s = Math.floor((Math.random()) * 1000000000).toString();
	if (s.length == 9) {
	    var q = Math.floor((Math.random()) * 3);
	    var mylist = [ "7", "8", "9" ];
	    var r = mylist[q] + s;
	    return r;
	} else {
	    return myFunction();
	}
    }

    function geocodeAddress(geocoder, resultsMap) {
	var address = document.getElementById('postal_code').value;
	geocoder.geocode({
	    'address' : address
	}, function(results, status) {
	    if (status === google.maps.GeocoderStatus.OK) {
		resultsMap.setCenter(results[0].geometry.location);
		var lat1 = results[0].geometry.location.lat();
		var lng1 = results[0].geometry.location.lng();
		var loc = {
		    lat : lat1,
		    lng : lng1
		};
		pyrmont = loc;
		map_call();
		var radiussize="500";
		if(document.getElementById('radius_size').value == "" || document.getElementById('radius_size').value == null){
		}
		else{
		    radiussize = document.getElementById('radius_size').value;
		}
		var placestype = document.getElementById('places_type').value;
		console.log(radiussize + " , " + placestype);
		service.nearbySearch({
		    location : loc,
		    radius : radiussize,
		    types : [ placestype ]
		}, function(results, status) {
		    if (status === google.maps.places.PlacesServiceStatus.OK) {
			var clients = [];
			for (var i = 0; i < results.length; i++) {
			    clients.push(results[i].place_id);
			}
			$.ajax({
			    url : 'checkmapclient',
			    type : 'POST',
			    contentType : "application/json",
			    data : JSON.stringify({
				clients : clients
			    }),
			    error : function() {
				alert("ERROR");
			    },
			    success : function(data) {
				$('#showPossibleClients').modal('show');
				$.each(data, function(i) {
				    if (data[i] == 1) {
					results[i].domain = placestype;
					createMarker(results[i]);
					dTable.row.add([ results[i].name, results[i].vicinity, myFunction(), results[i].domain, results[i].place_id, results[i].geometry.location.lat(), results[i].geometry.location.lng() ]).draw(false);
				    }
				});
			    }
			});

		    } else {
			alert('No new Clients in this Area. Please refine your search');

		    }
		})

	    }
	});

    }
    initMap();
});