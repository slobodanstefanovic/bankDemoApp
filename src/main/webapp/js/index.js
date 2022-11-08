function validate() {
	let sender = $("#sender").val();
	let receiver = $("#receiver").val();
	let amount = $("#amount").val();
	if (sender === receiver) {
		alert('You can\'t transfer money to your account');
		return false;
	}
	if (!amount.match(/^[+-]?\d+$/)) {
		alert('You have to type numeric amount');
		return false;
	}
	if (parseInt(amount) <= 0) {
		alert('Amount has to be greater then 0');
		return false;
	}
	validateAmount();
	getInputData();
	return true;
}

$(document).ready(function() {
	$("#uploads").submit(function() {
		let accoutsFile = $("#accountsFile").val();
		let transactionsFile = $("#transactionsFile").val();
		let isNotValid = (accoutsFile === '') || (transactionsFile === '');
		if (isNotValid)
			alert('Both files need to be uploaded');
		return !isNotValid;
	});
});

function validateAmount() {
	let sendingAmount = $("#amount").val();
	let senderName = $("#sender").val();
	$.post("ValidationServlet", { amount: sendingAmount, sender: senderName }, function(result) {
		$("#validation_response").text(result);
	});
}

function getInputData() {
	let sendingAmount = $("#amount").val();
	let senderName = $("#sender").val();
	let receiverName = $('#receiver').val();
	$.post("PostTransaction", { amount: sendingAmount, sender: senderName, receiver: receiverName }, function(result) {
		$("#validation_response").text($("#validation_response").text() + result);
	});
}

function getReport() {
	let accountForReport = $("#accountForReport").val();
	$.post("ReturnReportsFromDB", { name: accountForReport }, function(result) {
		$("#transactionsFromSelectedName").html(result);
	});
	return false;

}


