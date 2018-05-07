<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>iVotas</title>
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


	<s:form action="sign">
	<br>	
	<center>
	<div class="form-group col-lg-5">
	<h1>Bem-vindo ao iVotas</h1>
	<br>
	<h4>Aplicação de Voto Eletrónico da Universidade de Coimbra</h4>
	</div>
	<div class="form-group col-lg-2">
	
	<br>
	<br>
		<h5>Username</h5>
		<s:textfield name="username" cssClass="form-control input-sm"/><br>
						<s:fielderror cssClass="alert alert-danger actionError" fieldName="username"/>
	<br>
		<h5>Password</h5>
		<s:password name="password" cssClass="form-control input-sm"/><br>
						<s:fielderror cssClass="alert alert-danger actionError" fieldName="password"/>
										<s:fielderror cssClass="alert alert-danger actionError" fieldName="result"/>
		
		<s:submit cssClass="btn btn-default btn-md" value="Login" /> <br><br>
		<br>
		<br>
		<a class="btn btn-primary btn-md" href="https://www.facebook.com/v2.2/dialog/oauth?client_id=240183946519643&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FAdmin%2FloginfacebookCode&scope=publish_actions">Login com o Facebook</a>
		</div>
	</center>
	</s:form>
	</body>
</html>