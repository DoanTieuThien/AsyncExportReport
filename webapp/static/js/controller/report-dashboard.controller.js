let rootReport = '/report';
let exportReport = rootReport + '/load-all-report';

$(document).ready(function() {
	callRequest();
});

function callRequest() {
	let fullRequest = exportReport;
	invokeJsonFunction(fullRequest, null, 'GET', defaultTimeout,
			handleSuccessed, handleError, false, true);
}

function handleSuccessed(res, rawInput) {
	let message = res.des;
	let code = res.code;

	if (code != 'API-00000') {
		return;
	}
	let reportDataList = res.payload;

	if (reportDataList == 'undified' || reportDataList == '') {
		return;
	}

	let count = reportDataList.length;

	if (count == 0) {
		return;
	}
	for (i = 0; i < count; i++) {
		let reportData = reportDataList[i];
		let noNumber = $('#reportDataList tbody tr').length + 1;
		let status = reportData.status;

		if ('EXPORT-FINISEHED' == status) {
			$('#reportDataList tbody').append(
					'<tr style = "cursor:pointer" id = "'
							+ reportData.transactionId
							+ '" onclick = "downloadReport()"><td>' + noNumber
							+ '</td><td>' + reportData.commandName
							+ '</td><td>' + reportData.transactionId
							+ '</td><td>' + reportData.channelId + '</td><td>'
							+ reportData.status + '</td></tr>');
		} else {
			$('#reportDataList tbody').append(
					'<tr style = "cursor:pointer" id = "'
							+ reportData.transactionId + '" ><td>' + noNumber
							+ '</td><td>' + reportData.commandName
							+ '</td><td>' + reportData.transactionId
							+ '</td><td>' + reportData.channelId + '</td><td>'
							+ reportData.status + '</td></tr>');
		}
	}
}

function handleError(errorEvent) {

}

function downloadReport() {
	window.location.href = '/report/download-report';
}