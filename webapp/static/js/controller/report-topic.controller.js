let rootReport = '/report';
let registerExportReport = rootReport + '/register-export';

function callRequest() {
	let fullRequest = registerExportReport;
	let loginInput = {
		commandName : "EXPORT-REPORT",
		channelId : channelId,
		parameters : [ {
			key : "reportName",
			value : $('#reportName').val()
		}, {
			key : "fromDate",
			value : $('#search-from-date').val()
		}, {
			key : "toDate",
			value : $('#search-to-date').val()
		} ]
	};
	invokeJsonFunction(fullRequest, loginInput, 'POST', defaultTimeout,
			handleSuccessed, handleError, true, false);
}

function handleSuccessed(res, rawInput) {
	let message = res.des;
	blockUIDialog(message);
}

function handleError(errorEvent) {
	console.log("login error: " + errorEvent);
}