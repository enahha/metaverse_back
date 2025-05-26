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
	var frontPath = '${frontPath}';
	var frontLocale = '${frontLocale}';
	var adcd = '${adcd}';
	var token = '${token}';
	var userId = '${userId}';
	
	var redirectFrontBaseUri = '${domain}' + '/#/callback/instagramLoginRedirect';
	var params = '?frontPath=' + frontPath + '&frontLocale=' + frontLocale + '&adcd=' + adcd + '&token=' + token + '&userId=' + userId;
	
	var redirectUri = redirectFrontBaseUri + params;
	
	// alert(redirectUri);
	window.location.href = redirectUri;
</script>