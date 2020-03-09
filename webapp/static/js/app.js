var defaultTimeout = 60 * 1000;
var socket = null;
var webSocketPath = '/event-public';
let indexConnect = 0;
let channelId = '';

$(document).ready(function() {
	let dt = $('#search-from-date,#search-to-date').datetimepicker({
		language : 'vi',
		format : 'd/m/Y H:i',
		formatDate : 'd/m/Y',
		formatTime : 'H:i'
	});
	indexConnect = 0;
	disconnect();
	connect();
});

function getCurrentDate() {
	var currentDate = new Date();

	return date_format(currentDate);
}

function getCurrentTime() {
	var currentDate = new Date();

	return time_format(currentDate);
}

function pad_2(number) {
	return (number < 10 ? '0' : '') + number;
}

function hours(date) {
	var hours = date.getHours();
	if (hours > 12)
		return hours - 12; // Substract 12 hours when 13:00 and more
	return hours;
}

function am_pm(date) {
	if (date.getHours() == 0 && date.getMinutes() == 0
			&& date.getSeconds() == 0)
		return ''; // No AM for MidNight
	if (date.getHours() == 12 && date.getMinutes() == 0
			&& date.getSeconds() == 0)
		return ''; // No PM for Noon
	if (date.getHours() < 12)
		return ' AM';
	return ' PM';
}

function date_format(date) {
	return pad_2(date.getDate()) + '/' + pad_2(date.getMonth() + 1) + '/'
			+ (date.getFullYear() + ' ').substring(2) + pad_2(hours(date))
			+ ':' + pad_2(date.getMinutes()) + ':' + pad_2(date.getSeconds())
			+ am_pm(date);
}

function time_format(date) {
	return pad_2(hours(date)) + ':' + pad_2(date.getMinutes()) + ':'
			+ pad_2(date.getSeconds()) + am_pm(date);
}

function connect() {
	socket = new SockJS(webSocketPath);
	socket.onmessage = function(msg) {
		handleData(JSON.parse(msg.data));
	}
	socket.onerror = function(error) {
		indexConnect++;
		console.log(error);
	}
	socket.onclose = function(event) {
		indexConnect++;
		if (indexConnect < 100) {
			connect();
		} else {
			console.log("Reconnect over 100 time");
		}
		let reason;
		if (event.code == 1000)
			reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
		else if (event.code == 1001)
			reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
		else if (event.code == 1002)
			reason = "An endpoint is terminating the connection due to a protocol error";
		else if (event.code == 1003)
			reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
		else if (event.code == 1004)
			reason = "Reserved. The specific meaning might be defined in the future.";
		else if (event.code == 1005)
			reason = "No status code was actually present.";
		else if (event.code == 1006)
			reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
		else if (event.code == 1007)
			reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
		else if (event.code == 1008)
			reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
		else if (event.code == 1009)
			reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
		else if (event.code == 1010) // Note that this status code is not
			reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: "
					+ event.reason;
		else if (event.code == 1011)
			reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
		else if (event.code == 1015)
			reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
		else
			reason = "Unknown reason";
		console.log(reason);
	};
}

function handleData(data) {
	let commandName = data.commandName;

	if (commandName == 'undified' || commandName == null || commandName == '') {
		return;
	}

	let channelIdTemp = data.channelId;

	if (channelIdTemp == 'undified' || channelIdTemp == null
			|| channelIdTemp == '') {
		return;
	}

	if (commandName == 'PUBLIC-NEW-CONNECTION') {
		channelId = channelIdTemp;
	} else {
		let payload = data.payload;
		let alertMessage = '<a class="dropdown-item d-flex align-items-center" href="#"><div class="mr-3"><div class="icon-circle bg-primary"><i class="fas fa-file-alt text-white"></i></div></div><div><div class="small text-gray-500">'
				+ getCurrentDate()
				+ '</div><span class="font-weight-bold">Báo cáo có mã '
				+ payload.transactionId
				+ ' hiện tại đã '
				+ payload.status
				+ '</span></div></a>';
		$('#alertCenter').append(alertMessage);
		let count = $("#alertCenter a").length;
		$('#alertCount').text(count);
	}
}
function disconnect() {
	if (socket !== null) {
		socket.disconnect();
	}
}

function configAjax() {
	$.ajaxPrefilter(function(options, original_Options, jqXHR) {
		options.async = true;
	});
}

function blockUI() {
	$
			.blockUI({
				baseZ : 3000,
				blockMsgClass : 'blockMsgNew',
				message : '<img src="img/loading.gif" width = 50 height = 50/>&nbsp;&nbsp;&nbsp;&nbsp;<h5 style ="animation: blink 1s infinite;color: #fff">Xin hãy đợi...</h5>'
			});
}

function blockUIDialog(message) {
	let modalHtml = '<div style = "padding-top: 0px" tabindex="-1" role="dialog">'
			+ '<div class="modal-dialog" role="document">'
			+ '  <div class="modal-content">'
			+ ' <div class="modal-header">'
			+ '<h5 class="modal-title"><i class="fa fa-exclamation-circle fa-fw" aria-hidden="true"></i><small><strong>Thông báo</strong></small></h5>'
			+ '</div>'
			+ '<div class="modal-body">'
			+ '<p>'
			+ message
			+ '</p>'
			+ '</div>'
			+ '<div class="modal-footer">'
			+ '<button type="button" style = "cursor: pointer" onclick = "unblockUI()" class="btn btn-secondary" data-dismiss="modal">Đóng</button>'
			+ '</div></div></div></div>';
	$.blockUI({
		baseZ : 3000,
		blockMsgClass : 'blockMsgNew',
		message : modalHtml
	});
}

function unblockUI() {
	$.unblockUI();
}

function invokeJsonFunction(url, datainput, method, timeout, functionName,
		errorHandle, isSendData, closeBlockUI) {
	blockUI();
	configAjax();

	if (isSendData) {
		$.ajax({
			url : url,
			crossDomain : true,
			cache : false,
			dataType : 'json',
			type : method,
			data : JSON.stringify(datainput),
			contentType : 'application/json',
			timeout : timeout,
			async : true,
			processData : false,
			success : function(data, textStatus, jQxhr) {
				functionName(data, datainput);
				if (closeBlockUI) {
					unblockUI();
				}
			},
			error : function(jqXhr, textStatus, errorThrown) {
				errorHandle(textStatus);
				unblockUI();
			}
		});
	} else {
		$.ajax({
			url : url,
			crossDomain : true,
			cache : false,
			dataType : 'json',
			type : method,
			contentType : 'application/json',
			timeout : timeout,
			async : true,
			processData : false,
			success : function(data, textStatus, jQxhr) {
				functionName(data, datainput);
				unblockUI();
			},
			error : function(jqXhr, textStatus, errorThrown) {
				errorHandle(textStatus);
				unblockUI();
			}
		});
	}
}