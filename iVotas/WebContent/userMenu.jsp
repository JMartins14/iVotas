<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>iVotas - Menu Inicial</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
	integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb"
	crossorigin="anonymous">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
		integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js"
		integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh"
		crossorigin="anonymous"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"
		integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ"
		crossorigin="anonymous"></script>
		<center>
	<s:form action="goVote">
		<div class="form-group col-lg-5">
		<br>
		<br>
		<br>
			<h2>Seja bem-vindo <s:property value="username"/>!</h2>
		<s:actionmessage cssClass="alert alert-success"/>
		<s:actionerror cssClass="alert alert-danger"/>
			<s:action name="getActiveElections">
				</s:action>
				<br>
				<br>
				<br>
				<br>
				<br>
				<br>
				<br>				
			<s:if test="#attr.elections.size()>0">			
				<s:hidden name="username" value="%{#attr.user.getName()}"></s:hidden>
			
				<s:if test="#attr.elections.size()==1">
					<h5>Você pode votar em 1 eleição!</h5>
				</s:if>
				<s:else>
					<h5>Você pode votar em <s:property value="#attr.elections.size()"/> eleições!</h5>
				</s:else>
				<br>
						<div class="form-group col-lg-4">
				
			<s:select cssClass="form-control" name="electionTitle"
							list="#attr.elections"></s:select>
							</div>
			<s:submit cssClass="btn btn-primary btn-md" value="Escolher Eleição"></s:submit>
		</s:if>
		<s:if test="#attr.elections.size()==0">
			<h5>Neste momento não existem eleições ativas em que possa votar!</h5>
		
		</s:if>
		<br>
		<br>
					</div>
					<s:if test="%{#attr.user.isAssociated()==false}">	
					<a class="btn btn-info btn-sm"  href="https://www.facebook.com/v2.2/dialog/oauth?client_id=240183946519643&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FAdmin%2FfacebookCode&scope=publish_actions
					">Associar Utilizador ao Facebook</a>
							</s:if>
							<br>
							<br>
						
					
	</s:form>

	<button class="btn btn-default" type="submit" onclick="remove();">LogOut</button>
		</center>
	 <script type="text/javascript">

        var websocket = null;

        window.onload = function() { // URI = ws://2.80.47.233:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/iVotas/ws');
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

           websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
       		var s = "${session.username}";
			var obj = '{"ADD_USER" :  '+s+'}';
			websocket.send(obj);
        }
        
        function onClose(event) {
            
        }
        
        function onMessage(message) { // print the received message

        }
        
        function onError(event) {
            
        }
        
        function doSend() {
           
        }
        function remove(){
        	var s = "${session.username}";
			var obj = '{"DEL_USER" :  '+s+'}';
			websocket.send(obj);
        	window.location.href = "signin.jsp";
        	
        }
     

    </script>

</body>
</html>