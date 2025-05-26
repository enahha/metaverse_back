<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
	</body>
</html>

<script>
	var payCode = '${payCode}';
	var resultCode = '${resultCode}';
	var resultApiCode = '${resultApiCode}';
	var resultMsg = '${resultMsg}';
	var redirectUrl = '${redirectUrl}'; // payment/paymentCallback.vue에서 다음으로 이동할 front path
	var key = '${key}';
	var orderId = '${orderId}';
	
	
	var redirectFrontBaseUri = '${domain}' + '/#/payment/paymentCallback';
	var params = "?resultApiCode=" + resultApiCode + "&resultCode=" + resultCode + "&payCode=" + payCode + "&orderId=" + orderId + "&redirectUrl=" + redirectUrl + "&key=" + key + "&resultMsg=" + resultMsg;
	var redirectFrontUri = redirectFrontBaseUri + params;
	
	// alert(redirectUri);
	window.location.href = redirectFrontUri;
</script>