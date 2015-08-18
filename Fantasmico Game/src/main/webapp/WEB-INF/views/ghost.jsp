<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<%@ page isELIgnored="false" %>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
		<meta name="HandheldFriendly" content="True" />
		<title>Welcome Optimal Ghost</title>
		<link href="resources/css/reset.css" rel="stylesheet" type="text/css"/>
		<link href="resources/css/WebAppStyles.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript"> 
		var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
		</script> 
	</head> 
	<body>		
		<header>
			<img alt="Ghost game title" src="resources/images/title.png">
		</header>
		
		<aside class="contentBlock">
			<div id="userWinsMessage">
				<h1>You win!</h1>
			</div>
		
			<div id="computerWinsMessage">
				<h1>Fantasmiko wins!</h1>
			</div>
		</aside>
		
		<aside id="letters"  class="contentBlock">
		</aside>
		
		<aside id="inputLetter" class="contentBlock">
			<input id="inputString" data-bind="value: addedLetter, valueUpdate: 'keyup'" maxlength="1"/>
		</aside>
		
		<div class="dynamic">
			<button id="btnStartGame" class="button" data-bind="click: startGame">Start</button>
		</div>
		
		<div class="dynamic">
			<button id="btnEndGame" class="button" data-bind="click: endGame">Restart</button>
		</div>
		
		<div class="dynamic">
			<img id="imgFantasmico" alt="Fantasmico" src="resources/images/phantom.png">
		</div>
		
		<script type="text/javascript" src="resources/scripts/lib/jquery-2.0.3.min.js"></script>
		<script type="text/javascript" src="resources/scripts/lib/knockout-2.3.0.js"></script>
		<script type="text/javascript" src="resources/scripts/lib/jquery-cookie.js"></script>
		<script type="text/javascript" src="resources/scripts/lib/css3-mediaqueries.js"></script>
		<script type="text/javascript" src="resources/scripts/lib/modernizr.js"></script>
		<script type="text/javascript" src="resources/scripts/WebAppEntities.js"></script>
		<script type="text/javascript" src="resources/scripts/WebAppScripts.js"></script>
	</body>
</html>
